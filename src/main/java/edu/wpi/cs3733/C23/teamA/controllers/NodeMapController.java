package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.MapDraw;
import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.hibernate.Session;

public class NodeMapController extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;
  private GraphicsContext gc;

  // scaling constant
  private final double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulation

  public void initialize() {
    Session session = getSessionFactory().openSession();
    allNodes = NodeEntity.getNodeOnFloor("L1", session); // get all nodes from Database
    session.close();
    // Add Image
    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/25% Scale/00_thelowerlevel1_unlabeled_25%.png");

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
  }
}
