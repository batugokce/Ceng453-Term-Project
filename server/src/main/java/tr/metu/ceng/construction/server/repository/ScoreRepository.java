package tr.metu.ceng.construction.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.metu.ceng.construction.server.model.Score;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface of {@code Score} class for accessing data related with Score entity.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    /**
     * Creates a query to find all scores within a limited size.
     *
     * @return sorted Score list
     */
    @Query(value ="SELECT score.ownerPlayer.username, score.score, score.createdDate FROM Score score ORDER BY score.score DESC, score.createdDate DESC")
    List<Object[]> findAllTimesScores();


    /**
     * Creates a query to find the scores after a given date within a limited size.
     *
     * @param givenDate This parameter is used for finding scores whose date is after this given date.
     * @return sorted Score list
     */
    @Query(value = "SELECT score.ownerPlayer.username, score.score, score.createdDate FROM Score score WHERE score.createdDate >= :givenDate " +
            "ORDER BY score.score DESC, score.createdDate DESC")
    List<Object[]> findScoresInRange(@Param("givenDate") LocalDate givenDate);
}
