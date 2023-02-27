package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

public class KioskController {
  @FXML private Label announcement, left, right, leftD, rightD;
  @FXML public SplitPane mainSplitPane;
  @FXML public Node leftPane;
  @FXML public Node mapPane;
  @FXML public Node directionsPane;
  @FXML public Node rightPane;

  @FXML private AnchorPane anchorF3;
  @FXML private AnchorPane anchorF2;
  @FXML private AnchorPane anchorF1;
  @FXML private AnchorPane anchorL1;
  @FXML private AnchorPane anchorL2;

  @FXML private ImageView mainImageView;
  @FXML private GesturePane mainGesturePane;
  @FXML private StackPane mainStackPane;

  private AnchorPane[] aps = new AnchorPane[5];
  private PathfindingSystem pathfindingSystem = new PathfindingSystem(new AStar());
  private int currentFloorIndex = 0;
  private ArrayList<String> floorPath = new ArrayList<>();

  @FXML
  public void initialize() throws SQLException {
    leftPane = mainSplitPane.getItems().get(0);
    mapPane = mainSplitPane.getItems().get(1);
    directionsPane = mainSplitPane.getItems().get(2);
    rightPane = mainSplitPane.getItems().get(3);

    announcement.setText(kiosk.getMessage());
    left.setText(kiosk.getLeft());
    right.setText(kiosk.getRight());

    if (kiosk.isDirections()) {
      mainSplitPane.getItems().remove(leftPane);
      mainSplitPane.getItems().remove(rightPane);
    } else {
      mainSplitPane.getItems().remove(directionsPane);
    }

    // set anchorPanes into an array
    aps[0] = anchorL1;
    aps[1] = anchorL2;
    aps[2] = anchorF1;
    aps[3] = anchorF2;
    aps[4] = anchorF3;

    // prepare the gesture pane to attach to the stack pane
    this.mainGesturePane.setContent(mainStackPane);

    // set first map
    String initialTableString = kiosk.getStartLocation().getFloor();
    // currentFloor = Floor.indexFromTableString(initialTableString);
    // addFloorMapImage(initialTableString, mainImageView);

    runPathfinding();
  }

  /**
   * Method that runs pathfinding on the two nodes given via kiosk ie startNode and endNode
   *
   * @throws SQLException
   */
  private void runPathfinding() throws SQLException {
    pathfindingSystem.prepGraphDB(LocalDate.now());
    GraphNode start = pathfindingSystem.getNode(kiosk.getStartLocation().getNodeid());
    GraphNode end = pathfindingSystem.getNode(kiosk.getEndLocation().getNodeid());
    PathInfo info = pathfindingSystem.runPathfinding(start, end);
    ArrayList<GraphNode> path = info.getPath();
    floorPath = info.getFloorPath();

    callMapDraw(path);
  }

  /**
   * Given a path, draw it on its anchorPane and hide the other anchorPanes
   *
   * @param path the path that you want to be drawn
   */
  private void callMapDraw(ArrayList<GraphNode> path) {

    // clear the anchorPanes w/ the drawn paths
    for (AnchorPane ap : aps) {
      ap.getChildren().clear();
      ap.setVisible(false);
    }

    // Make this floor's pane viewable
    // aps[currentFloorIndex].setVisible(true);

    pathfindingSystem.drawPath(aps, path);
  }

  /** Method to cycle through all the maps for this move's path */
  @FXML
  public void cycleMaps() {

    // clear the anchorPanes w/ the drawn paths
    for (AnchorPane ap : aps) {
      ap.getChildren().clear();
      ap.setVisible(false);
    }

    // Make this floor viewable
    String thisFloor = "L2";
    int size = floorPath.size();
    if (floorPath.size() > 0) {
      thisFloor = floorPath.get(currentFloorIndex % size);
    }
    addFloorMapImage(thisFloor);
    aps[Floor.indexFromTableString(thisFloor)].setVisible(true);
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }

  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param floor is the table name of the floor
   */
  private void addFloorMapImage(String floor) {
    mainImageView.setImage(ImageLoader.getImage(floor));
  }
}
