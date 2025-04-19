package reetz.letrando.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reetz.letrando.DTO.MusicDTO;
import reetz.letrando.DTO.PlaylistDTO;
import reetz.letrando.DTO.PlaylistUpdateDTO;
import reetz.letrando.model.Usuario;
import reetz.letrando.repository.UsuarioRepository;
import reetz.letrando.service.PlaylistService;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> create(@RequestBody PlaylistDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByUsername(username);

        return ResponseEntity.ok(playlistService.create(dto, usuario));
    }

    @GetMapping("/user")
    public ResponseEntity<List<PlaylistDTO>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username);

        return ResponseEntity.ok(playlistService.getAllByUser(usuario.getId()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> update(
            @PathVariable Long id,
            @RequestBody PlaylistUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByUsername(username);

        return ResponseEntity.ok(playlistService.update(id, dto, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByUsername(username);

        playlistService.delete(id, usuario);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/musics")
    public ResponseEntity<List<MusicDTO>> getMusics(@PathVariable Long id) {
        List<MusicDTO> musics = playlistService.getPlaylistMusics(id);
        return ResponseEntity.ok(musics);
    }


}