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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class KioskController {
  @FXML private Label announcement, left, right, leftD, rightD;
  @FXML public SplitPane mainSplitPane;
  @FXML public Node leftPane;
  @FXML public Node mapPane;
  @FXML public Node directionsPane;
  @FXML public Node rightPane;
  @FXML private Text directionsText;

  @FXML private AnchorPane anchorF3;
  @FXML private AnchorPane anchorF2;
  @FXML private AnchorPane anchorF1;
  @FXML private AnchorPane anchorL1;
  @FXML private AnchorPane anchorL2;

  @FXML private ImageView mainImageView;
  @FXML private GesturePane mainGesturePane;
  @FXML private StackPane mainStackPane;
  @FXML private Label moveDetails;
  @FXML private Label floorNumber;
  @FXML private Label bruh;

  private AnchorPane[] aps = new AnchorPane[5];
  private PathfindingSystem pathfindingSystem = new PathfindingSystem(new AStar());
  private int currentFloorIndex = 0;
  private ArrayList<String> floorPath = new ArrayList<>();
  private String directions;

  @FXML
  public void initialize() throws SQLException {
    System.out.println(kiosk.getRight());
    leftPane = mainSplitPane.getItems().get(0);
    mapPane = mainSplitPane.getItems().get(1);
    directionsPane = mainSplitPane.getItems().get(2);
    rightPane = mainSplitPane.getItems().get(3);

    announcement.setText(kiosk.getMessage());

    if (kiosk.isDirections()) {
      mainSplitPane.getItems().remove(leftPane);
      mainSplitPane.getItems().remove(rightPane);
      leftD.setText(kiosk.getLeft());
      rightD.setText(kiosk.getRight());
    } else {
      mainSplitPane.getItems().remove(directionsPane);
      left.setText(kiosk.getLeft());
      right.setText(kiosk.getRight());
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
    // String initialTableString = kiosk.getStartLocation().getFloor();
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
    System.out.println(path.size());
    floorPath = info.getFloorPath();
    if (start.getFloor().equals(end.getFloor())) {
      moveDetails.setText(kiosk.getMoveName() + " is moving on " + end.getFloor());
    } else {
      moveDetails.setText(
          kiosk.getMoveName() + " is moving from " + start.getFloor() + " to " + end.getFloor());
    }
    directions = pathfindingSystem.generatePathString(path, floorPath);
    directionsText.setText(directions);

    pathfindingSystem.drawPath(aps, path);
    cycleMaps();
  }

  /** Runs a timer that cycles through the different floor maps related to this move */
  public void cycleMaps() {
    Runnable runCycles = () -> oneCycle();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(runCycles, 0, 5, TimeUnit.SECONDS);
  }

  /** Method to cycle through one of the maps for this move's path */
  private void oneCycle() {

    // clear the anchorPanes w/ the drawn paths
    for (AnchorPane ap : aps) {
      ap.setVisible(false);
    }

    // Make this floor viewable
    String thisFloor = "L2";
    int size = floorPath.size();
    if (floorPath.size() > 0) {
      thisFloor = floorPath.get(currentFloorIndex % size);

      currentFloorIndex++;
    }
    mainImageView.setImage(ImageLoader.getImage(thisFloor));
    aps[Floor.indexFromTableString(thisFloor)].setVisible(true);
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }
}
