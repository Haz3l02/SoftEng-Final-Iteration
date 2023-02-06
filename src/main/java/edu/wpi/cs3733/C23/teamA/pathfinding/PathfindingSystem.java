package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

// Contains methods from AStar, DFS, PathInterpreter, CSVReader, and Graph, with a Graph object.
// NOTE: for JavaDoc comments, view the other classes. This is a Facade class.
public class PathfindingSystem {

  // attributes
  Graph graph;

  // constructor
  public PathfindingSystem() {
    graph = new Graph();
  }

  // Methods relating to multiple Static Classes
  // AStar
  public ArrayList<GraphNode> traverseAStar(GraphNode startNode, GraphNode endNode) {
    return AStar.traverse(startNode, endNode);
  }

  // DFS
  public ArrayList<GraphNode> traverseDFS(GraphNode startNode, GraphNode endNode) {
    return DFS.traverse(startNode, endNode);
  }

  // PathInterpreter
  public ArrayList<GraphNode> getPath(GraphNode startNode, GraphNode endNode) {
    return PathInterpreter.getPath(startNode, endNode);
  }

  public String generatePathString(ArrayList<GraphNode> path) {
    return PathInterpreter.generatePathString(path);
  }

  // MapDraw
  public void drawPath(GraphicsContext gc, ArrayList<GraphNode> path, double scaleFactor) {
    MapDraw.drawPath(gc, path, scaleFactor);
  }

  // Methods relating to Graph
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
