package reetz.letrando.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.DTO.PlaylistDTO;
import reetz.letrando.model.Playlist;
import reetz.letrando.model.Usuario;
import reetz.letrando.repository.PlaylistRepository;
import reetz.letrando.repository.UsuarioRepository;

import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private  SpotifyAuthService spotifyAuthService;
    @Autowired
    private  ExternalService externalService;
    @Autowired
    private  UsuarioRepository usuarioRepository;


    public PlaylistDTO create(PlaylistDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.userId()).orElseThrow();

        Playlist playlist = new Playlist();
        playlist.setNome(dto.name());
        playlist.setUsuario(usuario);
        playlist.setMusicIds(dto.musicIds());

        Playlist saved = playlistRepository.save(playlist);

        return toDTO(saved);
    }
    public List<MusicDTO> getPlaylistMusics(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        List<String> musicIds = playlist.getMusicIds();

        String accessToken = spotifyAuthService.getAccessToken();

        return musicIds.stream()
                .map(id -> externalService.getMusicFromSpotify(id, accessToken))
                .toList();
    }
    public List<PlaylistDTO> getAllByUser(Long userId) {
        return playlistRepository.findAllByUsuarioId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public PlaylistDTO getById(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow();
        return toDTO(playlist);
    }

    public PlaylistDTO update(Long id, PlaylistDTO dto) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow();
        playlist.setNome(dto.name());
        playlist.setMusicIds(dto.musicIds());
        return toDTO(playlistRepository.save(playlist));
    }

    public void delete(Long id) {
        playlistRepository.deleteById(id);
    }

    private PlaylistDTO toDTO(Playlist p) {
        return new PlaylistDTO(p.getId(), p.getNome(), p.getUsuario().getId(), p.getMusicIds());
    }

}
