package pl.dolien.shop.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String AUTH_PATH = "/api/v1/auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static String extractJwt(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX))
            return authHeader.substring(BEARER_PREFIX_LENGTH);

        return null;
    }

    public static boolean isAuthPath(HttpServletRequest request) {
        return request.getServletPath().contains(AUTH_PATH);
    }
}
