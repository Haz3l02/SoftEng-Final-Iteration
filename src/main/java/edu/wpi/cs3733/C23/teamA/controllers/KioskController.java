package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.IAlgorithmStrategy;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Algorithm;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class KioskController {
  @FXML Label announcement, left, right;

  @FXML
  public void initialize() throws SQLException {
    announcement.setText(kiosk.getMessage());
    left.setText(kiosk.getLeft());
    right.setText(kiosk.getRight());

    // TODO Setup the split pane thing and the map
    runPathfinding();
  }

  private void runPathfinding() throws SQLException {
    IAlgorithmStrategy astar = new AStar();
    PathfindingSystem pathfindingSystem = new PathfindingSystem(astar);
    pathfindingSystem.prepGraphDB(LocalDate.now());
    GraphNode start = pathfindingSystem.getNode(kiosk.getStartLocation().getNodeid());
    GraphNode end = pathfindingSystem.getNode(kiosk.getEndLocation().getNodeid());
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(start, end).getPath();



  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }
}
