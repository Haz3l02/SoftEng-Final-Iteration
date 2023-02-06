package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.*;
import oracle.ucp.common.waitfreepool.Tuple;

/* This class has methods for pathfinding UI as well as methods to
create a graph using nodes from database and method to call AStar
to obtain and later print the path

Contains: input texts (startNodeID and endNodeID) and displayText (pathDisplay)
Contains Methods: - generatePath   - prepGraphDB     - callDFS
                  - prepGraphCSV   - callAStar       - clearForm
                  - initialize     - addFloorMapImage   -
 */
public class PathfindingController extends ServiceRequestController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startNodeID; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endNodeID; // field to enter endNode

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order

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
    List<Node> allNodes = Node.getAll(); // get all nodes from Database

    for (Node n : allNodes) {
      allNodeIDs.add(n.getNodeID()); // get nodeId
      allLongNames.add(Move.mostRecentLoc(n.getNodeID())); // get longName
    }

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

  public void goToMapScene(javafx.event.ActionEvent actionEvent) throws SQLException, IOException {
    FXMLLoader loader =
        new FXMLLoader(
            getClass()
                .getResource("resources/edu/wpi/cs3733/C23/teamA/views/PathfindingMapFXML.fxml"));
    // var resource = AApp.class.getResource("PathfindingMapFXML.fxml");

    System.out.println(loader.getLocation() == null);
    // store node data in a tuple
    Tuple<Integer, Integer> nodes =
        new Tuple<>(startNodeID.getSelectedIndex(), endNodeID.getSelectedIndex());

    PathfindingMapController controller = new PathfindingMapController();
    // Stage stage = new Stage();
    // stage.setScene(new Scene(loader.load()));
    // FXMLLoader loader = new FXMLLoader(resource);
    // loader.setController(controller);
    // loader.setLocation(resource);

    // Parent pane = (Parent) loader.load();
    Navigation.navigate(Screen.PATHFINDING_MAP);

    // System.out.println(loader.getController() == null);
    System.out.println(loader.getLocation() == null);

    controller = loader.getController();
    // AApp.getRootPane().setCenter(loader.load());

    // System.out.println(loader.getController() == null);
    System.out.println(loader.getLocation() == null);

    // NEED TO LOAD THE SCENE HERE

    System.out.println("before calling initData");
    controller.initData(nodes);
    System.out.println("after calling initData");

    // AApp.getRootPane().setCenter(loader.load());

    // stage.show();
    // Navigation.navigate(Screen.PATHFINDING_MAP);

    // return stage;
  }
}
