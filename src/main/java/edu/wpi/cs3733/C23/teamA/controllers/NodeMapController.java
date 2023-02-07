package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Node;
import edu.wpi.cs3733.C23.teamA.pathfinding.MapDraw;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NodeMapController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;

  // Lists of Nodes and Node Data
  private ArrayList<Node> allNodes;
  private GraphicsContext gc;

  // scaling constant
  private final double SCALE_FACTOR = 0.09; // constant for map size/coordinate manipulation

  public void initialize() {
    try {
      allNodes = Node.getAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // Add Image
    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/unlabeledMaps/25% Scale/00_thelowerlevel1_unlabeled_25%.png");

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
