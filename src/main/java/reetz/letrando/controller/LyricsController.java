package reetz.letrando.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reetz.letrando.DTO.LyricResponse;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.service.ExternalService;
import reetz.letrando.service.SpotifyAuthService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lyrics")
public class LyricsController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private ExternalService externalService;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getLyricsFromNameOnly(@RequestParam String songName) {
        try {
            //  Pega token e busca a música
            String token = spotifyAuthService.getAccessToken();
            List<MusicDTO> results = externalService.searchTracks(songName, token);

            if (results.isEmpty()) {
                return ResponseEntity.status(404).body("Música não encontrada no Spotify.");
            }

            MusicDTO music = results.get(0); // pega a primeira encontrada
            String artist = music.artist();
            String track = music.name();

            //  Chama a Lyrics.ovh
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://api.lyrics.ovh/v1")
                    .pathSegment(artist)
                    .pathSegment(track)
                    .build()
                    .toUriString();
            ResponseEntity<Map> lyricsResponse = restTemplate.getForEntity(url, Map.class);

            if (!lyricsResponse.getStatusCode().is2xxSuccessful() || !lyricsResponse.getBody().containsKey("lyrics")) {
                return ResponseEntity.status(404).body("Letra não encontrada.");
            }

            String lyrics = (String) lyricsResponse.getBody().get("lyrics");

            //  Monta resposta
            LyricResponse response = new LyricResponse(track, artist, lyrics);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // só pra debug mesmo
            return ResponseEntity.status(500).body("Erro ao buscar letra.");
        }
    }
}