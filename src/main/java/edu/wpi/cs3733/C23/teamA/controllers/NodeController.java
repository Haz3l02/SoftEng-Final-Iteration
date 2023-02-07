package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NodeController extends ServiceRequestController {

  @FXML private TableView<NodeEntity> dbTable;

  @FXML public TableColumn<NodeEntity, String> nodeCol;
  @FXML public TableColumn<NodeEntity, Integer> xCol;
  @FXML public TableColumn<NodeEntity, Integer> yCol;
  @FXML public TableColumn<NodeEntity, String> floorCol;
  @FXML public TableColumn<NodeEntity, String> buildingCol;
  @FXML public MFXButton delete;
  @FXML public MFXButton view;

  @FXML public MFXButton refresh;

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
      Transaction t = session.beginTransaction();
      session.remove(selected);
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
            refresh.setText("Invalid Input");
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
            refresh.setText("Invalid Input");
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
            refresh.setText("Invalid Input");
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
            refresh.setText("Invalid Input");
            ex.printStackTrace();
          }
        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToMoveScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.DATABASE);
  }
}
