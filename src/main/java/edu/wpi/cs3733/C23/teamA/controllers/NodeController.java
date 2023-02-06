package edu.wpi.cs3733.C23.teamA.controllers;

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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class NodeController extends ServiceRequestController {

  @FXML private TableView<NodeEntity> dbTable;

  @FXML public TableColumn<NodeEntity, String> nodeCol;
  @FXML public TableColumn<NodeEntity, Integer> xCol;
  @FXML public TableColumn<NodeEntity, Integer> yCol;
  @FXML public TableColumn<NodeEntity, String> floorCol;
  @FXML public TableColumn<NodeEntity, String> buildingCol;

  @FXML public MFXButton refresh;

  private Session session;
  private SessionFactory sf;
  private List<NodeEntity> data;

  private ObservableList<NodeEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
    sf = configuration.buildSessionFactory();
    session = sf.openSession();

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

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = session.createQuery("from NodeEntity ", NodeEntity.class).getResultList();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    for (NodeEntity node : data) {
      dbTableRowsModel.add(node);
    }
  }

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
          reloadData();
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
          reloadData();
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
          reloadData();
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
          reloadData();
        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToMoveScene(ActionEvent event) {
    Navigation.navigate(Screen.DATABASE);
  }
}
