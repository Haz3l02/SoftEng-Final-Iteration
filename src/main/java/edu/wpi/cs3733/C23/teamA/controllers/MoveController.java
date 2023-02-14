package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.persistence.PersistenceException;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MoveController extends MenuController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> locNameCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;

  @FXML public MFXComboBox<String> nodeBox;
  @FXML public MFXComboBox<String> locationBox;
  @FXML public DatePicker dateBox;
  @FXML public MFXButton submit;
  @FXML public Label warning;

  // List of all Node IDs in specific order
  private List<String> allNodeIDs;
  private List<String> allLongNames; // List of corresponding long names in order
  private List<MoveEntity> data;
  private List<NodeEntity> nodes;
  private MoveEntity row;
  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {

    reloadData();
    nodes = data.stream().map(moveEntity -> moveEntity.getNode()).toList();
    allNodeIDs = nodes.stream().map(nodeEntity -> nodeEntity.getNodeid()).toList();
    allLongNames =
        data.stream().map(moveEntity -> moveEntity.getLocationName().getLongname()).toList();

    ObservableList<String> nodes = FXCollections.observableArrayList(allNodeIDs);
    ObservableList<String> locationNames = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(nodes);
    locationBox.setItems(locationNames);

    nodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode().getNodeid()));
    locNameCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));

    dbTable.setItems(dbTableRowsModel);

    dbTable.getSortOrder().add(moveCol);
    dbTable.sort();
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      MoveImpl moveI = new MoveImpl();
      data = moveI.getAll();
      // moveI.closeSession();
      dbTableRowsModel.addAll(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    dbTable.sort();
  }

  public void submitEdit(ActionEvent event) {
    MoveImpl moveI = new MoveImpl();
    LocationNameImpl table = new LocationNameImpl();
    NodeImpl nodeI = new NodeImpl();
    if (!nodeBox.getText().trim().isBlank()
        || !locationBox.getText().trim().isBlank()
        || !dateBox.getValue().toString().isEmpty()) {
      MoveEntity newMove = new MoveEntity();
      newMove.setNode(nodeI.get(nodeBox.getText()));
      newMove.setLocationName(table.get(locationBox.getText()));
      newMove.setMovedate(dateBox.getValue());
      try {
        warning.setVisible(false);
        moveI.add(newMove);
        // moveI.closeSession();
      } catch (PersistenceException p) {
        warning.setVisible(true);
      }
    }
    // moveI.closeSession();
    // nodeI.closeSession();
    // table.closeSession();
    reloadData();
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToHomeDatabaseScene(ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }
}
