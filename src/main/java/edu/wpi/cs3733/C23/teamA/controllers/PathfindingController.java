package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.NodeIndicesHolder;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import oracle.ucp.common.waitfreepool.Tuple;
import org.hibernate.Session;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

public class PathfindingController extends ServiceRequestController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startNodeID; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endNodeID; // field to enter endNode

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order

  // objects needed for the maps
  private GraphicsContext gc;

  /**
   * Runs when the pathfinding page is opened, grabbing nodes from the database and anything else
   * that needs to exist in the page before pathfinding is called.
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException {

    // Database's list of longNames
    allNodeIDs = new ArrayList<String>();
    allLongNames = new ArrayList<String>();

    Session session = getSessionFactory().openSession();
    List<NodeEntity> allNodes = getAllRecords(NodeEntity.class, session); // get all nodes from Database

    for (NodeEntity n : allNodes) {
      allNodeIDs.add(n.getNodeid()); // get nodeId
      allLongNames.add(Move.mostRecentLoc(n.getNodeid())); // get longName
    }
    session.close();

    // Add to front end
    ObservableList<String> locations = FXCollections.observableArrayList(allLongNames);
    // populates the dropdown boxes
    startNodeID.setItems(locations);
    endNodeID.setItems(locations);
  }

  /** Method to clear the fields on the form on the UI page */
  @FXML
  public void clearForm() {
    // clear the selection fields
    startNodeID.clear();
    endNodeID.clear();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    // gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
  }

  public void goToMapScene(javafx.event.ActionEvent actionEvent) {
    Tuple<Integer, Integer> nodes =
        new Tuple<>(startNodeID.getSelectedIndex(), endNodeID.getSelectedIndex());

    NodeIndicesHolder.getInstance().setNodes(nodes);

    Navigation.navigate(Screen.PATHFINDING_MAP);
  }
}
