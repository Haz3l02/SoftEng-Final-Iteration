package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.BFS;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BFSTest {

  // Test nodes
  final GraphNode a1 = new GraphNode("a1", 4, -3, "a1");
  final GraphNode a2 = new GraphNode("a2", 0, 5, "a2");
  final GraphNode a3 = new GraphNode("a3", -4, -3, "a3");
  final GraphNode a4 = new GraphNode("a4", -3, 4, "a4");
  final GraphNode a5 = new GraphNode("a5", 3, 4, "a5");

  static HashMap<String, GraphNode> testGraph = new HashMap<>();
  PathfindingSystem pathfindingSystem = new PathfindingSystem(new BFS());

  @BeforeEach
  public void init() {
    // Test Nodes
    testGraph.put("a1", a1);
    testGraph.put("a2", a2);
    testGraph.put("a3", a3);
    testGraph.put("a4", a4);
    testGraph.put("a5", a5);
  }

  @Test
  public void empty() {
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a2);

    assertEquals(null, path);
  }

  @Test
  public void empty2() {
    a2.addNeighbor(a2);
    a1.addNeighbor(a1);
    a2.addNeighbor(a1);
    a3.addNeighbor(a2);
    a2.addNeighbor(a3);
    a3.addNeighbor(a4);
    a4.addNeighbor(a2);
    a5.addNeighbor(a1);
    a1.addNeighbor(a5);

    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a2);

    assertEquals(null, path);
  }

  @Test
  public void sameNoPath() {
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a1);
    String[] correctPath = {"a1"};

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void simpleTest() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);

    String[] correctPath = {"a1", "a2"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a2);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void simpleTest2() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);

    String[] correctPath = {"a1", "a3"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a3);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void mediumTest1() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a1.addNeighbor(a4);
    a4.addNeighbor(a5);
    a3.addNeighbor(a4);

    String[] correctPath = {"a1", "a4", "a5"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a5);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void mediumTest2() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a1.addNeighbor(a4);
    a4.addNeighbor(a5);
    a3.addNeighbor(a4);

    String[] correctPath = {"a1", "a4", "a5"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a5);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void loopedTest1() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a2.addNeighbor(a1);
    a1.addNeighbor(a1);
    a2.addNeighbor(a3);
    a3.addNeighbor(a1);

    String[] correctPath = {"a1"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a1, a1);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void loopedTest2() {
    // connecting nodes to make sure it picks the quickest way to a2
    a3.addNeighbor(a2);
    a2.addNeighbor(a4);
    a4.addNeighbor(a3);

    String[] correctPath = {"a3"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a3, a3);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void loopedTest3() {
    // connecting nodes to make sure it picks the quickest way to a2
    a3.addNeighbor(a2);
    a3.addNeighbor(a4);
    a3.addNeighbor(a5);
    a2.addNeighbor(a4);
    a4.addNeighbor(a5);
    a5.addNeighbor(a3);
    a5.addNeighbor(a1);
    a5.addNeighbor(a4);
    a5.addNeighbor(a2);
    a1.addNeighbor(a5);
    a1.addNeighbor(a2);

    String[] correctPath = {"a3", "a5", "a1"};
    ArrayList<GraphNode> path = pathfindingSystem.runPathfinding(a3, a1);

    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }
}
