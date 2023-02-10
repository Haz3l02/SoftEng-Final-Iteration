package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import edu.wpi.cs3733.C23.teamA.serviceRequests.NodeIndicesHolder;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import oracle.ucp.common.waitfreepool.Tuple;
import org.hibernate.Session;

/* This class has methods for pathfinding UI as well as methods to
create a graph using nodes from database and method to call AStar
to obtain and later print the path */
public class PathfindingMapController extends ServiceRequestController {

  // javaFX items
  @FXML private Text pathMapText;
  @FXML private Canvas mapCanvas; // to display the generated path
  @FXML private ImageView mapImage;

  private Session session;
  // Lists of Nodes and Node Data

  private List<String> allNodeIDs; // List of all Node IDs in specific order

  // a PathfindingSystem to run methods in the pathfinding package
  private static final PathfindingSystem pathfindingSystem = new PathfindingSystem();
  private double SCALE_FACTOR = 0.15;

  /**
   * Runs when the pathfinding page is opened, grabbing nodes from the database and anything else
   * that needs to exist in the page before pathfinding is called.
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException {
    Tuple<Integer, Integer> mapNodes = NodeIndicesHolder.getInstance().getNodes();
    allNodeIDs = new ArrayList<>();
    session = getSessionFactory().openSession();
    List<NodeEntity> allNodes =
        getAllRecords(NodeEntity.class, session); // get all nodes from Database
    for (NodeEntity n : allNodes) {
      allNodeIDs.add(n.getNodeid()); // get nodeId
    }
    session.close();
    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/20% Scale/00_thelowerlevel1_unlabeled_20%.png"); // place the map on the page

    // calls the function which makes and draws on the map
    generatePath(mapNodes.get1(), mapNodes.get2());
  }

  /**
   * Draws a visual representation of the path given in mapCanvas on top of mapImage.
   *
   * @param path The path of GraphNodes returned by callAStar, assumed not null
   */
  private void callMapDraw(ArrayList<GraphNode> path) {
    GraphicsContext gc = mapCanvas.getGraphicsContext2D();

    // clear the canvas w/ the drawn path; does NOT hide the map image
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    pathMapText.setText("");

    // constant for map size/coordinate manipulation
    pathfindingSystem.drawPath(gc, path, SCALE_FACTOR);
  }

  @FXML
  /**
   * Runs when the "Find Path" button is pressed, performing A* and displaying the path on the map.
   *
   * @param startIndex starting node index in the combo box on the previous page
   * @param endIndex ending node index in the combo box on the previous page
   */
  public void generatePath(int startIndex, int endIndex) throws SQLException, RuntimeException {

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
        pathfindingSystem.traverseAStar(start, end); // makes a call to AStar

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

  @FXML
  public void switchToPathfindingScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.PATHFINDING);
  }
}
