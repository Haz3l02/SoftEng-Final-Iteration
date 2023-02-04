package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Edge;
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

public class EdgeController extends ServiceRequestController {

  @FXML private TableView<Edge> dbTable;

  @FXML public TableColumn<Edge, String> startNodeCol;
  @FXML public TableColumn<Edge, String> endNodeCol;

  @FXML public MFXButton refresh;

  private ArrayList<Edge> data;

  private ObservableList<Edge> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    {
      try {
        data = Edge.getAll();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    for (Edge row : data) {
      dbTableRowsModel.add(row);
    }
    startNodeCol.setCellValueFactory(new PropertyValueFactory<>("node1"));
    endNodeCol.setCellValueFactory(new PropertyValueFactory<>("node2"));
    dbTable.setItems(dbTableRowsModel);
  }

  public void switchToMoveScene(ActionEvent event) {
    Navigation.navigate(Screen.DATABASE);
  }

  public void switchToNodeScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE);
  }
}
