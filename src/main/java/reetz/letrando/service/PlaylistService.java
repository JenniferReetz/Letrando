package reetz.letrando.service;

import org.springframework.stereotype.Service;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.model.Playlist;
import reetz.letrando.repository.PlaylistRepository;

import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SpotifyAuthService spotifyAuthService;
    private final ExternalService externalService;

    public PlaylistService(
            PlaylistRepository playlistRepository,
            SpotifyAuthService spotifyAuthService,
            ExternalService externalService
    ) {
        this.playlistRepository = playlistRepository;
        this.spotifyAuthService = spotifyAuthService;
        this.externalService = externalService;
    }

    public List<MusicDTO> getPlaylistMusics(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        List<String> musicIds = playlist.getMusicIds(); // lista de IDs

        String accessToken = spotifyAuthService.getAccessToken(); // token do client_credentials

        return musicIds.stream()
                .map(id -> externalService.getMusicFromSpotify(id, accessToken))
                .toList();
    }
}
