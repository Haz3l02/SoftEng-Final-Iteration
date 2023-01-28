package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.controllers.PathfindingController;
import edu.wpi.cs3733.C23.teamA.pathfinding.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.*; // notably, BeforeEach & Test

public class DFSTest {
  // test nodes
  final Node a1 = new Node("a1");
  final Node a2 = new Node("a2");
  final Node a3 = new Node("a3");
  final Node a4 = new Node("a4");
  final Node a5 = new Node("a5");
  final Node a6 = new Node("a6");
  static HashMap<String, Node> testGraph = new HashMap<>();

  // test graph from Hospital L1 data
  static HashMap<String, Node> graph = new HashMap<>();

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
    graph = PathfindingController.prepDFS();
  }

  @Test
  public void easyPath1() {
    // connecting nodes
    a1.addNeighbor(a2);

    String[] correctPath = {"a1", "a2"};
    ArrayList<Node> path = PathfindingController.callDFS(testGraph, "a1", "a2");
    for (int i = 0; i < path.size(); i++) {
      Node current = path.get(i);
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
    ArrayList<Node> path = PathfindingController.callDFS(testGraph, "a1", "a5");
    for (int i = 0; i < path.size(); i++) {
      Node current = path.get(i);
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
    ArrayList<Node> path = PathfindingController.callDFS(testGraph, "a1", "a4");
    for (int i = 0; i < path.size(); i++) {
      Node current = path.get(i);
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

    ArrayList<Node> shouldBeNull = PathfindingController.callDFS(testGraph, "a1", "a6");
    assertTrue(shouldBeNull == null);
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
    ArrayList<Node> path = PathfindingController.callDFS(testGraph, "a1", "a6");
    for (int i = 0; i < path.size(); i++) {
      Node current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void loopedPath2() {
    a5.addNeighbor(a2);
    a2.addNeighbor(a5);

    ArrayList<Node> shouldBeNull = PathfindingController.callDFS(testGraph, "a5", "a6");
    assertTrue(shouldBeNull == null);
  }

  @Test
  public void loopedPath3() {
    a1.addNeighbor(a4);
    a4.addNeighbor(a5);
    a5.addNeighbor(a2);
    a5.addNeighbor(a6);
    a2.addNeighbor(a1);

    String[] correctPath = {"a2", "a1", "a4", "a5", "a6"};
    ArrayList<Node> path = PathfindingController.callDFS(testGraph, "a2", "a6");
    for (int i = 0; i < path.size(); i++) {
      Node current = path.get(i);
      assertTrue(current.getNodeID().equals(correctPath[i]));
    }
  }

  @Test
  public void roomToVendingPath() throws IOException {
    // call to readNodes and readEdges method to add in all the nodes and edges
    ArrayList<Node> roomVending = PathfindingController.callDFS(graph, "CHALL003L1", "CHALL007L1");
    assertTrue(roomVending != null);
  }

  @Test
  public void easyHospitalPath() throws IOException {
    // going from L1 Nuclear Medicine (CLABS003L1) to L1 Ultrasound (CLABS004L1)
    // these rooms are literally right across a hallaway from eachother. They're connected in
    // L1Edges.csv.
    // If it didn't work, that would be BAD.
    ArrayList<Node> questionable = PathfindingController.callDFS(graph, "CLABS003L1", "CLABS004L1");
    assertTrue(questionable != null);
  }

  @Test
  public void questionableHospitalPath() throws IOException {
    // going from L1 Anesthesia Conference Room (CCONF001L1) to L1 Abrams Conference Room
    // (CCONF003L1)
    ArrayList<Node> questionable = PathfindingController.callDFS(graph, "CCONF001L1", "CCONF003L1");
    assertTrue(questionable != null);
  }

  @Test
  public void shapiroPath() throws IOException {
    // going from L1 Shapiro Elevator (GELEV00QL1) to L1 Day Surgery Family Waiting (CDEPT002L1)
    // these aren't connected on L1 (not directly, at least?) so there shouldn't be a path returned.
    ArrayList<Node> shouldBeEmpty =
        PathfindingController.callDFS(graph, "GELEV00QL1", "CDEPT002L1");
    assertTrue(shouldBeEmpty == null);
  }
}
