package com.example.server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.HttpExchange;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.RegisteredClaims.ISSUER;

public class AuthUtils {

    private static final String SECRET_KEY = "12321";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    public static String generateToken(String username) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .sign(ALGORITHM);
    }

    public static boolean isValidToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private static Map<String, String> tokenMap = new HashMap<>();

    public static void storeToken(String username, String token) {
        tokenMap.put(username, token);
    }

    public static String getTokenFromCookies(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                String[] pairs = cookie.split(";");
                for (String pair : pairs) {
                    String[] keyValue = pair.trim().split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("token")) {
                        return keyValue[1];
                    }
                }
            }
        }
        return null;
    }
}
