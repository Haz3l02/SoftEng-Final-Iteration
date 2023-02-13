package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
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

public class EdgeController extends MenuController {

  @FXML private TableView<EdgeEntity> dbTable;

  @FXML public TableColumn<EdgeEntity, String> edgeIDCol;
  @FXML public TableColumn<EdgeEntity, String> startNodeCol;
  @FXML public TableColumn<EdgeEntity, String> endNodeCol;

  @FXML public MFXButton refresh;

  private Session session;
  private List<EdgeEntity> data;

  private ObservableList<EdgeEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    session = getSessionFactory().openSession();

    reloadData();

    edgeIDCol.setCellValueFactory(new PropertyValueFactory<>("edgeid"));
    startNodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode1().getNodeid()));
    endNodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode2().getNodeid()));
    dbTable.setItems(dbTableRowsModel);

    editableColumns();
    dbTable.setEditable(true);
  }

  // Loads data from the database into the list
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = getAllRecords(EdgeEntity.class, session);
      dbTableRowsModel.addAll(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void editableColumns() {
    startNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    endNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    startNodeCol.setOnEditCommit(
        e -> {
          EdgeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            Transaction t = session.beginTransaction();
            n.setNode1(session.get(NodeEntity.class, e.getNewValue()));
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            refresh.setText("Invalid Node: Refresh");
          }
        });
    endNodeCol.setOnEditCommit(
        e -> {
          EdgeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
          try {
            Transaction t = session.beginTransaction();
            n.setNode2(session.get(NodeEntity.class, e.getNewValue()));
            session.persist(n);
            t.commit();
          } catch (Exception ex) {
            refresh.setText("Invalid Node: Refresh");
          }
        });
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
