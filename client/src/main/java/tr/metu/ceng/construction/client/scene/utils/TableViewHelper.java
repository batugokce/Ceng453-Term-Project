package tr.metu.ceng.construction.client.scene.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import tr.metu.ceng.construction.client.DTO.ScoreDTO;
import tr.metu.ceng.construction.client.controller.LeaderBoardController;

import java.util.List;

/**
 * Responsible for preparing getter methods for table view column fields and data
 */
public class TableViewHelper {

    private static final LeaderBoardController leaderBoardController = new LeaderBoardController();

    /**
     * Calls the controller method to get data to be filled in the table.
     *
     * @param scoreListType this is the button label representing the date range of desired type of listing scores.
     * It can be all-times, weekly ,or monthly.
     * @return an observable list of ScoreDTOs.
     */
    public static ObservableList<ScoreDTO> getScoreDTOListByScoreListType(String scoreListType) {
        List<ScoreDTO> allTimesScoreList = leaderBoardController.getScoresBySpecificUri(scoreListType);

        ObservableList<ScoreDTO> observableList = FXCollections.<ScoreDTO>observableArrayList();
        observableList.addAll(allTimesScoreList);
        return observableList;
    }

    /**
     * @return ScoreDTO Number TableColumn.
     */
    public static TableColumn<ScoreDTO, Integer> getNumberColumn() {
        TableColumn<ScoreDTO, Integer> numberCol = new TableColumn<>("#");
        PropertyValueFactory<ScoreDTO, Integer> numberCellValueFactory = new PropertyValueFactory<>("number");
        numberCol.setSortable(false);
        numberCol.setMinWidth(20);
        numberCol.setCellValueFactory(numberCellValueFactory);
        return numberCol;
    }

    /**
     * @return ScoreDTO Username TableColumn.
     */
    public static TableColumn<ScoreDTO, String> getUsernameColumn() {
        TableColumn<ScoreDTO, String> usernameCol = new TableColumn<>("Username");
        PropertyValueFactory<ScoreDTO, String> usernameCellValueFactory = new PropertyValueFactory<>("username");
        usernameCol.setMinWidth(100);
        usernameCol.setCellValueFactory(usernameCellValueFactory);
        return usernameCol;
    }

    /**
     * @return ScoreDTO Score TableColumn.
     */
    public static TableColumn<ScoreDTO, Integer> getScoreColumn() {
        TableColumn<ScoreDTO, Integer> scoreCol = new TableColumn<>("Score");
        PropertyValueFactory<ScoreDTO, Integer> scoreCellValueFactory = new PropertyValueFactory<>("score");
        scoreCol.setMinWidth(100);
        scoreCol.setCellValueFactory(scoreCellValueFactory);
        return scoreCol;
    }

    /**
     * @return ScoreDTO Created Date TableColumn.
     */
    public static TableColumn<ScoreDTO, String> getCreatedDateColumn() {
        TableColumn<ScoreDTO, String> createdDateCol = new TableColumn<>("Created Date");
        PropertyValueFactory<ScoreDTO, String> createdDateCellValueFactory = new PropertyValueFactory<>("createdDate");
        createdDateCol.setMinWidth(200);
        createdDateCol.setCellValueFactory(createdDateCellValueFactory);
        return createdDateCol;
    }
}
