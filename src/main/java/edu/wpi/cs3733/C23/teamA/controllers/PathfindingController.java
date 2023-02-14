package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.*;
import io.github.palexdev.materialfx.controls.MFXButton;
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

public class PathfindingController extends MenuController {

  //// javaFX items
  @FXML private MFXFilterComboBox<String> startLocBox; // field to enter startNode
  @FXML private MFXFilterComboBox<String> endLocBox; // field to enter endNode
  @FXML private MFXFilterComboBox<String> startFloorBox;
  @FXML private MFXFilterComboBox<String> endFloorBox;
  @FXML private MFXFilterComboBox<String> algosBox;
  @FXML private MFXDatePicker navDatePicker;
  @FXML private Text errorMessage;
  @FXML private Text pathMapText;
  @FXML private MFXButton clearButton;

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
  private List<String> startNodeIDs; // List of all Node IDs in specific order
  private List<String> endNodeIDs;
  private List<String> allLongNames; // List of corresponding long names in order
  private List<NodeEntity> allNodes;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

  // the date of navigation
  private LocalDate navDate;

  // objects needed for the maps
  private GraphicsContext gcL1;
  private GraphicsContext gcL2;
  private GraphicsContext gcF1;
  private GraphicsContext gcF2;
  private GraphicsContext gcF3;
  private GraphicsContext[] gcs = new GraphicsContext[5];
  private double SCALE_FACTOR = 0.135;

  // database objects
  MoveImpl moveImpl;
  NodeImpl nodeImpl;

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

    /*
    LocationNameImpl table = new LocationNameImpl();
    allNodeIDs = new ArrayList<String>();
    allLongNames =
        table.getAll().stream()
            .map(locationNameEntity -> locationNameEntity.getLongname())
            .toList();

    table.closeSession();
     */

    // add the map images (also already done in SceneBuilder)
    addFloorMapImage(HospitalMaps.L2.getFilename(), floorL2);
    addFloorMapImage(HospitalMaps.L1.getFilename(), floorL1);
    addFloorMapImage(HospitalMaps.F1.getFilename(), floorF1);
    addFloorMapImage(HospitalMaps.F2.getFilename(), floorF2);
    addFloorMapImage(HospitalMaps.F3.getFilename(), floorF3);

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

    // autofill the date picker to the current date
    navDatePicker.setValue(navDatePicker.getCurrentDate());
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
    navDatePicker.clear();

    // canvases
    for (GraphicsContext gc : gcs) {
      gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    // text
    pathMapText.setText("Directions on how to get to your destination go here...");
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
    Floor floor = Floor.valueOf(Floor.fromString(startFloorBox.getValue()));
    nodeImpl = new NodeImpl();
    moveImpl = new MoveImpl();

    List<NodeEntity> allNodesStartFloor =
        nodeImpl.getNodeOnFloor(floor.getTableString()); // get all nodes from Database

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();
    LocationNameEntity locNameEnt;

    for (NodeEntity n : allNodesStartFloor) {
      locNameEnt = moveImpl.mostRecentLoc(n.getNodeid());
      // if the LocationNameEntity isn't null, add it to the dropdown. If it is, it's a node w/ no
      // location attached
      if (locNameEnt != null) {
        idsFloor.add(n.getNodeid()); // get nodeId
        namesFloor.add(locNameEnt.getLongname()); // get longName
      }
    }

    ObservableList<String> locs = FXCollections.observableArrayList(namesFloor);
    startNodeIDs = idsFloor;

    startLocBox.setItems(locs);
    startLocBox.setDisable(false);
    startLocBox.clear();
  }

  @FXML
  public void fillEndLocationBox() {
    Floor floor = Floor.valueOf(Floor.fromString(endFloorBox.getValue()));
    nodeImpl = new NodeImpl();
    moveImpl = new MoveImpl();

    List<NodeEntity> allNodesStartFloor =
        nodeImpl.getNodeOnFloor(floor.getTableString()); // get all nodes from Database

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();
    LocationNameEntity locNameEnt;

    for (NodeEntity n : allNodesStartFloor) {
      locNameEnt = moveImpl.mostRecentLoc(n.getNodeid());
      // if the LocationNameEntity isn't null, add it to the dropdown. If it is, it's a node w/ no
      // location attached
      if (locNameEnt != null) {
        idsFloor.add(n.getNodeid()); // get nodeId
        namesFloor.add(locNameEnt.getLongname()); // get longName
      }
    }

    ObservableList<String> locs = FXCollections.observableArrayList(namesFloor);
    endNodeIDs = idsFloor;

    endLocBox.setItems(locs);
    endLocBox.setDisable(false);
    endLocBox.clear();
  }

  /**
   * Given a path, draw it on mapCanvas.
   *
   * @param path the path that you want to be drawn
   */
  private void callMapDraw(ArrayList<GraphNode> path) {
    // getting graphicsContents for each canvas and put in array
    gcs[0] = floorL1Canvas.getGraphicsContext2D();
    gcs[1] = floorL2Canvas.getGraphicsContext2D();
    gcs[2] = floorF1Canvas.getGraphicsContext2D();
    gcs[3] = floorF2Canvas.getGraphicsContext2D();
    gcs[4] = floorF3Canvas.getGraphicsContext2D();

    // clear the canvases w/ the drawn paths
    for (GraphicsContext gc : gcs) {
      gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    pathfindingSystem.drawPath(gcs, path, SCALE_FACTOR);
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
    String sName = startNodeIDs.get(startIndex);
    String eName = endNodeIDs.get(endIndex);

    // run A*
    GraphNode start = pathfindingSystem.getNode(sName);
    GraphNode end = pathfindingSystem.getNode(eName);
    ArrayList<GraphNode> path =
        pathfindingSystem.runPathfinding(
            start, end); // makes a call to the algorithm that was selected

    // if a path was found, draw a path
    if (path != null) {
      pathMapText.setText(pathfindingSystem.generatePathString(path));
      callMapDraw(path);
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
