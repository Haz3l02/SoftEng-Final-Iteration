package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapdrawing.PathDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
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

  @FXML private AnchorPane anchor1;
  @FXML private AnchorPane anchor2;
  @FXML private AnchorPane anchor3;
  @FXML private AnchorPane anchor4;
  @FXML private ImageView image1;
  @FXML private ImageView image2;
  @FXML private ImageView image3;
  @FXML private ImageView image4;
  @FXML private GesturePane gesture1;
  @FXML private GesturePane gesture2;
  @FXML private GesturePane gesture3;
  @FXML private GesturePane gesture4;
  @FXML private StackPane stack1;
  @FXML private StackPane stack2;
  @FXML private StackPane stack3;
  @FXML private StackPane stack4;

  private Integer[] floors = new Integer[5]; // tells you which anchorPane it is at
  private AnchorPane[] aps = new AnchorPane[5];
  private PathfindingSystem pathfindingSystem = new PathfindingSystem(new AStar());
  private int currentFloor;

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

    System.out.println(kiosk.getStartLocation().getFloor());
    System.out.println(kiosk.getEndLocation().getFloor());

    aps[0] = anchor1;
    aps[1] = anchor2;
    aps[2] = anchor3;
    aps[3] = anchor4;
    AnchorPane anchor5 = new AnchorPane();
    aps[4] = anchor5;

    // TODO Setup the split pane thing and the map

    // prepare the gesture panes to attach to the stack panes
    this.gesture1.setContent(stack1);
    this.gesture2.setContent(stack2);
    this.gesture1.setContent(stack3);
    this.gesture2.setContent(stack4);

    // set first map
    //    String initialTableString = kiosk.getStartLocation().getFloor();
    //    currentFloor = Floor.indexFromTableString(initialTableString);
    addFloorMapImage("L1", image1);
    addFloorMapImage("L2", image2);
    addFloorMapImage("1", image3);
    addFloorMapImage("2", image4);

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
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(start, end).getPath();

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
      ap.setVisible(true);
    }
    //
    //    // Make this floor's pane viewable
    //    aps[currentFloor].setVisible(true);

    PathDraw.drawPath(aps, path, 5, 0.0675);
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
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
}
