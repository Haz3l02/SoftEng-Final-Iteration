package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EdgeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NodeController extends MenuController {

  @FXML private TableView<NodeEntity> dbTable;

  @FXML public TableColumn<NodeEntity, String> nodeCol;
  @FXML public TableColumn<NodeEntity, Integer> xCol;
  @FXML public TableColumn<NodeEntity, Integer> yCol;
  @FXML public TableColumn<NodeEntity, String> floorCol;
  @FXML public TableColumn<NodeEntity, String> buildingCol;
  @FXML public TextField newx;

  @FXML public TextField newy;
  @FXML public MFXTextField xBox;
  @FXML public MFXTextField yBox;
  @FXML public MFXComboBox floorBox;
  @FXML public MFXComboBox buildingBox;
  @FXML public Button submit;

  public NodeEntity selected;
  private Session session;
  private List<NodeEntity> data;

  private ObservableList<NodeEntity> dbTableRowsModel = FXCollections.observableArrayList();

  /** runs on switching to this scene */
  public void initialize() {
    session = getSessionFactory().openSession();

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
      node.delete(selected);
      reloadData();
    }
  }

  public void onSubmit() {
    String x = xBox.getText().trim();
    String y = yBox.getText().trim();
    String floor = floorBox.getValue().toString();
    String building = buildingBox.getValue().toString();

    if (!x.isEmpty() && !y.isEmpty()) {
      Transaction t = session.beginTransaction();
      NodeEntity n = new NodeEntity();
      n.setNodeid("L1X" + x + "Y" + y);
      n.setXcoord(Integer.parseInt(x));
      n.setYcoord(Integer.parseInt(y));
      n.setFloor(floor);
      n.setBuilding(building);
      System.out.println(n.getNodeid());
      session.persist(n);
      t.commit();
      reloadData();
    }
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = getAllRecords(NodeEntity.class, session);
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
            Transaction t = session.beginTransaction();
            session
                .createMutationQuery(
                    "update NodeEntity set nodeid = '"
                        + e.getNewValue()
                        + "' where nodeid ='"
                        + n.getNodeid()
                        + "'")
                .executeUpdate();
            n = session.get(NodeEntity.class, e.getNewValue());
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    xCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            if (e.getNewValue() >= 0 && e.getNewValue() < 9999) {
              Transaction t = session.beginTransaction();
              n.setXcoord(e.getNewValue());
              session.persist(n);
              t.commit();
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
              Transaction t = session.beginTransaction();
              n.setYcoord(e.getNewValue());
              session.persist(n);
              t.commit();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    floorCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            Transaction t = session.beginTransaction();
            n.setFloor(e.getNewValue());
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    buildingCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            Transaction t = session.beginTransaction();
            n.setBuilding(e.getNewValue());
            session.persist(n);
            t.commit();
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
    session.close();
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToMoveScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.DATABASE);
  }

  public void switchToMapScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.NODE_MAP);
  }
}
