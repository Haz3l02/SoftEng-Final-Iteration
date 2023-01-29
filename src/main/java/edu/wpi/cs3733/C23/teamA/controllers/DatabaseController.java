package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.DBTableRow;
import edu.wpi.cs3733.C23.teamA.databases.Move;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DatabaseController {

  @FXML private TableView<DBTableRow> dbTable;

  @FXML public TableColumn<DBTableRow, String> nodeCol;
  @FXML public TableColumn<DBTableRow, String> locNameCol;
  @FXML public TableColumn<DBTableRow, String> moveCol;
  @FXML public TableColumn<DBTableRow, String> edgeDataCol;

  private ArrayList<Move> data;

  {
    try {
      data = Move.getAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private ObservableList<DBTableRow> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    for (Move row : data) {
      dbTableRowsModel.add(
          new DBTableRow(row.getNodeID(), row.getLongName(), row.getMoveDate(), "goes to"));
    }
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("node"));
    locNameCol.setCellValueFactory(new PropertyValueFactory<>("locName"));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("move"));
    edgeDataCol.setCellValueFactory(new PropertyValueFactory<>("edgeData"));

    dbTable.setItems(dbTableRowsModel);
  }
}
