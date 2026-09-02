package dentalclinic.service.notification;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OPTIONAL, dependency-free real email sender using raw SMTP commands
 * over a socket. Uses only javax.net.ssl and java.net/java.io - all
 * part of the core JDK, so this adds ZERO new Maven dependencies.
 *
 * NOT wired in by default - EmailNotificationChannel uses the safe
 * simulated path. To actually use this, you would need:
 *   1. A Gmail account with 2-Step Verification enabled
 *   2. An "App Password" generated at myaccount.google.com/apppasswords
 *      (NOT your normal Gmail password - Google blocks raw SMTP login
 *      with a regular password for security reasons)
 *   3. Your network/campus WiFi allowing outbound traffic on port 587
 *      (some networks block this - if the connection times out, that's
 *      almost certainly why, not a bug in this code)
 *
 * This class is a genuine implementation of the SMTP protocol's initial
 * handshake (EHLO, STARTTLS, AUTH LOGIN, MAIL FROM, RCPT TO, DATA) -
 * worth explaining in your report if you use it, since writing this by
 * hand demonstrates a real understanding of how email delivery works
 * under the hood, rather than just calling a library method.
 */
public class EmailSenderSmtp {

    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String appPassword;

    public EmailSenderSmtp(String smtpHost, int smtpPort, String username, String appPassword) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.appPassword = appPassword;
    }

    public boolean send(String toAddress, String subject, String body) {
        try (Socket rawSocket = new Socket(smtpHost, smtpPort)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(rawSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(rawSocket.getOutputStream(), true);

            readResponse(reader); // server greeting
            sendCommand(writer, reader, "EHLO localhost");
            sendCommand(writer, reader, "STARTTLS");

            // Upgrade the plain socket to an encrypted one, mid-connection -
            // this is what STARTTLS actually means at the protocol level.
            SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket(rawSocket, smtpHost, smtpPort, true);
            sslSocket.startHandshake();

            BufferedReader secureReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter secureWriter = new PrintWriter(sslSocket.getOutputStream(), true);

            sendCommand(secureWriter, secureReader, "EHLO localhost");
            sendCommand(secureWriter, secureReader, "AUTH LOGIN");
            sendCommand(secureWriter, secureReader, base64(username));
            sendCommand(secureWriter, secureReader, base64(appPassword));
            sendCommand(secureWriter, secureReader, "MAIL FROM:<" + username + ">");
            sendCommand(secureWriter, secureReader, "RCPT TO:<" + toAddress + ">");
            sendCommand(secureWriter, secureReader, "DATA");

            String message = "Subject: " + subject + "\r\n"
                    + "From: " + username + "\r\n"
                    + "To: " + toAddress + "\r\n"
                    + "\r\n"
                    + body + "\r\n.";
            sendCommand(secureWriter, secureReader, message);
            sendCommand(secureWriter, secureReader, "QUIT");

            return true;
        } catch (IOException e) {
            System.err.println("Real SMTP send failed (falling back to simulated): " + e.getMessage());
            return false;
        }
    }

    private void sendCommand(PrintWriter writer, BufferedReader reader, String command) throws IOException {
        writer.print(command + "\r\n");
        writer.flush();
        readResponse(reader);
    }

    private String readResponse(BufferedReader reader) throws IOException {
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
            // SMTP multi-line responses use a hyphen after the code
            // (e.g. "250-") until the final line (e.g. "250 "); a space
            // means this is the last line of the response.
            if (line.length() >= 4 && line.charAt(3) == ' ') {
                break;
            }
        }
        return response.toString();
    }

    private String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}