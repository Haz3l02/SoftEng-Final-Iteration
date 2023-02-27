package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapdrawing.PathfindingDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Algorithm;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.tts.TextReader;
import io.github.palexdev.materialfx.controls.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

/** Controller for the PathfindingNewFXML.fxml page */
public class PathfindingController extends MenuController {

  // javaFX items for buttons and text
  @FXML private MFXFilterComboBox<String> startLocBox; // field to pick start location
  @FXML private MFXFilterComboBox<String> endLocBox; // field to pick end location
  @FXML private MFXFilterComboBox<String> startFloorBox; // dropdown of floors
  @FXML private MFXFilterComboBox<String> endFloorBox; // dropdown of floors
  @FXML private MFXFilterComboBox<String> algosBox; // pick which alg to use
  @FXML private MFXDatePicker navDatePicker;
  @FXML private Text errorMessage;
  @FXML private Text pathMapText;
  @FXML private MFXCheckbox avoidStairsCheckbox;
  @FXML private MFXToggleButton toggleLocationNames;
  @FXML private MFXToggleButton toggleServiceRequests;
  @FXML Text srReminder;

  // FXML Data for buttons and panes
  @FXML private Button l1Button;
  @FXML private Button l2Button;
  @FXML private Button f1Button;
  @FXML private Button f2Button;
  @FXML private Button f3Button;
  @FXML private ImageView mainImageView; // imageview to be changed when each floor is selected
  @FXML private AnchorPane anchorF3;
  @FXML private AnchorPane anchorF2;
  @FXML private AnchorPane anchorF1;
  @FXML private AnchorPane anchorL2;
  @FXML private AnchorPane anchorL1;
  @FXML private AnchorPane serviceRequestPane; // displays service requests on currentFloor
  @FXML private AnchorPane textAnchorPane; // displays location names on currentFloor
  @FXML private StackPane mainStackPane; // stack pane with all the anchor panes and image view
  @FXML private GesturePane mainGesturePane; // gesture pane to sync with stack pane above
  @FXML MFXTextField adminMessage;
  @FXML Text messageText;

  // TTS buttons
  @FXML private MFXButton speakButton;
  @FXML private MFXButton stopSpeakingButton;

  // local variables saved
  private AnchorPane[] aps = new AnchorPane[5];
  private int currentFloor = 1;
  private final IdNumberHolder holder = IdNumberHolder.getInstance();

  // Lists of Nodes and Node Data
  private List<String> startNodeIDs; // List of all Node IDs in specific order
  private List<String> endNodeIDs;
  private HashMap<MoveEntity, MoveEntity> movesInNextWeek;

  // a PathfindingSystem to run methods in the pathfinding package
  private static PathfindingSystem pathfindingSystem;

  // the date of navigation
  private LocalDate navDate;
  private LocalDate weekLater;

  @FXML
  public void sendMessage() {
    messageText.setText(adminMessage.getText());
    adminMessage.clear();
  }

