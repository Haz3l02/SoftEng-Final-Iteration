package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import edu.wpi.cs3733.C23.teamA.serviceRequests.NodeIndicesHolder;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import oracle.ucp.common.waitfreepool.Tuple;

/* This class has methods for pathfinding UI as well as methods to
create a graph using nodes from database and method to call AStar
to obtain and later print the path
 */

public class PathfindingController extends ServiceRequestController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startNodeID; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endNodeID; // field to enter endNode

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order

  // objects needed for the maps
  private GraphicsContext gc;
  private final double SCALE_FACTOR = 0.065; // constant for map size/coordinate manipulation

  // the PathfindingSystem which runs methods from classes in the pathfinding package
  private PathfindingSystem pathfindingSystem;

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

    // initialize the pathfindingSystem
    pathfindingSystem = new PathfindingSystem();

    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/unlabeledMaps/25% Scale/00_thelowerlevel1_unlabeled_25%.png"); // place the map on the page
  }

  /**
   * Draws a visual representation of the path given in mapCanvas on top of mapImage.
   *
   * @param path The path of GraphNodes returned by callAStar
   */
  private void callMapDraw(ArrayList<GraphNode> path) {
    gc = mapCanvas.getGraphicsContext2D();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

    pathfindingSystem.drawPath(gc, path, SCALE_FACTOR);
  }

  @FXML
  /**
   * Runs when the "Find Path" button is pressed, performing A* and displaying the path on the map.
   */
  public void generatePath() throws SQLException, RuntimeException {

    int startIndex = startNodeID.getSelectedIndex(); // from User Input
    int endIndex = endNodeID.getSelectedIndex(); // from User Input

    if (startIndex == -1 || endIndex == -1) {
      reminder.setText("Please select an option from all fields in the form!");
      reminder.setVisible(true);
    } else {
      // create the graph hashMap where String is nodeId and GraphNode is the node
      pathfindingSystem.prepGraphDB();
    }

    // get the IDs from the input combined w/ indexes
    String sName = allNodeIDs.get(startIndex);
    String eName = allNodeIDs.get(endIndex);

    // run A*
    GraphNode start = pathfindingSystem.getNode(sName);
    GraphNode end = pathfindingSystem.getNode(eName);
    ArrayList<GraphNode> path =
        pathfindingSystem.traverseAStar(start, end); // makes a call to AStar

    callMapDraw(path); // draw the path on top of the image

    // print the path to the textField (if needed)
    // pathDisplay.setText(pathfindingSystem.generatePathString(path));
  }

  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param pathName the path to the image to be added
   */
  private void addFloorMapImage(String pathName) {
    File file = new File(pathName);
    Image image = new Image(file.toURI().toString());
    mapImage.setImage(image);
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
