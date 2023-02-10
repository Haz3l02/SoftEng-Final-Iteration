package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
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
import javafx.scene.text.Text;
import oracle.ucp.common.waitfreepool.Tuple;
import org.hibernate.Session;

public class PathfindingController extends ServiceRequestController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startNodeID; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endNodeID; // field to enter endNode
  @FXML private Text errorMessage;

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order
  private List<NodeEntity> allNodes;

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
    allNodes = getAllRecords(NodeEntity.class, session); // get all nodes from Database

    for (NodeEntity n : allNodes) {
      allNodeIDs.add(n.getNodeid()); // get nodeId
      allLongNames.add(MoveEntity.mostRecentLoc(n.getNodeid(), session)); // get longName
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
    errorMessage.setText("");

    // clear the canvas w/ the drawn path; does NOT hide the map image
    // gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
  }

  public void goToMapScene(javafx.event.ActionEvent actionEvent) {
    Tuple<Integer, Integer> nodeIndices =
        new Tuple<>(startNodeID.getSelectedIndex(), endNodeID.getSelectedIndex());

    if (nodeIndices.get1() == -1 || nodeIndices.get2() == -1) {
      errorMessage.setText("Have not selected a location in both drop downs");
    } else {
      errorMessage.setText("");
      NodeIndicesHolder.getInstance().setNodes(nodeIndices);
      Navigation.navigate(Screen.PATHFINDING_MAP);
    }
  }
}
