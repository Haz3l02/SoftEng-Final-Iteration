package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import oracle.ucp.common.waitfreepool.Tuple;

/* This class has methods for pathfinding UI as well as methods to
create a graph using nodes from database and method to call AStar
to obtain and later print the path

Contains: input texts (startNodeID and endNodeID) and displayText (pathDisplay)
Contains Methods: - generatePath   - prepGraphDB     - callDFS
                  - prepGraphCSV   - callAStar       - clearForm
                  - initialize     - addFloorMapImage   -
 */
public class PathfindingMapController extends ServiceRequestController {

  // javaFX items
  @FXML private Text pathDisplay; // to display the generated path
  @FXML private Canvas mapCanvas; // to display the generated path
  @FXML private ImageView mapImage;

  // Lists of Nodes and Node Data

  private GraphicsContext gc;
  private List<String> allNodeIDs; // List of all Node IDs in specific order

  private static Tuple<Integer, Integer> mapNodes;

  /**
   * Runs when the pathfinding page is opened, grabbing nodes from the database and anything else
   * that needs to exist in the page before pathfinding is called.
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException {
    System.out.println(mapNodes.get1() + ", " + mapNodes.get2() + " initializing start");
    allNodeIDs = new ArrayList<>();

    List<Node> allNodes = Node.getAll(); // get all nodes from Database

    for (Node n : allNodes) {
      allNodeIDs.add(n.getNodeID()); // get nodeId
    }

    System.out.println("initializing");
    System.out.println(mapNodes == null);

    Platform.runLater(
        () -> {
          try {
            generatePath(mapNodes.get1(), mapNodes.get2());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });

    generatePath(mapNodes.get1(), mapNodes.get2());
  }

  // this receives the data from PathfindingController
  void initData(Tuple<Integer, Integer> nodes) throws SQLException {
    initialize();
    System.out.println("initData start");
    System.out.println(nodes.get1() + ", " + nodes.get2() + " initData input");

    /*
        allNodeIDs = new ArrayList<>();

        List<Node> allNodes = Node.getAll(); // get all nodes from Database

        for (Node n : allNodes) {
          allNodeIDs.add(n.getNodeID()); // get nodeId
        }
    */
    mapNodes = nodes;

    System.out.println(mapNodes.get1() + ", " + mapNodes.get2() + " initData assigned to mapNodes");

    // generatePath(nodes.get1(), nodes.get2());
  }

  /**
   * Draws a visual representation of the path given in mapCanvas on top of mapImage.
   *
   * @param path The path of GraphNodes returned by callAStar
   */
  public void callMapDraw(ArrayList<GraphNode> path) {
    gc = mapCanvas.getGraphicsContext2D();
    MapDraw.drawShapes(gc, path, 0.05); // change the scale factor here!
  }

  @FXML
  /**
   * Runs when the "Find Path" button is pressed, performing A* and displaying the path on the map.
   */
  public void generatePath(int startIndex, int endIndex) throws SQLException, RuntimeException {

    // int startIndex = startNodeID.getSelectedIndex(); // from User Input
    // int endIndex = endNodeID.getSelectedIndex(); // from User Input

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

    System.out.println("before addFloorMapImage");

    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/unlabeledMaps/5% Scale/00_thelowerlevel1_unlabeled_5%.png"); // place the map on the page

    System.out.println("after addFloorMapImage");
    callMapDraw(path); // draw the path on top of the image

    System.out.println("after callMapDraw");
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
   * Method gets called when pathDisplay button is pressed Takes in startNodeID and endNodeID given
   * by user text input Preps graph using prepGraphDB method Prints out the path generated by AStar
   */
  /*
  @FXML
  public void generatePathOld() {
    // check that the fields are filled out
    if (startNodeID.getText().equals("") || endNodeID.getText().equals("")) {
      reminder.setText("Please fill out all fields in the form!");
      reminder.setVisible(true);
    } else {
      // create the graph hashMap
      HashMap<String, GraphNode> hospitalL1;

      // try to initialize the graph. If it fails, throw an error
      try {
        hospitalL1 = prepGraphDB(); // reads through DB files //
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      // get the nodeIDs from the application
      String sID = startNodeID.getText();
      String eID = endNodeID.getText();

      // check if the nodes entered are in the graph at all
      // Note: this could probably be a helper function, but that can happen later - Audrey
      boolean startValid = hospitalL1.containsKey(sID);
      boolean endValid = hospitalL1.containsKey(eID);

      // check both nodes
      if (!startValid && !endValid) {
        reminder.setText("Both IDs entered are invalid. Please try again.");
        reminder.setVisible(true);
      }
      // check start node
      else if (!startValid) {
        reminder.setText("The Start Node ID isn't valid. Please try again.");
        reminder.setVisible(true);
      }
      // check end node
      else if (!endValid) {
        reminder.setText("The End Node ID isn't valid. Please try again.");
        reminder.setVisible(true);
      } else {
        // run A*
        ArrayList<GraphNode> path = callAStar(hospitalL1, sID, eID); // makes a call to AStar //

        // print the path to the textField
        pathDisplay.setText(PathInterpreter.generatePathString(path));
      }
    }
  }
  */

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
    // table
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
    return DFS.traverse(graph.get(startNodeID), graph.get(endNodeID));
  }

  /**
   * @param graph a HashMap<String, Node> (normally) generated by prepDFS() from reading .csv files
   * @param startNodeID the ID of the node where the user wants to start from
   * @param endNodeID the ID of the node that the user wants to get to
   * @return the path between startNode and endNode, if it exists. Otherwise, null.
   */
  public static ArrayList<GraphNode> callAStar(
      HashMap<String, GraphNode> graph, String startNodeID, String endNodeID) {
    return AStar.traverse(graph.get(startNodeID), graph.get(endNodeID));
  }

  /** Method to clear the fields on the form on the UI page */
  @FXML
  public void clearForm() {

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
  }
}
