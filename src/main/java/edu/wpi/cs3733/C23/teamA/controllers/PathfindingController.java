package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.MoveEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Algorithm;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.io.File;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.hibernate.Session;

public class PathfindingController extends MenuController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startLocBox; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endLocBox; // field to enter endNode
  @FXML private MFXFilterComboBox<String> startFloorBox;
  @FXML private MFXFilterComboBox<String> endFloorBox;
  @FXML private MFXFilterComboBox<String> algosBox;
  @FXML private Text errorMessage;

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order
  private List<NodeEntity> allNodes;

  @FXML private Text pathMapText;
  @FXML private Canvas mapCanvas; // to display the generated path
  @FXML private ImageView mapImage;

  private Session session;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

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
    ObservableList<String> floors =
        FXCollections.observableArrayList(
            Floor.L1.getTableString(),
            Floor.L2.getTableString(),
            Floor.F1.getTableString(),
            Floor.F2.getTableString(),
            Floor.F3.getTableString());
    ObservableList<String> algos =
        FXCollections.observableArrayList(
            Algorithm.ASTAR.getDropText(),
            Algorithm.BFS.getDropText(),
            Algorithm.DFS.getDropText());
    // populates the dropdown boxes
    startLocBox.setItems(locations);
    endLocBox.setItems(locations);
    startFloorBox.setItems(floors);
    endFloorBox.setItems(floors);
    algosBox.setItems(algos);

    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/00_thelowerlevel1_unlabeled.png");
  }

  /** Method to clear the fields on the form on the UI page */
  @FXML
  public void clearForm() {
    // clear the selection fields
    startLocBox.clear();
    endLocBox.clear();
    startFloorBox.clear();
    endFloorBox.clear();
    algosBox.clear();
    errorMessage.setText("");

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
  }

  // TODO: function to initialize PathfindingSystem with a different algorithm depending on which
  // one is selected in the dropdown

  @FXML
  public void setPathfindingAlgorithm(ActionEvent event) {

    /*
    pathfindingSystem =
        new PathfindingSystem();

     */
  }

  // TODO: function to get an ObservableList of locations based on floor and some stuff to do that
  // when a floor is picked in dropdown

  private void callMapDraw(ArrayList<GraphNode> path) {
    GraphicsContext gc = mapCanvas.getGraphicsContext2D();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    pathMapText.setText("");

    // constant for map size/coordinate manipulation
    pathfindingSystem.drawPath(gc, path, 1);
  }

  /**
   * Runs when the "Find Path" button is pressed, performing A* and displaying the path on the map.
   */
  @FXML
  public void generatePath(ActionEvent event) throws SQLException, RuntimeException {

    int startIndex = startLocBox.getSelectedIndex();
    int endIndex = endLocBox.getSelectedIndex();

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
        pathfindingSystem.runPathfinding(start, end); // makes a call to AStar

    // if a path was found, draw a path
    if (path != null) {
      pathMapText.setText("Path Between " + sName + " and " + eName + ".");
      callMapDraw(path); // draw the path on top of the image
    } else {
      pathMapText.setText("No Path Found Between " + sName + " and " + eName + ".");
    }
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

  /*
  public void goToMapScene(javafx.event.ActionEvent actionEvent) {
    Tuple<Integer, Integer> nodeIndices =
        new Tuple<>(startLocBox.getSelectedIndex(), endLocBox.getSelectedIndex());

    if (nodeIndices.get1() == -1 || nodeIndices.get2() == -1) {
      errorMessage.setText("Have not selected a location in both drop downs");
    } else {
      errorMessage.setText("");
      NodeIndicesHolder.getInstance().setNodes(nodeIndices);
      Navigation.navigate(Screen.PATHFINDING_MAP);
    }
  }

   */
}
