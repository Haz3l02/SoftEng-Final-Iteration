package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.controllers.PathfindingController;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
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

    // Hospital L1 Graph
    graphL1 = PathfindingController.prepGraph();
  }

  @Test
  public void shortestPath1() {
    // connecting nodes
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a3.addNeighbor(a2);

    String[] correctPath = {"a1", "a2"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a2");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void shortestPath2() {
    // connecting nodes
    a1.addNeighbor(a2);
    a1.addNeighbor(a3);
    a2.addNeighbor(a3);
    a3.addNeighbor(a2);

    String[] correctPath = {"a1", "a3"};
    ArrayList<GraphNode> path = PathfindingController.callAStar(testGraph, "a1", "a3");
    for (int i = 0; i < path.size(); i++) {
      GraphNode current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void shortestPath3() {
    // connecting nodes
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
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void shortestPath4() {
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
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void shortestPath4Reverse() {
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
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }
}
