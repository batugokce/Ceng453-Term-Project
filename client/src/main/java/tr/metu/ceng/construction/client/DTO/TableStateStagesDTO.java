package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds stages of the table state
 * Include two table state dto; one for before compute move, the other one for the final table state
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableStateStagesDTO {

    /**
     * Holds state of the table right before the compute move
     */
    private TableStateDTO tableStateBeforeComputeMove;

    /**
     * Holds final state of the table after computer move
     */
    private TableStateDTO finalTableState;

}