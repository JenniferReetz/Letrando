package reetz.letrando.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.DTO.PlaylistDTO;
import reetz.letrando.DTO.PlaylistUpdateDTO;
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


    public PlaylistDTO create(PlaylistDTO dto, Usuario usuario) {
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
    public PlaylistDTO update(Long id, PlaylistUpdateDTO dto, Usuario usuario) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow();

        if (!playlist.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar essa playlist.");
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            playlist.setNome(dto.name());
        }

        if (dto.musicIds() != null) {
            playlist.setMusicIds(dto.musicIds());
        }

        return toDTO(playlistRepository.save(playlist));
    }

    public void delete(Long id, Usuario usuario) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow();

        if (!playlist.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar essa playlist.");
        }

        playlistRepository.delete(playlist);
    }

    private PlaylistDTO toDTO(Playlist p) {
        return new PlaylistDTO(p.getId(), p.getNome(), p.getMusicIds());
    }

}
