package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.DFS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.*; // notably, BeforeEach & Test

public class DFSTest {
  // test nodes
  final GraphNode a1 = new GraphNode("a1", 0, 0, "a1", "UNKN");
  final GraphNode a2 = new GraphNode("a2", 0, 0, "a2", "UNKN");
  final GraphNode a3 = new GraphNode("a3", 0, 0, "a3", "UNKN");
  final GraphNode a4 = new GraphNode("a4", 0, 0, "a4", "UNKN");
  final GraphNode a5 = new GraphNode("a5", 0, 0, "a5", "UNKN");
  final GraphNode a6 = new GraphNode("a6", 0, 0, "a6", "UNKN");
  static HashMap<String, GraphNode> testGraph = new HashMap<>();

  // test graph from Hospital L1 data
  PathfindingSystem pathfindingSystem = new PathfindingSystem(new DFS());
  ArrayList<GraphNode> path;
  PathInfo pathInfo;

  @BeforeEach
  public void init() throws IOException {
    // Test Nodes
    testGraph.put("a1", a1);
    testGraph.put("a2", a2);
    testGraph.put("a3", a3);
    testGraph.put("a4", a4);
    testGraph.put("a5", a5);
    testGraph.put("a6", a6);

    // Hospital L1 Graph
    pathfindingSystem.prepGraphCSV();
  }

  @Test
  public void easyPath1() {
    // connecting nodes
    a1.addNeighbor(a2);

    String[] correctPath = {"a1", "a2"};
    pathInfo = pathfindingSystem.runPathfinding(a1, a2);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void easyPath2() {
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a1.addNeighbor(a4);
    a3.addNeighbor(a5);

    String[] correctPath = {"a1", "a3", "a5"};
    pathInfo = pathfindingSystem.runPathfinding(a1, a5);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void mediumPath1() {
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a3.addNeighbor(a5);
    a5.addNeighbor(a6);
    a2.addNeighbor(a4);
    a3.addNeighbor(a6);

    String[] correctPath = {"a1", "a2", "a4"};
    pathInfo = pathfindingSystem.runPathfinding(a1, a4);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void mediumPath2() {
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a3.addNeighbor(a5);
    a5.addNeighbor(a1);
    a2.addNeighbor(a4);
    a3.addNeighbor(a4);

    pathInfo = pathfindingSystem.runPathfinding(a1, a6);
    assertTrue(pathInfo == null);
  }

  @Test
  public void loopedPath1() {
    a1.addNeighbor(a2);
    a2.addNeighbor(a1);
    a4.addNeighbor(a5);
    a5.addNeighbor(a6);
    a2.addNeighbor(a4);
    a3.addNeighbor(a6);

    String[] correctPath = {"a1", "a2", "a4", "a5", "a6"};
    pathInfo = pathfindingSystem.runPathfinding(a1, a6);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void loopedPath2() {
    a5.addNeighbor(a2);
    a2.addNeighbor(a5);

    pathInfo = pathfindingSystem.runPathfinding(a5, a6);
    assertTrue(pathInfo == null);
  }

  @Test
  public void loopedPath3() {
    a1.addNeighbor(a4);
    a4.addNeighbor(a5);
    a5.addNeighbor(a2);
    a5.addNeighbor(a6);
    a2.addNeighbor(a1);

    String[] correctPath = {"a2", "a1", "a4", "a5", "a6"};
    pathInfo = pathfindingSystem.runPathfinding(a2, a6);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  /* todo write database tests in a new file - bEcAuSe CoDe CoVeRaGe
  @Test
  public void roomToVendingPath() throws IOException {
    // call to readNodes and readEdges method to add in all the nodes and edges
    GraphNode start = pathfindingSystem.getNode("CHALL003L1");
    GraphNode end = pathfindingSystem.getNode("CHALL007L1");

    pathInfo = pathfindingSystem.runPathfinding(start, end);
    path = pathInfo.getPath();
    assertTrue(path != null);
  }

  @Test
  public void easyHospitalPath() throws IOException {
    // going from L1 Nuclear Medicine (CLABS003L1) to L1 Ultrasound (CLABS004L1)
    // these rooms are literally right across a hallaway from eachother.
    // If it didn't work, that would be BAD.
    GraphNode start = pathfindingSystem.getNode("CLABS003L1");
    GraphNode end = pathfindingSystem.getNode("CLABS004L1");
    pathInfo = pathfindingSystem.runPathfinding(start, end);
    path = pathInfo.getPath();
    assertTrue(path != null);
  }

  @Test
  public void questionableHospitalPath() throws IOException {
    // going from L1 Anesthesia Conference Room (CCONF001L1) to L1 Abrams Conference Room
    // (CCONF003L1)
    GraphNode start = pathfindingSystem.getNode("CCONF001L1");
    GraphNode end = pathfindingSystem.getNode("CCONF003L1");
    pathInfo = pathfindingSystem.runPathfinding(start, end);
    path = pathInfo.getPath();
    assertTrue(path != null);
  }

  @Test
  public void sameStartAndEnd1() {
    // some random neighbors, loop with self
    a2.addNeighbor(a1);
    a1.addNeighbor(a5);
    a1.addNeighbor(a1);

    String[] correctPath = {"a1"};
    pathInfo = pathfindingSystem.runPathfinding(a1, a1);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }
   */

  @Test
  public void sameStartAndEnd2() {
    // no neighbors

    String[] correctPath = {"a4"};
    pathInfo = pathfindingSystem.runPathfinding(a4, a4);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void sameStartAndEnd3() {
    // small loop between a1 and a2
    a2.addNeighbor(a1);
    a1.addNeighbor(a2);

    String[] correctPath = {"a2"};
    pathInfo = pathfindingSystem.runPathfinding(a2, a2);
    path = pathInfo.getPath();

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }
}
