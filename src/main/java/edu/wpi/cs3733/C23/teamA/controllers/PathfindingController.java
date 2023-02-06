package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

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
  @FXML private Text pathDisplay; // to display the generated path
  @FXML private Canvas mapCanvas; // to display the generated path
  @FXML private ImageView mapImage;

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order

  // objects needed for the map
  private GraphicsContext gc;
  private final double SCALE_FACTOR = 0.05; // constant for map size/coordinate manipulation

  // the PathfindingSystem which runs methods from classes in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

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

  /**
   * Draws a visual representation of the path given in mapCanvas on top of mapImage.
   *
   * @param path The path of GraphNodes returned by callAStar
   */
  private void callMapDraw(ArrayList<GraphNode> path) {
    gc = mapCanvas.getGraphicsContext2D();
    pathfindingSystem.drawPath(gc, path, SCALE_FACTOR);
  }

  @FXML
  /**
   * Runs when the "Find Path" button is pressed, performing A* and displaying the path on the map.
   */
  public void generatePath() throws SQLException, RuntimeException {

    int startIndex = startNodeID.getSelectedIndex(); // from User Input
    int endIndex = endNodeID.getSelectedIndex(); // from User Input

    HashMap<String, GraphNode> hospitalL1 = null;

    if (startIndex == -1 || endIndex == -1) {
      reminder.setText("Please select an option from all fields in the form!");
      reminder.setVisible(true);
    } else {
      // create the graph hashMap where String is nodeId and GraphNode is the node
      hospitalL1 = prepGraphDB();
    }

    // get the IDs from the input combined w/ indexes
    String sName = allNodeIDs.get(startIndex);
    String eName = allNodeIDs.get(endIndex);

    // run A*
    ArrayList<GraphNode> path = callAStar(hospitalL1, sName, eName); // makes a call to AStar //

    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/unlabeledMaps/5% Scale/00_thelowerlevel1_unlabeled_5%.png"); // place the map on the page
    callMapDraw(path); // draw the path on top of the image

    // print the path to the textField (if needed)
    // pathDisplay.setText(PathInterpreter.generatePathString(path));
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

  /**
   * Method used for testing purposed to read in CSV file
   *
   * @return a HashMap<String, Node> representing a graph, read from .csv files
   * @throws IOException if the files cannot be read
   */
  public static HashMap<String, GraphNode> prepGraphCSV() throws IOException {
    // create a graph to hold the information
    HashMap<String, GraphNode> graph = new HashMap<>();

    // add the CSV information to the graph (TO BE UPDATED LATER)
    CSVReader.readNodes("src/main/resources/edu/wpi/cs3733/C23/teamA/mapCSV/nodes.csv", graph);
    CSVReader.readEdges("src/main/resources/edu/wpi/cs3733/C23/teamA/mapCSV/edges.csv", graph);

    return graph;
  }

  /**
   * Generates a graph to use with added Nodes and neighboring nodes added
   *
   * @return HashMap<String, GraphNode> graph where String is the nodeID and GraphNode is the node
   */
  public static HashMap<String, GraphNode> prepGraphDB() throws SQLException {
    // create a graph to hold the L1 information
    HashMap<String, GraphNode> graph = new HashMap<>();

    // Nodes
    ArrayList<Node> allNodes = Node.getAll(); // Gets list of all nodes from database's
    for (Node n : allNodes) {
      // create the graph and add the nodes (id, xcoord, ycoord, longName)
      GraphNode g =
          new GraphNode(
              n.getNodeID(), n.getXcoord(), n.getYcoord(), Move.mostRecentLoc(n.getNodeID()));
      graph.put(n.getNodeID(), g);
    }

    // Edges
    /* - read through edge columns and add edges to correct node (bidirectional) */
    ArrayList<Edge> allEdges = Edge.getAll(); // Gets list of all edges from database's edge table
    for (Edge e : allEdges) {
      GraphNode node1 = graph.get(e.getNode1());
      GraphNode node2 = graph.get(e.getNode2());
      node1.addNeighbor(node2);
      node2.addNeighbor(node1);
    }
    return graph;
  }

  /**
   * @param graph a HashMap<String, Node> (normally) generated by prepDFS() from reading .csv files
   * @param startNodeID the ID of the node where the user wants to start from
   * @param endNodeID the ID of the node that the user wants to get to
   * @return the path between startNode and endNode, if it exists. Otherwise, null.
   */
  public static ArrayList<GraphNode> callDFS(
      HashMap<String, GraphNode> graph, String startNodeID, String endNodeID) {
    return pathfindingSystem.traverseDFS(graph.get(startNodeID), graph.get(endNodeID));
  }

  /**
   * @param graph a HashMap<String, Node> (normally) generated by prepDFS() from reading .csv files
   * @param startNodeID the ID of the node where the user wants to start from
   * @param endNodeID the ID of the node that the user wants to get to
   * @return the path between startNode and endNode, if it exists. Otherwise, null.
   */
  public static ArrayList<GraphNode> callAStar(
      HashMap<String, GraphNode> graph, String startNodeID, String endNodeID) {
    return pathfindingSystem.traverseAStar(graph.get(startNodeID), graph.get(endNodeID));
  }

  /** Method to clear the fields on the form on the UI page */
  @FXML
  public void clearForm() {
    // clear the selection fields
    startNodeID.clear();
    endNodeID.clear();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
  }
}
