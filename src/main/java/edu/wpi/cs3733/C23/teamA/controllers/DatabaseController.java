package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseController extends MenuController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> locNameCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;

  @FXML public MFXComboBox<String> nodeBox;
  @FXML public MFXComboBox<String> locationBox;
  @FXML public DatePicker dateBox;
  @FXML public MFXButton submit;

  // List of all Node IDs in specific order
  private List<String> allNodeIDs;
  private List<String> allLongNames; // List of corresponding long names in order
  private List<MoveEntity> data;
  private List<NodeEntity> nodes;
  private Session session;
  private MoveEntity row;
  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  /** runs on switching to this scene */
  public void initialize() {
    session = getSessionFactory().openSession();

    nodes = getAllRecords(NodeEntity.class, session); // get all nodes from Database
    allNodeIDs = new ArrayList<>();
    allLongNames = new ArrayList<>();

    for (NodeEntity n : nodes) {
      allNodeIDs.add(n.getNodeid()); // get nodeId
      allLongNames.add(MoveEntity.mostRecentLoc(n.getNodeid(), session)); // get longName
    }

    ObservableList<String> nodes = FXCollections.observableArrayList(allNodeIDs);
    ObservableList<String> locationNames = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(nodes);
    locationBox.setItems(locationNames);

    reloadData();

    nodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode().getNodeid()));
    locNameCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    moveCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));

    dbTable.setItems(dbTableRowsModel);

    dbTable.getSortOrder().add(moveCol);
    dbTable.sort();
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
    dbTable.sort();
  }

  public void submitEdit(ActionEvent event) {
    if (!nodeBox.getText().trim().isBlank()
        || !locationBox.getText().trim().isBlank()
        || !dateBox.getValue().toString().isEmpty()) {
      Transaction t = session.beginTransaction();
      MoveEntity newMove = new MoveEntity();
      newMove.setNode(session.get(NodeEntity.class, nodeBox.getText()));
      newMove.setLocationName(session.get(LocationNameEntity.class, locationBox.getText()));
      newMove.setMovedate(Timestamp.valueOf(dateBox.getValue().atStartOfDay()));
      session.persist(newMove);
      t.commit();
    }
    reloadData();
  }

  public void switchToEdgeScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.NODE);
  }

  public void switchToMapScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.NODE_MAP);
  }

  public void switchToMoveScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.DATABASE);
  }

  public void switchToHomeDatabaseScene(ActionEvent event) {
    session.close();
    Navigation.navigate(Screen.HOME_DATABASE);
  }

  public void submit(ActionEvent actionEvent) {}
}