  @FXML
  public void switchToHomeScene() {
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  /**
   * Runs when the pathfinding page is opened, grabbing nodes from the database and anything else
   * that needs to exist in the page before pathfinding is called.
   */
  public void initialize() throws SQLException {
    // prepare floor/algorithm dropdowns
    srReminder.setVisible(false);
    if (!holder.getJob().equalsIgnoreCase("admin")) {
      adminMessage.setDisable(true);
      adminMessage.setPromptText("Only admins can submit messages");
    }

    ObservableList<String> floors =
        FXCollections.observableArrayList(
            Floor.L2.getExtendedString(),
            Floor.L1.getExtendedString(),
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

    // anchor panes
    aps[0] = anchorL1;
    aps[1] = anchorL2;
    aps[2] = anchorF1;
    aps[3] = anchorF2;
    aps[4] = anchorF3;

    String initialTableString = "L2";
    // add the map images
    addFloorMapImage(initialTableString, mainImageView);

    // prepare the gesture pane to attach to the stack pane
    Node stackPane = mainStackPane;
    this.mainGesturePane.setContent(stackPane);

    // autofill the date picker to the current date
    // navDatePicker.setValue(LocalDate.of(2023, 1, 1));
    navDatePicker.setValue(navDatePicker.getCurrentDate());
    navDate = navDatePicker.getValue();

    // get the moves in the next week
    weekLater = navDate.plusDays(7);
    movesInNextWeek = FacadeRepository.getInstance().getLocationChanges(navDate, weekLater);

    // don't show location names or service requests on open
    serviceRequestPane.setVisible(false);
    textAnchorPane.setVisible(false);
    // show service request icons and location name
    List<NodeEntity> allNodesL2 =
        FacadeRepository.getInstance().getNodesOnFloor(initialTableString);
    PathfindingDraw.drawServiceRequestIcons(serviceRequestPane, initialTableString);
    PathfindingDraw.drawLocations(allNodesL2, textAnchorPane);

    // Action Listener for toggle switch
    toggleServiceRequests
        .selectedProperty()
        .addListener(
            Observable -> {
              changeSRs();
              srReminder.setVisible(true);
            });
    toggleLocationNames.selectedProperty().addListener(Observable -> changeLocations());
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

    // disable stuff
    startFloorBox.setDisable(true);
    endFloorBox.setDisable(true);
    startLocBox.setDisable(true);
    endLocBox.setDisable(true);

    // reset the location lists
    ObservableList<String> empty = FXCollections.observableArrayList();
    startLocBox.setItems(empty);
    endLocBox.setItems(empty);

    // text
    pathMapText.setText("Directions on how to get to your destination go here...");
    errorMessage.setText("");
  }

  /** Toggles Service Requests viewable on click */
  @FXML
  public void changeSRs() {
    serviceRequestPane.setVisible(!serviceRequestPane.isVisible());
  }

  /** Toggles location names viewable on click */
  @FXML
  public void changeLocations() {
    textAnchorPane.setVisible(!textAnchorPane.isVisible());
  }

  /**
   * Updates pathfindingSystem with a new PathfindingSystem object with the algorithm selected in
   * the dropdown
   */
  @FXML
  public void setPathfindingAlgorithm() {
    pathfindingSystem = new PathfindingSystem(Algorithm.fromString(algosBox.getValue()));
    startFloorBox.setDisable(false);
    endFloorBox.setDisable(false);
  }

  /**
   * Sets the navigation date using a calendar and clears dropdowns accordingly and gets moves for
   * the next week
   */
  @FXML
  public void setNavigationDate() {
    navDate = navDatePicker.getValue();

    startLocBox.clear();
    endLocBox.clear();
    startFloorBox.clear();
    endFloorBox.clear();

    startLocBox.setDisable(true);
    endLocBox.setDisable(true);

    ObservableList<String> empty = FXCollections.observableArrayList();
    startLocBox.setItems(empty);
    endLocBox.setItems(empty);

    // get the moves in the next week
    weekLater = navDate.plusDays(7);
    movesInNextWeek = FacadeRepository.getInstance().getLocationChanges(navDate, weekLater);
  }

  /** Using the value selected in startFloorBox, it fills the location dropdowns accordingly */
  @FXML
  public void fillStartLocationBox() {
    Floor floor = Floor.valueOf(Floor.fromString(startFloorBox.getValue()));

    // NOTE: moveAllMostRecentFloor may have been overwritten, as it stopped working. This still
    // works though,
    // and isn't that much slower.
    List<MoveEntity> moveEntities = FacadeRepository.getInstance().moveAllMostRecent(navDate);
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

  /** Using the value selected in endFloorBox, it fills the location dropdowns accordingly */
  @FXML
  public void fillEndLocationBox() {
    Floor floor = Floor.valueOf(Floor.fromString(endFloorBox.getValue()));

    // NOTE: moveAllMostRecentFloor may have been overwritten, as it stopped working. This still
    // works though,
    // and isn't that much slower.
    List<MoveEntity> moveEntities = FacadeRepository.getInstance().moveAllMostRecent(navDate);
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

  /** pain */
  public void checkForMovesStart() {
    int startIndex = startLocBox.getSelectedIndex();
    int algoIndex = algosBox.getSelectedIndex();

    // make sure the algorithm is picked and the box has a value
    if (algoIndex != -1 && startIndex != -1) {
      // get the location name
      String startLocName = startLocBox.getValue();

      // loop through the keys (moveEntities) hashMap of move entities
      for (MoveEntity m : movesInNextWeek.keySet()) {

        // long name of the current key (most recent) and the date
        LocationNameEntity locName = m.getLocationName();
        LocalDate moveDate = m.getMovedate();

        if (locName != null) {
          if (locName.getLongname().equals(startLocName)) {
            // Ask the user if they want the path from current to future displayed
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Notice for Upcoming Moves");
            alert.setHeaderText(
                "The location selected has an upcoming move, set to happen on "
                    + moveDate.toString()
                    + ".");
            alert.setContentText(
                "Would you like to navigate from its current location to its new location?");

            if (alert.showAndWait().get() == ButtonType.OK) {
              // get the IDs from the initial (value) and final (key) moves
              String startID = startNodeIDs.get(startIndex);
              String endID = movesInNextWeek.get(m).getNode().getNodeid();

              try {
                generatePathFromMovePopup(startID, endID);
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          }
        }
      }
    }
  }

  /** pain II, electric boogaloo */
  public void checkForMovesEnd() {
    int endIndex = endLocBox.getSelectedIndex();
    int algoIndex = algosBox.getSelectedIndex();

    // make sure the algorithm is picked and the box has a value
    if (algoIndex != -1 && endIndex != -1) {
      // get the location name
      String endLocName = endLocBox.getValue();

      // loop through the keys (moveEntities) hashMap of move entities
      for (MoveEntity m : movesInNextWeek.keySet()) {

        // long name of the current key (most recent)
        LocationNameEntity locName = m.getLocationName();
        LocalDate moveDate = m.getMovedate();

        if (locName != null) {
          if (locName.getLongname().equals(endLocName)) {
            // Ask the user if they want the path from current to future displayed
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Notice for Upcoming Moves");
            alert.setHeaderText(
                "The location selected has an upcoming move, set to happen on "
                    + moveDate.toString()
                    + ".");
            alert.setContentText(
                "Would you like to navigate from its current location to its new location?");

            if (alert.showAndWait().get() == ButtonType.OK) {
              // get the IDs from the initial (value) and final (key) moves
              String startID = endNodeIDs.get(endIndex);
              String endID = movesInNextWeek.get(m).getNode().getNodeid();

              try {
                generatePathFromMovePopup(startID, endID);
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Given a path, draw it on its anchorPane and hide the other anchorPanes
   *
   * @param path the path that you want to be drawn
   */
  private void callMapDraw(ArrayList<GraphNode> path, ArrayList<String> floorPath) {

    // clear the anchorPanes w/ the drawn paths
    for (AnchorPane ap : aps) {
      ap.getChildren().clear();
      ap.setVisible(false);
    }

    // Make this floor's pane viewable
    aps[currentFloor].setVisible(true);

    pathfindingSystem.drawPath(aps, path);
  }

  @FXML
  public void generatePath() throws SQLException, RuntimeException {

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
        callMapDraw(path, floorPath);

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
      callMapDraw(path, floorPath);

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
   * @param floor is the table name of the floor
   * @param iv is the image view to be updated
   */
  private void addFloorMapImage(String floor, ImageView iv) {
    Image image = ImageLoader.getImage(floor);
    iv.setImage(image);
  }

  @FXML
  public void generateFloor(ActionEvent event) {
    int previousFloor = currentFloor;
    String tableFloor = "L1";
    if (event.getSource().equals(l1Button)) {
      tableFloor = "L1";
      currentFloor = 0;
    } else if (event.getSource().equals(l2Button)) {
      tableFloor = "L2";
      currentFloor = 1;
    } else if (event.getSource().equals(f1Button)) {
      tableFloor = "1";
      currentFloor = 2;
    } else if (event.getSource().equals(f2Button)) {
      tableFloor = "2";
      currentFloor = 3;
    } else if (event.getSource().equals(f3Button)) {
      tableFloor = "3";
      currentFloor = 4;
    }
    initializeFloorMap(tableFloor, previousFloor);
  }

  private void initializeFloorMap(String tableFloor, int previousFloor) {

    // show map image
    addFloorMapImage(tableFloor, mainImageView);

    // show nodes and edges for this floor
    aps[previousFloor].setVisible(false);
    aps[currentFloor].setVisible(true);

    // show service request icons
    List<NodeEntity> allNodes = FacadeRepository.getInstance().getNodesOnFloor(tableFloor);
    PathfindingDraw.drawServiceRequestIcons(serviceRequestPane, tableFloor);

    // show location names
    PathfindingDraw.drawLocations(allNodes, textAnchorPane);
  }

  /** Takes the text in the pathfinding directions and speaks them out loud in english. */
  @FXML
  public void directionsToSpeech(ActionEvent event) {
    TextReader tts = new TextReader(pathMapText.getText());
    if (event.getSource().equals(speakButton)) {
      tts.readText();
    } else if (event.getSource().equals(stopSpeakingButton)) {
      tts.stopText();
    }
  }
}
