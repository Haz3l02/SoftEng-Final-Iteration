package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.*;
import io.github.palexdev.materialfx.controls.*;
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
  @FXML private MFXCheckbox avoidStairsCheckbox;
  @FXML private MFXToggleButton toggleLocationNames;
  @FXML private MFXToggleButton toggleServiceRequests;
  @FXML private MFXToggleButton toggleUpcomingMoves;

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
  private HashMap<MoveEntity, MoveEntity> movesInNextWeek;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

  // the date of navigation
  private LocalDate navDate;
  private LocalDate weekLater;

  // objects needed for the maps
  private GraphicsContext[] gcs = new GraphicsContext[5];
  private final double SCALE_FACTOR = 0.135;

  // popup stuff (move elsewhere?)
  @FXML private MFXButton okButton; // return true from popup
  @FXML private MFXButton cancelButton; // return false from popup
  @FXML private Text popupText; // set the location name in the rest of the text

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

    // add the map images (also already done in SceneBuilder)
    /*
    addFloorMapImage("L1", floorL2);
    addFloorMapImage("L2", floorL1);
    addFloorMapImage("1", floorF1);
    addFloorMapImage("2", floorF2);
    addFloorMapImage("3", floorF3);
    */

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
    // navDatePicker.setValue(LocalDate.of(2023, 1, 1));
    navDatePicker.setValue(navDatePicker.getCurrentDate());
    navDate = navDatePicker.getValue();

    // get the moves in the next week
    weekLater = navDate.plusDays(7);
    movesInNextWeek = FacadeRepository.getInstance().getLocationChanges(navDate, weekLater);
  }

  /** Method to clear the fields on the form on the UI page */
  @FXML
  public void clearForm() {
    // clear the selection fields
    startLocBox.clearSelection();
    endLocBox.clearSelection();
    startFloorBox.clearSelection();
    endFloorBox.clearSelection();
    algosBox.clearSelection();
    navDatePicker.clear();

    // reset the location lists
    ObservableList<String> empty = FXCollections.observableArrayList();
    startLocBox.setItems(empty);
    endLocBox.setItems(empty);

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

    startLocBox.clear();
    endLocBox.clear();
    startFloorBox.clear();
    endFloorBox.clear();

    ObservableList<String> empty = FXCollections.observableArrayList();
    startLocBox.setItems(empty);
    endLocBox.setItems(empty);

    // get the moves in the next week
    weekLater = navDate.plusDays(7);
    movesInNextWeek = FacadeRepository.getInstance().getLocationChanges(navDate, weekLater);
  }

  @FXML
  public void fillStartLocationBox() {
    Floor floor = Floor.valueOf(Floor.fromString(startFloorBox.getValue()));

    List<MoveEntity> moveEntities =
        FacadeRepository.getInstance().moveAllMostRecentFloor(navDate, floor.getTableString());
    System.out.println(moveEntities.size());

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();

    LocationNameEntity locNameEnt;
    NodeEntity node;

    for (MoveEntity m : moveEntities) {
      locNameEnt = m.getLocationName();
      node = m.getNode();
      // if the LocationNameEntity isn't null, add it to the dropdown.
      // If it is null, it's a node w/ no location attached, and doesn't need to be there
      if (locNameEnt != null
          && node.getFloor().equals(floor.getTableString())
          && !idsFloor.contains(node.getNodeid())) {
        idsFloor.add(node.getNodeid()); // get nodeId
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

    List<MoveEntity> moveEntities = FacadeRepository.getInstance().moveAllMostRecent(navDate);
    System.out.println(moveEntities.size());

    ArrayList<String> idsFloor = new ArrayList<>();
    ArrayList<String> namesFloor = new ArrayList<>();

    LocationNameEntity locNameEnt;
    NodeEntity node;

    for (MoveEntity m : moveEntities) {
      locNameEnt = m.getLocationName();
      node = m.getNode();
      // if the LocationNameEntity isn't null, add it to the dropdown.
      // If it is null, it's a node w/ no location attached, and doesn't need to be there
      if (locNameEnt != null
          && node.getFloor().equals(floor.getTableString())
          && !idsFloor.contains(node.getNodeid())) {
        idsFloor.add(node.getNodeid()); // get nodeId
        namesFloor.add(locNameEnt.getLongname()); // get longName
      }
    }

    ObservableList<String> locs = FXCollections.observableArrayList(namesFloor);
    endNodeIDs = idsFloor;

    endLocBox.setItems(locs);
    endLocBox.setDisable(false);
    endLocBox.clear();
  }

  public void checkForMoves() throws SQLException {
    int startIndex = startLocBox.getSelectedIndex();
    int endIndex = endLocBox.getSelectedIndex();
    int algoIndex = algosBox.getSelectedIndex();

    // if the locBoxes or algosBox are empty, don't do anything
    if ((startIndex == -1 && endIndex == -1) || algoIndex == -1) {
      // then don't run anything :)
    } else {
      // get the location names
      String startLocName = startLocBox.getValue();
      String endLocName = endLocBox.getValue();

      // loop through the keys (moveEntities) hashMap of move entities
      for (MoveEntity m : movesInNextWeek.keySet()) {
        // long name of the current key (most recent)
        LocationNameEntity locName = m.getLocationName();

        if (locName != null) {
          if (locName.getLongname().equals(startLocName)
              || locName.getLongname().equals(endLocName)) {

            // now strip the user of their agency and do things for them (change this later)
            // get the IDs from the initial (value) and final (key) moves
            String startID = movesInNextWeek.get(m).getNode().getNodeid();
            String endID = m.getNode().getNodeid();

            // System.out.println(startID);
            // System.out.println(endID);
            // System.out.println(movesInNextWeek.get(m).getMovedate());
            // System.out.println(m.getMovedate());

            // TODO if/else inside popup; if yes, execute and break- also add message
            generatePathFromMovePopup(startID, endID);
            break; // so that it doesn't check the rest of the moves
          }
        }
      }
    }
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
    int startFloorIndex = startFloorBox.getSelectedIndex();
    int endFloorIndex = endFloorBox.getSelectedIndex();

    if (startIndex == -1
        || endIndex == -1
        || algIndex == -1
        || startFloorIndex == -1
        || endFloorIndex == -1
        || navDatePicker.getValue() == null) {
      reminder.setText("Please select an option from all fields in the form!");
      reminder.setVisible(true);
    } else {
      // create the graph hashMap where String is nodeId and GraphNode is the node
      pathfindingSystem.prepGraphDB(navDate);

      // get the IDs from the input combined w/ indexes
      String sName = startNodeIDs.get(startIndex);
      String eName = endNodeIDs.get(endIndex);

      // run pathfinding
      GraphNode start = pathfindingSystem.getNode(sName);
      GraphNode end = pathfindingSystem.getNode(eName);
      PathInfo pathInfo;

      // makes a call to the algorithm that was selected
      if (avoidStairsCheckbox.isSelected()) {
        pathInfo = pathfindingSystem.runPathfindingNoStairs(start, end);
      } else {
        pathInfo = pathfindingSystem.runPathfinding(start, end);
      }

      // if pathInfo isn't null, grab the path and draw it
      if (pathInfo != null) {
        // get the paths from pathInfo
        ArrayList<GraphNode> path = pathInfo.getPath();
        ArrayList<String> floorPath = pathInfo.getFloorPath();

        pathMapText.setText(pathfindingSystem.generatePathString(path, floorPath));
        callMapDraw(path);

        if (pathInfo.isContainsStairs()) {
          errorMessage.setText(
              "Disclaimer: The path generated between "
                  + start.getLongName()
                  + " and "
                  + end.getLongName()
                  + " uses stairs.");
        } else {
          errorMessage.setText("");
        }
      } else {
        pathMapText.setText(
            "No Path Found Between " + start.getLongName() + " and " + end.getLongName() + ".");
      }
    }
  }

  @FXML
  public void generatePathFromMovePopup(String startID, String endID)
      throws SQLException, RuntimeException {
    // create the graph hashMap where String is nodeId and GraphNode is the node
    pathfindingSystem.prepGraphDB(navDate); // don't know if this will work tbh

    // run pathfinding
    GraphNode start = pathfindingSystem.getNode(startID);
    GraphNode end = pathfindingSystem.getNode(endID);
    PathInfo pathInfo;

    // makes a call to the algorithm that was selected
    if (avoidStairsCheckbox.isSelected()) {
      pathInfo = pathfindingSystem.runPathfindingNoStairs(start, end);
    } else {
      pathInfo = pathfindingSystem.runPathfinding(start, end);
    }

    // if pathInfo isn't null, grab the path and draw it
    if (pathInfo != null) {
      // get the paths from pathInfo
      ArrayList<GraphNode> path = pathInfo.getPath();
      ArrayList<String> floorPath = pathInfo.getFloorPath();

      pathMapText.setText(pathfindingSystem.generatePathString(path, floorPath));
      callMapDraw(path);

      if (pathInfo.isContainsStairs()) {
        errorMessage.setText(
            "Disclaimer: The path generated between "
                + start.getLongName()
                + " and "
                + end.getLongName()
                + " uses stairs.");
      } else {
        errorMessage.setText("");
      }
    } else {
      pathMapText.setText(
          "No Path Found Between " + start.getLongName() + " and " + end.getLongName() + ".");
    }
  }

  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param floor is the tablename of the floor
   * @param iv is the image view to be updated
   */
  private void addFloorMapImage(String floor, ImageView iv) {
    Image image = ImageLoader.getImage(floor);
    iv.setImage(image);
  }
}
