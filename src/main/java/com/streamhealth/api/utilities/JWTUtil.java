package com.streamhealth.api.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public class JWTUtil {
    public static String extractLoginFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("login").asString();
    }
}
