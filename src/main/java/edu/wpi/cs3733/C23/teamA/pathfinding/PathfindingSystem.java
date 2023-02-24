package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.IAlgorithmStrategy;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

// Contains methods from AStar, DFS, PathInterpreter, CSVReader, and Graph, with a Graph object.
// NOTE: for JavaDoc comments, view the other classes. This is a Facade class.
public class PathfindingSystem {

  // attributes
  Graph graph;
  @Setter @Getter IAlgorithmStrategy algorithmStrategy;

  // constructor
  public PathfindingSystem(IAlgorithmStrategy algorithmStrategy) {
    this.algorithmStrategy = algorithmStrategy; // AStar, DFS, or BFS
    graph = new Graph();
  }

  // run pathfinding using the System
  public PathInfo runPathfinding(GraphNode startNode, GraphNode endNode) {
    return algorithmStrategy.traverse(startNode, endNode);
  }

  // run pathfinding using the System (no stairs)
  public PathInfo runPathfindingNoStairs(GraphNode startNode, GraphNode endNode) {
    return algorithmStrategy.traverseNoStairs(startNode, endNode);
  }

  // PathInterpreter
  public String generatePathString(ArrayList<GraphNode> path, ArrayList<String> floorPath) {
    return PathInterpreter.generatePathString(path, floorPath);
  }

  // MapDraw
  public void drawPath(AnchorPane[] aps, ArrayList<GraphNode> path, ArrayList<String> floorPath) {
    MapDraw.drawPathClickable(aps, path, floorPath);
  }

  // Methods relating to Graph
  public void getGraph() {
    graph.getGraph();
  }

  public void addNode(String nodeID, GraphNode node) {
    graph.addNode(nodeID, node);
  }

  public GraphNode getNode(String key) {
    return graph.getNode(key);
  }

  public void prepGraphDB(LocalDate navDate) throws SQLException {
    graph.prepGraphDB(navDate);
  }

  public void prepGraphCSV() throws IOException {
    graph.prepGraphCSV();
  }

  public void reset() {
    graph.reset();
  }
}
