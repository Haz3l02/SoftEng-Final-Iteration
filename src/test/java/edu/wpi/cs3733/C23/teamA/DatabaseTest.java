package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.databases.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import java.sql.SQLException;
import org.junit.jupiter.api.*; // notably, BeforeEach & Test

// Testing class for database connection with PathfindingController
public class DatabaseTest {

  private PathfindingSystem pathfindingSystem = new PathfindingSystem();

  @BeforeEach
  public void init() throws SQLException {
    pathfindingSystem.prepGraphDB();
  }

  // DATABASE STUFF WHICH IS MAYBE OUTDATED
  /*
  @Test
  public void testPrint() throws SQLException, ClassNotFoundException {
    new Adb();
    System.out.println(Node.getAll());
    System.out.println(Move.getAll());
    System.out.println(LocationName.getAll());
    System.out.println(Edge.getAll());
    System.out.println(Move.mostRecentLoc("L1X2665Y1043"));
  }

  @Test // Tests that the proper edges from database are added to the graph
  public void testGraphMakerEdge1() {
    ArrayList<GraphNode> neighbors = graph.get("L1X2065Y1284").getNeighbors();
    boolean correct =
        (neighbors.get(0).getNodeID().equals("L1X1965Y1284")
                || neighbors.get(0).getNodeID().equals("L1X0555Y1284"))
            && (neighbors.get(1).getNodeID().equals("L1X1965Y1284")
                || neighbors.get(1).getNodeID().equals("L1X0555Y1284"));

    assertTrue(correct);
  }

  @Test
  public void testGraphMakerEdge2() {
    ArrayList<GraphNode> neighbors = graph.get("L1X2490Y1043").getNeighbors();
    boolean correct =
        (neighbors.get(0).getNodeID().equals("L1X0666Y1043")
                || neighbors.get(0).getNodeID().equals("L1X0555Y0066"))
            && (neighbors.get(1).getNodeID().equals("L1X0666Y1043")
                || neighbors.get(1).getNodeID().equals("L1X0555Y0066"));

    assertTrue(correct);
  }

  @Test // Tests that proper node info from database (xcoord and ycoord) are added to the graph
  public void testGraphMakerNode1() throws SQLException {
    HashMap<String, GraphNode> graph = PathfindingMapController.prepGraphDB();
    boolean all1 = false;
    boolean all2 = false;

    if (graph.get("L1X0056Y1245").getXCoord() == 56
        && graph.get("L1X0056Y1245").getYCoord() == 1245) {
      all1 = true;
    }
    if (graph.get("L1X2770Y1284").getXCoord() == 2770
        && graph.get("L1X2770Y1284").getYCoord() == 1284) {
      all2 = true;
    }

    assertTrue(all1 & all2);
  }
   */
}
