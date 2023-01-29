package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class DatabaseController {

  @FXML private TableView<Move> dbTable;

  @FXML public TableColumn<Move, String> nodeCol;
  @FXML public TableColumn<Move, String> locNameCol;
  @FXML public TableColumn<Move, String> moveCol;
  // @FXML public TableColumn<Move, String> edgeDataCol;

  private ArrayList<Move> data;

  {
    try {
      data = Move.getAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private ObservableList<Move> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() throws SQLException {
    for (Move row : data) {
      dbTableRowsModel.add(row);
    }
    initializeColumns();
  }

  public void initializeColumns() {
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    locNameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
    // edgeDataCol.setCellValueFactory(new PropertyValueFactory<>("edgeData"));
    dbTable.setItems(dbTableRowsModel);
    editableColumns();
  }

  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nodeCol.setOnEditCommit(
        e ->
            e.getTableView()
                .getItems()
                .get(e.getTablePosition().getRow())
                .setNodeID(e.getNewValue()));
    locNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    locNameCol.setOnEditCommit(
        e ->
            e.getTableView()
                .getItems()
                .get(e.getTablePosition().getRow())
                .setLongName(e.getNewValue()));
    moveCol.setCellFactory(TextFieldTableCell.forTableColumn());
    moveCol.setOnEditCommit(
        e ->
            e.getTableView()
                .getItems()
                .get(e.getTablePosition().getRow())
                .setMoveDate(e.getNewValue()));
    // edgeDataCol.setCellFactory(TextFieldTableCell.forTableColumn());
    dbTable.setEditable(true);
  }
}
