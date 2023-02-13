package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import static java.lang.String.valueOf;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.persistence.PersistenceException;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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
  @FXML public TableColumn<MoveEntity, String> moveCol;
  @FXML public TableColumn<MoveEntity, String> moveDateCol;

  @FXML public MFXFilterComboBox<String> nodeBox;
  @FXML public MFXFilterComboBox<String> locationBox;
  @FXML public DatePicker dateBox;
  @FXML public MFXButton submit;
  @FXML public Label warning;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton createMove;

  // List of all Node IDs in specific order
  private List<String> allNodeID;
  private List<String> allLongNames; // List of corresponding long names in order

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  MoveImpl moveImpl = new MoveImpl();
  List<MoveEntity> moveData = new ArrayList<>();

  /** runs on switching to this scene */
  public void initialize() {
    moveData = moveImpl.getAll();

    allNodeID = moveImpl.getNodeID();
    allLongNames = moveImpl.getLocationName();

    ObservableList<String> nodes = FXCollections.observableArrayList(allNodeID);
    ObservableList<String> locationNames = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(nodes);
    locationBox.setItems(locationNames);

    nodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode().getNodeid()));
    moveCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    moveDateCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));

    dbTableRowsModel.addAll(moveData);
    dbTable.setItems(dbTableRowsModel);
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      moveData = moveImpl.getAll();
      dbTableRowsModel.addAll(moveData);
      moveI.closeSession();

      clearEdits();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void delete(ActionEvent event) {
    List<String> moveIDs = new ArrayList<>();
    moveIDs.add(nodeBox.getValue());
    moveIDs.add(locationBox.getValue());
    moveIDs.add(dateBox.getValue().toString());
    moveImpl.delete(moveIDs);
    reloadData();
  }

  @FXML
  public void createMove(ActionEvent event) {
    NodeImpl nodeimpl = new NodeImpl();
    LocationNameImpl location = new LocationNameImpl();
    LocationNameEntity loc = location.get(locationBox.getValue());
    long bill = 90;
    Timestamp theTime = new Timestamp(bill);

    MoveEntity theMove = new MoveEntity(nodeimpl.get(nodeBox.getValue()), loc, theTime);
    try {
      warning.setVisible(false);
      moveI.add(newMove);
      moveI.closeSession();
    } catch (PersistenceException p) {
      warning.setVisible(true);
    }
    moveImpl.add(theMove);
    reloadData();
  }

  public void submitEdit(ActionEvent event) {
    if (!nodeBox.getText().trim().isBlank()
        || !locationBox.getText().trim().isBlank()
        || !dateBox.getValue().toString().isEmpty()) {

      ObservableList<MoveEntity> currentTableData = dbTable.getItems();

      String moveDate = dateBox.getValue().toString();
      String nodeID = nodeBox.getValue();
      String theLocation = locationBox.getValue();

      for (MoveEntity move : currentTableData) {
        if (move.getMovedate().toString().equals(moveDate)
            && move.getLocationName().getLongname().equals(theLocation)
            && move.getNode().getNodeid().equals(nodeID)) {
          List<String> moveID = new ArrayList<>();
          moveID.add(nodeID);
          moveID.add(theLocation);
          moveID.add(moveDate);

          NodeImpl nodeimpl = new NodeImpl();
          LocationNameImpl location = new LocationNameImpl();
          LocationNameEntity loc = location.get(locationBox.getValue());

          move.setNode(nodeimpl.get(nodeBox.getValue()));
          move.setLocationName(loc);
          long bill = 90;
          Timestamp theTime = new Timestamp(bill);
          move.setMovedate(theTime);

          moveImpl.update(moveID, move);
          dbTable.setItems(currentTableData);
          reloadData();
          break;
        }
      }
    }
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE);
  }

  public void switchToHomeDatabaseScene(ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }

  public void clearEdits() {
    nameBox.clear();
    locationBox.clear();

    //        createEmployee.setVisible(true);
    //        editButton.setDisable(true);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

    MoveEntity clickedMoveTableRow = dbTable.getSelectionModel().getSelectedItem();
    // Time date;
    LocalDate newDate = null;
    // newDate.atStartOfDay();
    // Time time = new Time(2, 1, 1);

    if (clickedMoveTableRow != null) {
      nodeBox.setText(valueOf(clickedMoveTableRow.getNode().getNodeid()));
      locationBox.setText(valueOf(clickedMoveTableRow.getLocationName().getLongname()));
      dateBox.setValue(newDate);
      editButton.setDisable(false);
      deleteButton.setDisable(false);
      createMove.setVisible(false);
    }

    ObservableList<String> node = FXCollections.observableArrayList(allNodeID);
    ObservableList<String> location = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(node);
    locationBox.setItems(location);
  }
}
