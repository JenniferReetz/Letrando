package reetz.letrando.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reetz.letrando.model.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
