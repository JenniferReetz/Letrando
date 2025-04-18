package reetz.letrando.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reetz.letrando.DTO.MusicDTO;

import java.util.ArrayList;
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

    public List<MusicDTO> searchTracks(String query, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=10";

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> tracks = (Map<String, Object>) body.get("tracks");
        List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");

        List<MusicDTO> result = new ArrayList<>();

        for (Map<String, Object> item : items) {
            String id = (String) item.get("id");
            String name = (String) item.get("name");

            Map<String, Object> albumMap = (Map<String, Object>) item.get("album");
            String album = (String) albumMap.get("name");

            List<Map<String, Object>> artists = (List<Map<String, Object>>) item.get("artists");
            String artist = (String) artists.get(0).get("name");

            result.add(new MusicDTO(id, name, album, artist));
        }

        return result;
    }
}
