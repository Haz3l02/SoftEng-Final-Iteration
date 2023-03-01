package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapdrawing.PathDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInterpreter;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
  @FXML public Node mapPane;
  @FXML public Node directionsPane;
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
  @FXML private Text floorNumber;

  private AnchorPane[] aps = new AnchorPane[5];
  private PathfindingSystem pathfindingSystem = new PathfindingSystem(new AStar());
  private int currentFloorIndex = 0;
  private ArrayList<String> floorPath = new ArrayList<>();
  private String directions;
  private ArrayList<String> directionsArray;

  @FXML
  public void initialize() throws SQLException {
    System.out.println(kiosk.getRight());
    mapPane = mainSplitPane.getItems().get(0);
    directionsPane = mainSplitPane.getItems().get(1);

    announcement.setText(kiosk.getMessage());
    leftD.setText(kiosk.getLeft());
    rightD.setText(kiosk.getRight());

    if (!kiosk.isDirections()) {
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
    mainGesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    runPathfinding();

    // added scaling to auto-zoom
    Platform.runLater(
        () -> {
          mainGesturePane.zoomTo(1.15, new Point2D(650, 230));
        });
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
    if (start.getFloor().equals(end.getFloor())) {
      moveDetails.setText(
          kiosk.getMoveName()
              + " is moving on "
              + Floor.extendedStringFromTableString(end.getFloor())
              + ".");
    } else {
      moveDetails.setText(
          kiosk.getMoveName()
              + " is moving from "
              + Floor.extendedStringFromTableString(start.getFloor())
              + " to "
              + Floor.extendedStringFromTableString(end.getFloor())
              + ".");
    }
    // directions = PathInterpreter.generatePathStringShort(path, floorPath);
    // directionsText.setText(directions);
    directionsArray = PathInterpreter.generatePathStringShortArray(path, floorPath);

    PathDraw.drawPathLines(aps, path, 5, 0.135);
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

    // hide the anchorPanes with the drawn paths
    for (AnchorPane ap : aps) {
      ap.setVisible(false);
    }

    // Make this floor viewable
    String thisFloor = "L2";
    int size = floorPath.size();
    if (floorPath.size() > 0) {
      thisFloor = floorPath.get(currentFloorIndex % size);
      directionsText.setText(directionsArray.get(currentFloorIndex % size));
      currentFloorIndex++;
    }
    mainImageView.setImage(ImageLoader.getImage(thisFloor));
    aps[Floor.indexFromTableString(thisFloor)].setVisible(true);
    floorNumber.setText(Floor.extendedStringFromTableString(thisFloor));
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }
}
