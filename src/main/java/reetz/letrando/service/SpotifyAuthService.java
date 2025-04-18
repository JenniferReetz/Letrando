package reetz.letrando.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant expiresAt;

    public String getAccessToken() {
        // Codificação base64 para autenticação
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // Cabeçalhos da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // Corpo da requisição
        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        // Enviar requisição POST
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        System.out.println("Resposta do Spotify: " + response.getBody());

        Map body = response.getBody();
        if (body != null && body.containsKey("access_token")) {
            return (String) body.get("access_token");
        } else {
            System.out.println("Erro ao obter token: " + body);
            return null;
        }
    }
}