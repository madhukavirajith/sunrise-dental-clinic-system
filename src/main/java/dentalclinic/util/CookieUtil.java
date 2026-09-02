package dentalclinic.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 * Small shared helper for reading/writing cookies, used by LoginServlet
 * and AppointmentServlet. Centralising this avoids duplicating the same
 * lookup-by-name loop and URL encode/decode logic in multiple servlets.
 */
public class CookieUtil {

    public static Optional<String> readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .map(c -> urlDecode(c.getValue()))
                .findFirst();
    }

    public static Cookie createCookie(String name, String value, int maxAgeSeconds, String path) {
        Cookie cookie = new Cookie(name, urlEncode(value));
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath(path);
        // httpOnly means JavaScript on the page cannot read this cookie -
        // a basic but genuine security measure against XSS-based cookie
        // theft, worth mentioning under the brief's "Ethical" EDGE attribute.
        cookie.setHttpOnly(true);
        return cookie;
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String urlDecode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}