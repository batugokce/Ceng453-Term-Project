package tr.metu.ceng.construction.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.metu.ceng.construction.server.model.Player;

/**
 * Repository interface of {@code Player} class for accessing data related with Player entity.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    Player findByEmailAddress(String emailAddress);

    Player findByPasswordResetToken(String passwordResetToken);

    boolean existsByEmailAddress(String emailAddress);
}
