package tr.metu.ceng.construction.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.server.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an overall score for a finished game.
 * It has a score and created date.
 * A score entity should not exist without a player.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Score extends BaseEntity {

    /**
     * Indicates the score(point).
     */
    @Column(name="SCORE")
    private Integer score;

    /**
     * Indicates the date when the score recorded.
     */
    @Column(name="CREATED_DATE")
    private LocalDate createdDate;

    /**
     * This attribute indicates the owner player of the score entity.
     * It should not be null.
     */
    @ManyToOne
    @JsonIgnore
    private Player ownerPlayer;

    /**
     * This method checks whether a given object is equals to this score or not.
     * If id, score and created date of two scores are equal, these are same.
     *
     * @param object this is the object to check equality.
     * @return a boolean that indicates if two objects are equal or not.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Score)) return false;
        Score scoreObject = (Score) object;
        return  getId().equals(scoreObject.getId()) &&
                score.equals(scoreObject.score) &&
                createdDate.equals(scoreObject.createdDate);
    }

    /**
     * Returns a hash code for this {@code Score}.
     *
     * @return a hash code value for this object based on id, score and created date.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), score, createdDate);
    }

}
