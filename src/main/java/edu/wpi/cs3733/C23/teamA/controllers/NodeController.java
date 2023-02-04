package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Node;
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
import javafx.util.converter.IntegerStringConverter;

public class NodeController extends ServiceRequestController {

  @FXML private TableView<Node> dbTable;

  @FXML public TableColumn<Node, String> nodeCol;
  @FXML public TableColumn<Node, Integer> xCol;
  @FXML public TableColumn<Node, Integer> yCol;
  @FXML public TableColumn<Node, String> floorCol;
  @FXML public TableColumn<Node, String> buildingCol;

  @FXML public MFXButton refresh;

  private ArrayList<Node> data;

  private ObservableList<Node> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    try {
      data = Node.getAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    for (Node row : data) {
      dbTableRowsModel.add(row);
    }
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xCol.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
    yCol.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
    dbTable.setItems(dbTableRowsModel);
    editableColumns();
    dbTable.setEditable(true);
  }

  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    yCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCol.setOnEditCommit(
        e -> {
          Node n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            if (e.getNewValue() >= 0 && e.getNewValue() < 9999) {
              n.setXcoord(e.getNewValue());
              n.update();
            }
          } catch (SQLException ex) {
            refresh.setText("Invalid Input");
            ex.printStackTrace();
          }
          dbTable.refresh();
        });
    yCol.setOnEditCommit(
        e -> {
          Node n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            if (e.getNewValue() >= 0 && e.getNewValue() < 9999) {
              n.setYcoord(e.getNewValue());
              n.update();
            }
          } catch (SQLException ex) {
            refresh.setText("Invalid Input");
            ex.printStackTrace();
          }
          dbTable.refresh();
        });
    floorCol.setOnEditCommit(
        e -> {
          Node n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setFloor(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Input");
            ex.printStackTrace();
          }
          dbTable.refresh();
        });
    buildingCol.setOnEditCommit(
        e -> {
          Node n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setBuilding(e.getNewValue());
            n.update();
          } catch (SQLException ex) {
            refresh.setText("Invalid Input");
            ex.printStackTrace();
          }
          dbTable.refresh();
        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToMoveScene(ActionEvent event) {
    Navigation.navigate(Screen.DATABASE);
  }
}
