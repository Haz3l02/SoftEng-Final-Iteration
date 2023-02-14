package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.HomeDatabaseController.iecsv;
import static java.lang.String.valueOf;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.PersistenceException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

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
  @FXML protected StackPane reminderPaneMove;
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXTextField fileNameField;
  private PopOver popup;
  @FXML private MFXButton cancel;

  MoveImpl move = new MoveImpl();

  // List of all Node IDs in specific order
  private List<String> allNodeID;
  private List<String> allLongNames; // List of corresponding long names in order

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  MoveImpl moveImpl = new MoveImpl();
  List<MoveEntity> moveData = new ArrayList<>();

  /** runs on switching to this scene */
  public void initialize() {
    moveData = moveImpl.getAll();
    warning.setVisible(false);
    reminderPaneMove.setVisible(false);
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
    MoveEntity theMove = new MoveEntity(nodeimpl.get(nodeBox.getValue()), loc, dateBox.getValue());
    try {
      warning.setVisible(false);
      moveImpl.add(theMove);
    } catch (PersistenceException p) {
      warning.setVisible(true);
      reminderPaneMove.setVisible(true);
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

          NodeImpl nodeimpl = new NodeImpl();
          LocationNameImpl location = new LocationNameImpl();
          LocationNameEntity loc = location.get(locationBox.getValue());

          move.setNode(nodeimpl.get(nodeBox.getValue()));
          move.setLocationName(loc);
          move.setMovedate(dateBox.getValue());

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

  @FXML
  public void switchToImportPopup(ActionEvent event) throws IOException {

    if (!event.getSource().equals(cancel)) {
      FXMLLoader loader =
          new FXMLLoader(Main.class.getResource("views/ImportEmployeeCSVFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(cancel)) {
      popup.hide();
    }
  }

  @FXML
  public void importCSV(ActionEvent event) throws FileNotFoundException {
    if (fileNameField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      if (iecsv.getTableType().equals("move")) {
        move.importFromCSV(fileNameField.getText());
      }

      popup.hide();
    }
  }

  @FXML
  public void close(ActionEvent event) {
    popup.hide();
  }

  @FXML
  public void switchToExportPopup(ActionEvent event) throws IOException {

    if (!event.getSource().equals(cancel)) {
      FXMLLoader loader =
          new FXMLLoader(Main.class.getResource("views/ExportEmployeeCSVFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(cancel)) {
      popup.hide();
    }
  }

  @FXML
  public void exportCSV(ActionEvent event) throws IOException {

    if (fileNameField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      if (iecsv.getTableType().equals("move")) {
        move.exportToCSV(fileNameField.getText());
      }
    }

    popup.hide();
  }
}
