package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.IAlgorithmStrategy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
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
  public ArrayList<GraphNode> runPathfinding(GraphNode startNode, GraphNode endNode) {
    return algorithmStrategy.traverse(startNode, endNode);
  }

  // PathInterpreter
  public String generatePathString(ArrayList<GraphNode> path) {
    return PathInterpreter.generatePathString(path);
  }

  // MapDraw
  public void drawPath(GraphicsContext[] gcs, ArrayList<GraphNode> path, double scaleFactor) {
    MapDraw.drawPath(gcs, path, scaleFactor);
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

  public void prepGraphDB() throws SQLException {
    graph.prepGraphDB();
  }

  public void prepGraphCSV() throws IOException {
    graph.prepGraphCSV();
  }

  public void reset() {
    graph.reset();
  }
}
