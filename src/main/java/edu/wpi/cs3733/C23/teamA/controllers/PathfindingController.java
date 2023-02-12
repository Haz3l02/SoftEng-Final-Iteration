package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.MoveEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Algorithm;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.HospitalMaps;
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
import org.hibernate.Session; // remove later

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
  @FXML private ImageView floorL2;
  @FXML private ImageView floorL1;
  @FXML private ImageView floorF1;
  @FXML private ImageView floorF2;
  @FXML private ImageView floorF3;

  private Session session;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

  // objects needed for the maps
  private GraphicsContext gc;
  private double SCALE_FACTOR = 0.14;

  /**
   * Runs when the pathfinding page is opened, grabbing nodes from the database and anything else
   * that needs to exist in the page before pathfinding is called.
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException {
    ObservableList<String> floors =
        FXCollections.observableArrayList(
            Floor.L1.getExtendedString(),
            Floor.L2.getExtendedString(),
            Floor.F1.getExtendedString(),
            Floor.F2.getExtendedString(),
            Floor.F3.getExtendedString());
    ObservableList<String> algos =
        FXCollections.observableArrayList(
            Algorithm.ASTAR.getDropText(),
            Algorithm.BFS.getDropText(),
            Algorithm.DFS.getDropText());

    // populates the dropdown boxes
    startFloorBox.setItems(floors);
    endFloorBox.setItems(floors);
    algosBox.setItems(algos);

    addFloorMapImage(HospitalMaps.L2.getFilename(), floorL2);
    addFloorMapImage(HospitalMaps.L1.getFilename(), floorL1);
    addFloorMapImage(HospitalMaps.F1.getFilename(), floorF1);
    addFloorMapImage(HospitalMaps.F2.getFilename(), floorF2);
    addFloorMapImage(HospitalMaps.F3.getFilename(), floorF3);
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
  }

  @FXML
  public void setPathfindingAlgorithm(ActionEvent event) {
    pathfindingSystem = new PathfindingSystem(Algorithm.fromString(algosBox.getValue()));
  }

  // TODO: function to get an ObservableList of locations based on floor and some stuff to do that
  // when a floor is picked in dropdown

  @FXML
  public void fillStartLocationBox() {

    Session session = getSessionFactory().openSession();
    Floor floor = Floor.valueOf(Floor.fromString(startFloorBox.getValue()));

    List<NodeEntity> allNodesStartFloor =
        NodeEntity.getNodeOnFloor(floor.getTableString(), session); // get all nodes from Database

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();

    for (NodeEntity n : allNodesStartFloor) {
      idsFloor.add(n.getNodeid()); // get nodeId
      namesFloor.add(MoveEntity.mostRecentLoc(n.getNodeid(), session)); // get longName
    }
    session.close();

    ObservableList<String> locs = FXCollections.observableArrayList(namesFloor);

    startLocBox.setItems(locs);
    startLocBox.setDisable(false);
  }

  @FXML
  public void fillEndLocationBox() {

    Session session = getSessionFactory().openSession();
    Floor floor = Floor.valueOf(Floor.fromString(endFloorBox.getValue()));

    List<NodeEntity> allNodesEndFloor =
        NodeEntity.getNodeOnFloor(floor.getTableString(), session); // get all nodes from Database

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();

    for (NodeEntity n : allNodesEndFloor) {
      idsFloor.add(n.getNodeid()); // get nodeId
      namesFloor.add(MoveEntity.mostRecentLoc(n.getNodeid(), session)); // get longName
    }
    session.close();

    ObservableList<String> locs = FXCollections.observableArrayList(namesFloor);

    endLocBox.setItems(locs);
    endLocBox.setDisable(false);
  }

  private void callMapDraw(ArrayList<GraphNode> path) {
    GraphicsContext gc = mapCanvas.getGraphicsContext2D();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

    // constant for map size/coordinate manipulation
    pathfindingSystem.drawPath(gc, path, SCALE_FACTOR);
  }

  /**
   * Runs when the "Find Path" button is pressed, performing pathfinding and displaying the path on
   * the map.
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
        pathfindingSystem.runPathfinding(
            start, end); // makes a call to the algorithm that was selected

    // if a path was found, draw a path
    if (path != null) {
      pathMapText.setText(pathfindingSystem.generatePathString(path));
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
  private void addFloorMapImage(String pathName, ImageView iv) {
    File file = new File(pathName);
    Image image = new Image(file.toURI().toString());
    iv.setImage(image); // this does not work
  }
}
