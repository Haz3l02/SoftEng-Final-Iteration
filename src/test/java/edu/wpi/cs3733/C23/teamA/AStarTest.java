package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.controllers.PathfindingController;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInterpreter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.*; // notably, BeforeEach & Test

public class AStarTest {

  // test nodes
  final GraphNode a1 = new GraphNode("a1", 4, -3);
  final GraphNode a2 = new GraphNode("a2", 0, 5);
  final GraphNode a3 = new GraphNode("a3", -4, -3);
  final GraphNode a4 = new GraphNode("a4", -3, 4);
  final GraphNode a5 = new GraphNode("a5", 3, 4);
  static HashMap<String, GraphNode> testGraph = new HashMap<>();

  final GraphNode b1 = new GraphNode("b1", 0, 0);
  final GraphNode b2 = new GraphNode("b2", -1, 3);
  final GraphNode b3 = new GraphNode("b3", 1, 2);
  final GraphNode b4 = new GraphNode("b4", 0, 4);
  static HashMap<String, GraphNode> testGraph2 = new HashMap<>();

  // test graph from Hospital L1 data
  static HashMap<String, GraphNode> graphL1 = new HashMap<>();

  @BeforeEach
  public void init() throws IOException {
    // Test Nodes
    testGraph.put("a1", a1);
    testGraph.put("a2", a2);
    testGraph.put("a3", a3);
    testGraph.put("a4", a4);
    testGraph.put("a5", a5);

    testGraph2.put("b1", b1);
    testGraph2.put("b2", b2);
    testGraph2.put("b3", b3);
    testGraph2.put("b4", b4);

    // Hospital L1 Graph
    graphL1 = PathfindingController.prepGraphCSV();
  }

  @Test
  public void shortestPath1() {
    // connecting nodes to make sure it picks the quickest way to a2
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a3.addNeighbor(a2);

    String[] correctPath = {"a1", "a2"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a2");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestPath2() {
    // same connecting nodes making sure it picks the quickest way to a3
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a3.addNeighbor(a2);

    String[] correctPath = {"a1", "a3"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a3");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestPath3() {
    // connecting nodes with multiple existing paths, one being shorter
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a3.addNeighbor(a2);
    a2.addNeighbor(a5);
    a3.addNeighbor(a5);
    a3.addNeighbor(a4);
    a5.addNeighbor(a4);

    String[] correctPath = {"a1", "a3", "a4"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a4");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestNumberPath() {
    // connecting nodes
    a1.addNeighbor(a2);
    a2.addNeighbor(a3);
    a3.addNeighbor(a4);
    a4.addNeighbor(a5);
    a5.addNeighbor(a1);
    a1.addNeighbor(a5);
    a2.addNeighbor(a1);
    a3.addNeighbor(a2);
    a4.addNeighbor(a3);
    a5.addNeighbor(a4);

    String[] correctPath = {"a2", "a1", "a5"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a2", "a5");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestNumberPathReverse() {
    // connecting nodes entered in reverse order to assure no bias in order
    a5.addNeighbor(a4);
    a4.addNeighbor(a3);
    a3.addNeighbor(a2);
    a2.addNeighbor(a1);
    a1.addNeighbor(a5);
    a5.addNeighbor(a1);
    a4.addNeighbor(a5);
    a3.addNeighbor(a4);
    a2.addNeighbor(a3);
    a1.addNeighbor(a2);

    String[] correctPath = {"a2", "a1", "a5"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a2", "a5");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void sameStartAndEnd1() {
    // small cycle
    a1.addNeighbor(a5);
    a5.addNeighbor(a1);

    String[] correctPath = {"a1"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a1");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void sameStartAndEnd2() {
    // no neighboring nodes

    String[] correctPath = {"a1"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a1");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void sameStartAndEnd3() {
    // random nodes, with self loop
    a2.addNeighbor(a1);
    a1.addNeighbor(a5);
    a1.addNeighbor(a1);

    String[] correctPath = {"a1"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a1");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestPath4() {
    // random nodes, with self loop
    b1.addNeighbor(b2);
    b1.addNeighbor(b3);
    b2.addNeighbor(b4);
    b3.addNeighbor(b4);
    b4.addNeighbor(b3);
    b4.addNeighbor(b2);
    b2.addNeighbor(b1);
    b3.addNeighbor(b1);

    String[] correctPath = {"b1", "b3", "b4"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph2, "b1", "b4");
    System.out.println(PathInterpreter.generatePathString(path));
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }

  @Test
  public void shortestPath4Backwards() {
    // random nodes, with self loop
    b1.addNeighbor(b2);
    b1.addNeighbor(b3);
    b2.addNeighbor(b4);
    b3.addNeighbor(b4);
    b4.addNeighbor(b3);
    b4.addNeighbor(b2);
    b2.addNeighbor(b1);
    b3.addNeighbor(b1);

    String[] correctPath = {"b4", "b3", "b1"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph2, "b4", "b1");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertEquals(current.getNodeID(), correctPath[i]);
    }
  }
}