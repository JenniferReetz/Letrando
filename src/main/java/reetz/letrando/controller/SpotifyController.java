package reetz.letrando.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.model.Playlist;
import reetz.letrando.repository.PlaylistRepository;
import reetz.letrando.service.ExternalService;
import reetz.letrando.service.SpotifyAuthService;

import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @Autowired
    private ExternalService externalService;

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @PostMapping("/add-music/{playlistId}")
    public ResponseEntity<String> addMusicToPlaylist(@PathVariable Long playlistId, @RequestBody String musicId) {
        String accessToken = spotifyAuthService.getAccessToken();
        MusicDTO musicDTO = externalService.getMusicFromSpotify(musicId, accessToken);

        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) {
            return ResponseEntity.status(404).body("Playlist não encontrada");
        }

        playlist.getMusicIds().add(musicDTO.id());

        playlistRepository.save(playlist);

        return ResponseEntity.ok("Música adicionada à playlist com sucesso");
    }

    @GetMapping("/music/{musicId}")
    public ResponseEntity<MusicDTO> getMusicInfo(@PathVariable String musicId) {
        String accessToken = spotifyAuthService.getAccessToken();

        MusicDTO musicDTO = externalService.getMusicFromSpotify(musicId, accessToken);

        return ResponseEntity.ok(musicDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MusicDTO>> searchMusic(@RequestParam("q") String query) {
        String accessToken = spotifyAuthService.getAccessToken();
        List<MusicDTO> musics = externalService.searchTracks(query, accessToken);
        return ResponseEntity.ok(musics);
    }
}