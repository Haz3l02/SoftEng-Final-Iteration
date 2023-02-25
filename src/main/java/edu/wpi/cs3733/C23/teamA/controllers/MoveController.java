package edu.wpi.cs3733.C23.teamA.controllers;

import static java.lang.String.valueOf;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import jakarta.persistence.PersistenceException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MoveController extends MenuController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;
  @FXML public TableColumn<MoveEntity, String> moveDateCol;

  @FXML public MFXFilterComboBox<String> nodeBox;
  @FXML public MFXFilterComboBox<String> locationBox;
  @FXML public MFXDatePicker dateBox;
  @FXML public MFXButton submit;
  // @FXML public Label warning;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton createMove;
  @FXML protected Text warning;

  // List of all Node IDs in specific order
  private List<String> allNodeID;
  private List<String> allLongNames; // List of corresponding long names in order

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  List<MoveEntity> moveData = new ArrayList<>();

  /** runs on switching to this scene */
  public void initialize() {
    moveData = FacadeRepository.getInstance().getAllMove();
    warning.setVisible(false);

    allNodeID =
        FacadeRepository.getInstance().getAllMove().stream()
            .map(moveEntity -> moveEntity.getNode().getNodeid())
            .toList();
    allLongNames =
        FacadeRepository.getInstance().getAllMove().stream()
            .map(moveEntity -> moveEntity.getLocationName().getLongname())
            .toList();

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
      moveData = FacadeRepository.getInstance().getAllMove();
      dbTableRowsModel.addAll(moveData);

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
    FacadeRepository.getInstance().deleteMove(moveIDs);
    reloadData();
  }

  @FXML
  public void createMove(ActionEvent event) {
    LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
    MoveEntity theMove =
        new MoveEntity(
            FacadeRepository.getInstance().getNode(nodeBox.getValue()), loc, dateBox.getValue());
    try {
      warning.setVisible(false);
      FacadeRepository.getInstance().addMove(theMove);
    } catch (PersistenceException p) {
      warning.setVisible(true);
    }
    // moveImpl.add(theMove);
    reloadData();
  }

  public void submitEdit(ActionEvent event) {
    if (!nodeBox.getText().trim().isBlank()
        || !locationBox.getText().trim().isBlank()
        || !dateBox.getValue().toString().isEmpty()) {

      ObservableList<MoveEntity> currentTableData = dbTable.getItems();

      LocalDate moveDate = dateBox.getValue();
      String nodeID = nodeBox.getValue();
      String theLocation = locationBox.getValue();
      String submitDate = dateBox.getText().toString();

      for (MoveEntity move : currentTableData) {
        if (move.getMovedate().equals(moveDate)
            && move.getLocationName().getLongname().equals(theLocation)
            && move.getNode().getNodeid().equals(nodeID)) {
          List<String> moveID = new ArrayList<>();
          moveID.add(nodeID);
          moveID.add(theLocation);
          moveID.add(submitDate);

          LocationNameEntity loc =
              FacadeRepository.getInstance().getLocation(locationBox.getValue());

          move.setNode(FacadeRepository.getInstance().getNode(nodeBox.getValue()));
          move.setLocationName(loc);
          move.setMovedate(dateBox.getValue());

          FacadeRepository.getInstance().addMove(move);
          System.out.println("Updateing Node");
          dbTable.setItems(currentTableData);
          reloadData();
          break;
        }
      }
    }
  }

  @FXML
  public void switchToImport(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.IMPORT_CSV);
  }

  public void switchToExport(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.EXPORT_CSV);
  }

  public void clearEdits() {
    nodeBox.clear();
    locationBox.clear();
    dateBox.setValue(dateBox.getCurrentDate());

    //        createEmployee.setVisible(true);
    //        editButton.setDisable(true);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

    MoveEntity clickedMoveTableRow = dbTable.getSelectionModel().getSelectedItem();

    if (clickedMoveTableRow != null) {
      nodeBox.setText(valueOf(clickedMoveTableRow.getNode().getNodeid()));
      locationBox.setText(valueOf(clickedMoveTableRow.getLocationName().getLongname()));
      dateBox.setValue(clickedMoveTableRow.getMovedate());

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
