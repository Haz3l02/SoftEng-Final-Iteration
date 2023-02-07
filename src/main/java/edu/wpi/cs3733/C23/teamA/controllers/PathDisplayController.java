package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Node;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class PathDisplayController {

  @FXML private Canvas mapCanvas; // to display the generated path
  @FXML private ImageView mapImage;

  // Lists of Nodes and Node Data
  private ArrayList<Node> allNodes;
  private GraphicsContext gc;
  private List<String> allNodeIDs; // List of all Node IDs in specific order
  private List<String> allLongNames; // List of corresponding long names in order

  // scaling constant
  private final double SCALE_FACTOR = 0.09; // constant for map size/coordinate manipulation

  // the PathfindingSystem which runs methods from classes in the pathfinding package
  private PathfindingSystem pathfindingSystem;

  public void initialize() throws SQLException {}
}
