package reetz.letrando.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reetz.letrando.DTO.MusicDTO;

import java.util.List;
import java.util.Map;

@Service
public class ExternalService {
    public MusicDTO getMusicFromSpotify(String musicId, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.spotify.com/v1/tracks/" + musicId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();

        String name = (String) body.get("name");
        String album = ((Map<String, Object>) body.get("album")).get("name").toString();
        List<Map<String, Object>> artistsList = (List<Map<String, Object>>) body.get("artists");
        String artist = artistsList.get(0).get("name").toString();

        return new MusicDTO(musicId, name, album, artist);
    }


}
