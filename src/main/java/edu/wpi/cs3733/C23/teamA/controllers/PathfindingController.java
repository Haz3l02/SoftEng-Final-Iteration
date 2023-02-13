package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Algorithm;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.HospitalMaps;
import edu.wpi.cs3733.C23.teamA.serviceRequests.NodeIndicesHolder;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.hibernate.Session; // remove later

public class PathfindingController extends MenuController {

  // javaFX items
  @FXML private MFXFilterComboBox<String> startNodeID; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endNodeID; // field to enter endNode
  @FXML private MFXFilterComboBox<String> startFloorBox;
  @FXML private MFXFilterComboBox<String> endFloorBox;
  @FXML private MFXFilterComboBox<String> algosBox;
  @FXML private MFXDatePicker navDatePicker;
  @FXML private Text errorMessage;
  @FXML private Text pathMapText;

  // canvases
  @FXML private Canvas floorL1Canvas;
  @FXML private Canvas floorL2Canvas;
  @FXML private Canvas floorF1Canvas;
  @FXML private Canvas floorF2Canvas;
  @FXML private Canvas floorF3Canvas;

  // image views
  @FXML private ImageView floorL2;
  @FXML private ImageView floorL1;
  @FXML private ImageView floorF1;
  @FXML private ImageView floorF2;
  @FXML private ImageView floorF3;

  // stack panes
  @FXML private StackPane floorL1Stack;
  @FXML private StackPane floorL2Stack;
  @FXML private StackPane floorF1Stack;
  @FXML private StackPane floorF2Stack;
  @FXML private StackPane floorF3Stack;

  // gesture panes
  @FXML private GesturePane floorL1gPane;
  @FXML private GesturePane floorL2gPane;
  @FXML private GesturePane floorF1gPane;
  @FXML private GesturePane floorF2gPane;
  @FXML private GesturePane floorF3gPane;

  // Lists of Nodes and Node Data
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order
  private List<NodeEntity> allNodes;

  private Session session;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

  // the date of navigation
  private LocalDate navDate;

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
    // prepare floor/algorithm dropdowns
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

    // populates the floor/algorithm dropdowns
    startFloorBox.setItems(floors);
    endFloorBox.setItems(floors);
    algosBox.setItems(algos);

    LocationNameImpl table = new LocationNameImpl();
    // Database's list of longNames
    allNodeIDs = new ArrayList<String>();
    allLongNames =
        table.getAll().stream()
            .map(locationNameEntity -> locationNameEntity.getLongname())
            .toList();

    table.closeSession();

    // add the map images (also already done in SceneBuilder)
    addFloorMapImage(HospitalMaps.L2.getFilename(), floorL2);
    addFloorMapImage(HospitalMaps.L1.getFilename(), floorL1);
    addFloorMapImage(HospitalMaps.F1.getFilename(), floorF1);
    addFloorMapImage(HospitalMaps.F2.getFilename(), floorF2);
    addFloorMapImage(HospitalMaps.F3.getFilename(), floorF3);

    for (NodeEntity n : allNodes) {
      allNodeIDs.add(n.getNodeid()); // get nodeId
    }

    // Add to front end
    ObservableList<String> locations = FXCollections.observableArrayList(allLongNames);
    // populates the dropdown boxes
    startNodeID.setItems(locations);
    endNodeID.setItems(locations);

    // prepare the gesture panes
    Node nodeL1 = floorL1Stack;
    this.floorL1gPane.setContent(nodeL1);
    Node nodeL2 = floorL2Stack;
    this.floorL2gPane.setContent(nodeL2);
    Node nodeF1 = floorF1Stack;
    this.floorF1gPane.setContent(nodeF1);
    Node nodeF2 = floorF2Stack;
    this.floorF2gPane.setContent(nodeF2);
    Node nodeF3 = floorF3Stack;
    this.floorF3gPane.setContent(nodeF3);
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

  /**
   * Updates pathfindingSystem with a new PathfindingSystem object with the algorithm selected in
   * the dropdown
   *
   * @param event
   */
  @FXML
  public void setPathfindingAlgorithm(ActionEvent event) {
    pathfindingSystem = new PathfindingSystem(Algorithm.fromString(algosBox.getValue()));
  }

  @FXML
  public void setNavigationDate(ActionEvent event) {
    navDate = navDatePicker.getValue();
    // System.out.println(navDate);
  }

  @FXML
  public void fillStartLocationBox() {

    session = getSessionFactory().openSession();
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
    startLocBox.clear();
  }

  @FXML
  public void fillEndLocationBox() {

    session = getSessionFactory().openSession();
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
    endLocBox.clear();
  }

  /**
   * Given a path, draw it on mapCanvas.
   *
   * @param path the path that you want to be drawn
   */
  private void callMapDraw(ArrayList<GraphNode> path, Canvas canvas) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // constant for map size/coordinate manipulation
    pathfindingSystem.drawPath(gc, path, SCALE_FACTOR);
  }

  @FXML
  public void generatePath(ActionEvent event) throws SQLException, RuntimeException {

    int startIndex = startLocBox.getSelectedIndex();
    int endIndex = endLocBox.getSelectedIndex();
    int algIndex = algosBox.getSelectedIndex();

    if (startIndex == -1 || endIndex == -1 || algIndex == -1) {
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
      callMapDraw(path, floorL1Canvas); // draw the path on top of the image
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
