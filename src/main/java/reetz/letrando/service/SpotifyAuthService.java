package reetz.letrando.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class SpotifyAuthService {

    private final String clientId = "${CLIENT_ID}";
    private final String clientSecret = "${CLIENT_SECRETS}";

    private String cachedToken;
    private Instant expiresAt;

    public String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(expiresAt)) {
            return cachedToken;
        }

        RestTemplate restTemplate = new RestTemplate();

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        cachedToken = (String) body.get("access_token");
        int expiresIn = (int) body.get("expires_in");

        expiresAt = Instant.now().plusSeconds(expiresIn - 60);

        return cachedToken;
    }
}