package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

  private ArrayList<Move> data;

  private ObservableList<Move> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    reloadData();
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    locNameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
    dbTable.setItems(dbTableRowsModel);
    editableColumns();
    dbTable.setEditable(true);
  }

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = Move.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (Move row : data) {
      row.getConnections();
      dbTableRowsModel.add(row);
    }
  }

  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nodeCol.setOnEditCommit(
        e -> {
          Move n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setNodeID(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Node: Refresh");
          }
          reloadData();
        });
    locNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    locNameCol.setOnEditCommit(
        e -> {
          Move n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setLongName(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Location: Refresh");
          }
          reloadData();
        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE);
  }
}
