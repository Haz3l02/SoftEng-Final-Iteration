package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.hibernateDB.MoveEntity;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseController extends ServiceRequestController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> locNameCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;

  @FXML public MFXButton refresh;

  private List<MoveEntity> data;
  private Session session;
  private SessionFactory sf;
  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
    sf = configuration.buildSessionFactory();
    session = sf.openSession();

    reloadData();
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeid"));
    locNameCol.setCellValueFactory(new PropertyValueFactory<>("longname"));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));
    dbTable.setItems(dbTableRowsModel);
    editableColumns();
    dbTable.setEditable(true);
  }

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      data = session.createQuery("from MoveEntity ", MoveEntity.class).getResultList();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (MoveEntity move : data) {
      dbTableRowsModel.add(move);
    }
  }

  public void editableColumns() {
    //    nodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    //    nodeCol.setOnEditCommit(
    //        e -> {
    //          MoveEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
    //          try {
    //            Transaction t = session.beginTransaction();
    //            n.setNodeid(e.getNewValue());
    //            session.persist(n);
    //            t.commit();
    //            reloadData();
    //          } catch (Exception ex) {
    //            refresh.setText("Invalid Node: Refresh");
    //          }
    //        });
    //    locNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    //    locNameCol.setOnEditCommit(
    //        e -> {
    //          MoveEntity n = e.getTableView().getItems().get(e.getTablePosition().getRow());
    //          try {
    //            Transaction t = session.beginTransaction();
    //            n.setLongname(e.getNewValue());
    //            session.persist(n);
    //            t.commit();
    //          } catch (Exception ex) {
    //            refresh.setText("Invalid Location: Refresh");
    //          }
    //          reloadData();
    //        });
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE);
  }
}
