package tr.metu.ceng.construction.server.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * This abstract class is a common base class for other classes to extend.
 * It has only one attribute which is the id.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * Holds an identifier for each entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

}
