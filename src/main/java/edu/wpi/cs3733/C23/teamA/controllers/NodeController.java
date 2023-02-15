package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.HomeDatabaseController.iecsv;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EdgeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.PopOver;

public class NodeController extends MenuController {

  @FXML private TableView<NodeEntity> dbTable;

  @FXML public TableColumn<NodeEntity, String> nodeCol;
  @FXML public TableColumn<NodeEntity, Integer> xCol;
  @FXML public TableColumn<NodeEntity, Integer> yCol;
  @FXML public TableColumn<NodeEntity, String> floorCol;
  @FXML public TableColumn<NodeEntity, String> buildingCol;
  @FXML public MFXTextField xBox;
  @FXML public MFXTextField yBox;

  @FXML public Button submit;
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXTextField fileNameField;
  private PopOver popup;
  @FXML private MFXButton cancel;

  NodeImpl node = new NodeImpl();
  List<NodeEntity> nodeData = new ArrayList<>();
  @FXML public MFXComboBox floorBox;
  @FXML public MFXComboBox buildingBox;

  @FXML public MFXButton createNode;
  @FXML public MFXButton deleteButton;

  private NodeEntity selected = null;
  private List<NodeEntity> data;

  private NodeImpl nodeImpl = new NodeImpl();
  private EdgeImpl edgeImpl = new EdgeImpl();

  private ObservableList<NodeEntity> dbTableRowsModel = FXCollections.observableArrayList();

  /** runs on switching to this scene */
  public void initialize() {
    reloadData();

    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeid"));
    xCol.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
    yCol.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
    dbTable.setItems(dbTableRowsModel);

    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    yCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingCol.setCellFactory(TextFieldTableCell.forTableColumn());

    ObservableList<String> floors =
        FXCollections.observableArrayList(
            Arrays.stream(Floor.values()).map(floor -> floor.getTableString()).toList());
    ObservableList<String> buildings =
        FXCollections.observableArrayList(
            Arrays.stream(Building.values()).map(building -> building.getTableString()).toList());
    floorBox.setItems(floors);
    buildingBox.setItems(buildings);
  }

  public void rowClicked() {
    selected = dbTable.getSelectionModel().getSelectedItem();
    deleteButton.setDisable(false);
    if (selected != null) {
      xBox.setText(String.valueOf(selected.getXcoord()));
      yBox.setText(String.valueOf(selected.getYcoord()));
      floorBox.setText(selected.getFloor());
      buildingBox.setText(selected.getBuilding());
    }
  }

  public void delete() {
    if (selected != null) {
      edgeImpl.collapseNode(selected);
      nodeImpl.delete(selected.getNodeid());
      reloadData();
    }
  }

  public void onSubmit() {
    String x = xBox.getText().trim();
    String y = yBox.getText().trim();
    String floor = floorBox.getText().trim();
    String building = buildingBox.getText().trim();
    if (!x.isEmpty() && !y.isEmpty() && !floor.isEmpty() && !building.isEmpty()) {
      if (selected != null) {
        selected.setXcoord(Integer.parseInt(x));
        selected.setYcoord(Integer.parseInt(y));
        selected.setFloor(floor);
        selected.setBuilding(building);
        nodeImpl.update(selected.getNodeid(), selected);
      } else {
        NodeEntity newNode =
            new NodeEntity(
                floor + "X" + x + "Y" + y,
                Integer.parseInt(x),
                Integer.parseInt(y),
                floor,
                building);
        nodeImpl.add(newNode);
      }
    }
    reloadData();
  }

  public void clearEdits() {
    xBox.clear();
    yBox.clear();
    floorBox.clear();
    buildingBox.clear();
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    clearEdits();
    selected = null;
    deleteButton.setDisable(true);
    dbTableRowsModel.clear();
    try {
      data = nodeImpl.getAll();
      dbTableRowsModel.addAll(data);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Set cells to respond to modification and try to commit changes to the database */
  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    yCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nodeCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            String oldId = n.getNodeid();
            n.setNodeid(e.getNewValue());
            NodeImpl nodeI = new NodeImpl();
            nodeI.update(oldId, n);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    xCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            if (e.getNewValue() >= 0 && e.getNewValue() < 9999) {
              n.setXcoord(e.getNewValue());
              NodeImpl nodeI = new NodeImpl();
              nodeI.update(n.getNodeid(), n);
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    yCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            if (e.getNewValue() >= 0 && e.getNewValue() < 9999) {
              n.setYcoord(e.getNewValue());
              NodeImpl nodeI = new NodeImpl();
              nodeI.update(n.getNodeid(), n);
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    floorCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setFloor(e.getNewValue());
            NodeImpl nodeI = new NodeImpl();
            nodeI.update(n.getNodeid(), n);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    buildingCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            n.setFloor(e.getNewValue());
            NodeImpl nodeI = new NodeImpl();
            nodeI.update(n.getNodeid(), n);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
  }

  public void addTableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    xCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    yCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingCol.setCellFactory(TextFieldTableCell.forTableColumn());
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToMoveScene(ActionEvent event) {
    Navigation.navigate(Screen.MOVE);
  }

  public void switchToMapScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  void clearForm(ActionEvent event) {
    fileNameField.clear();
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
      if (iecsv.getTableType().equals("node")) {
        node.importFromCSV(fileNameField.getText());
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
      if (iecsv.getTableType().equals("node")) {
        node.exportToCSV(fileNameField.getText());
      }
    }

    popup.hide();
  }
}
