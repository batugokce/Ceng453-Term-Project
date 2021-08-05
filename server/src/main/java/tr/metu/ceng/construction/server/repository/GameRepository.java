package tr.metu.ceng.construction.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.metu.ceng.construction.server.model.Game;

/**
 * Repository interface of {@code Game} class for accessing data.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
