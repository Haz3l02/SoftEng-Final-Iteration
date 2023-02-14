package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.HomeDatabaseController.iecsv;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EdgeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
  @FXML public TextField newx;
  @FXML public TextField newy;

  @FXML public Button submit;
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXTextField fileNameField;
  private PopOver popup;
  @FXML private MFXButton cancel;

  NodeImpl node = new NodeImpl();
  List<NodeEntity> nodeData = new ArrayList<>();

  private NodeEntity selected;
  private List<NodeEntity> data;

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

    editableColumns();
    dbTable.setEditable(true);
  }

  public void rowClicked() {
    selected = dbTable.getSelectionModel().getSelectedItem();
  }

  public void delete() {
    if (selected != null) {
      EdgeImpl edge = new EdgeImpl();
      NodeImpl node = new NodeImpl();
      edge.collapseNode(selected);
      node.delete(selected.getNodeid());

      reloadData();
    }
  }

  public void onSubmit() {
    String x = newx.getText().trim();
    String y = newy.getText().trim();
    if (!x.isEmpty() && !y.isEmpty()) {
      NodeEntity n = new NodeEntity();
      n.setNodeid("L1X" + x + "Y" + y);
      n.setXcoord(Integer.parseInt(x));
      n.setYcoord(Integer.parseInt(y));
      n.setFloor("L1");
      n.setBuilding("BTM");
      NodeImpl nodeI = new NodeImpl();
      nodeI.add(n);
      reloadData();
    }
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      NodeImpl nodeI = new NodeImpl();
      data = nodeI.getAll();
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
