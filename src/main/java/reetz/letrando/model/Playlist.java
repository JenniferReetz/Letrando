package reetz.letrando.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ElementCollection
    @CollectionTable(name = "playlist_music", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "spotify_music_id")
    private List<String> musicIds = new ArrayList<>();

    // Construtores, getters e setters...

    public Playlist() {}

    public Playlist(Usuario usuario, String nome, List<String> musicIds) {
        this.usuario = usuario;
        this.musicIds = musicIds;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsurario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<String> getMusicIds() {
        return musicIds;
    }

    public void setMusicIds(List<String> musicIds) {
        this.musicIds = musicIds;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }
}