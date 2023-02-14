package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.pathfinding.MapDraw;
import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.kurobako.gesturefx.GesturePane;

public class NodeMapController extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;

  @FXML private GesturePane gPane;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;
  private GraphicsContext gc;

  // scaling constant
  private double SCALE_FACTOR; // constant for map size/coordinate manipulation

  public void initialize() {
    NodeImpl node = new NodeImpl();
    allNodes = node.getNodeOnFloor("L1"); // get all nodes from Database
    // node.closeSession();
    // Add Image
    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/00_thelowerlevel1_unlabeled.png");

    SCALE_FACTOR = gPane.getCurrentScale();
    // Add nodes as circles
    gc = nodeMapCanvas.getGraphicsContext2D();
    MapDraw.drawNodes(gc, allNodes, SCALE_FACTOR);
  }

  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param pathName the path to the image to be added
   */
  private void addFloorMapImage(String pathName) {

    File file = new File(pathName);
    Image image = new Image(file.toURI().toString());
    nodeMapImage.setImage(image);
    Node node = nodeMapImage;
    System.out.println("here");
    this.gPane.setContent(node);
    this.gPane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    System.out.println("hello ");
  }
}
