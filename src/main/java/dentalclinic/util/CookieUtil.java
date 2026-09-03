package dentalclinic.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

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