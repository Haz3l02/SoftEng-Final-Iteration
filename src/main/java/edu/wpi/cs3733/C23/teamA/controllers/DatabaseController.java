package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class DatabaseController extends ServiceRequestController {

  @FXML private TableView<Move> dbTable;

  @FXML public TableColumn<Move, String> nodeCol;
  @FXML public TableColumn<Move, String> locNameCol;
  @FXML public TableColumn<Move, String> moveCol;

  @FXML public MFXButton refresh;
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
  public void initialize() {

    for (Move row : data) {
      dbTableRowsModel.add(row);
    }
    initializeColumns();
    editableColumns();
    dbTable.setEditable(true);
  }

  public void initializeColumns() {
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    locNameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
    // edgeDataCol.setCellValueFactory(new PropertyValueFactory<>("edgeData"));
    dbTable.setItems(dbTableRowsModel);
  }

  public void ReLoadPage() {}

  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    locNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    locNameCol.setOnEditCommit(
        e -> {
          Move n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setLongName(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Node: Refresh");
            n.setLongName(e.getOldValue());
            initializeColumns();
          }
        });
    nodeCol.setOnEditCommit(
        e -> {
          Move n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setNodeID(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Location: Refresh");
            n.setNodeID(e.getOldValue());
            initializeColumns();
          }
        });
    // edgeDataCol.setCellFactory(TextFieldTableCell.forTableColumn());
  }
}
