package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.MoveEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseController extends ServiceRequestController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> locNameCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;

  @FXML public MFXButton refresh;

  private List<MoveEntity> data;
  private Session session;
  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    session = getSessionFactory().openSession();

    reloadData();

    nodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode().getNodeid()));
    locNameCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));
    dbTable.setItems(dbTableRowsModel);

    editableColumns();
    dbTable.setEditable(true);
  }

  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = getAllRecords(MoveEntity.class, session);
      dbTableRowsModel.addAll(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Set cells to respond to modification and try to commit changes to the database */
  public void editableColumns() {
    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nodeCol.setOnEditCommit(
        e -> {
          NodeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow()).getNode();
          try {
            Transaction t = session.beginTransaction();
            n.setNodeid(e.getNewValue());
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            ex.printStackTrace();
            refresh.setText("Invalid Node: Refresh");
          }
        });
    locNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    locNameCol.setOnEditCommit(
        e -> {
          LocationNameEntity n =
              e.getTableView().getItems().get(e.getTablePosition().getRow()).getLocationName();
          try {
            Transaction t = session.beginTransaction();
            n.setLongname(e.getNewValue());
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            refresh.setText("Invalid Location: Refresh");
          }
        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.NODE);
  }
}
