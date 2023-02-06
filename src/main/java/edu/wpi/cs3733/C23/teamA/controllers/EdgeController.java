package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.hibernateDB.EdgeEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EdgeController extends ServiceRequestController {

  @FXML private TableView<EdgeEntity> dbTable;

  @FXML public TableColumn<EdgeEntity, String> startNodeCol;
  @FXML public TableColumn<EdgeEntity, String> endNodeCol;

  @FXML public MFXButton refresh;

  private Session session;
  private SessionFactory sf;
  private List<EdgeEntity> data;

  private ObservableList<EdgeEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    // Create configurations, session factory, and session
    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
    sf = configuration.buildSessionFactory();
    session = sf.openSession();

    reloadData();
    startNodeCol.setCellValueFactory(new PropertyValueFactory<>("node1"));
    endNodeCol.setCellValueFactory(new PropertyValueFactory<>("node2"));
    dbTable.setItems(dbTableRowsModel);
  }

  // Loads data from the database into the list
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = session.createQuery("from EdgeEntity ", EdgeEntity.class).getResultList();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (EdgeEntity edge : data) {
      dbTableRowsModel.add(edge);
    }
  }

  public void editableColumns() {
    //    startNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    //    endNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    //    startNodeCol.setOnEditCommit(
    //        e -> {
    //          EdgeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
    //          try {
    //            Transaction t = session.beginTransaction();
    //            n.setNode1(e.getNewValue());
    //            session.persist(n);
    //            t.commit();
    //          } catch (Exception ex) {
    //            refresh.setText("Invalid Node: Refresh");
    //          }
    //          reloadData();
    //        });
    //    endNodeCol.setOnEditCommit(
    //        e -> {
    //          EdgeEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
    //          try {
    //            Transaction t = session.beginTransaction();
    //            n.setNode2(e.getNewValue());
    //            session.persist(n);
    //            t.commit();
    //          } catch (Exception ex) {
    //            refresh.setText("Invalid Node: Refresh");
    //          }
    //          reloadData();
    //        });
    //  }

    //  public void switchToMoveScene(ActionEvent event) {
    //    Navigation.navigate(Screen.DATABASE);
    //  }
    //
    //  public void switchToNodeScene(ActionEvent event) {
    //    Navigation.navigate(Screen.NODE);
  }
}
