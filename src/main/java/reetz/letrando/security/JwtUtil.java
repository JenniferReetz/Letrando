package reetz.letrando.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String gerarToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 1 hora
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public String extrairUsername(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token); // se for inválido, vai lançar exceção
        return decodedJWT.getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}