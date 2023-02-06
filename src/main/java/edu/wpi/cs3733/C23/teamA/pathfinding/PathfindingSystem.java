package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.canvas.GraphicsContext;

// Contains methods from AStar, DFS, PathInterpreter, and CSVReader
public class PathfindingSystem {

  private AStar astar;
  private CSVReader csvreader;
  private DFS dfs;
  private MapDraw mapdraw;
  private PathInterpreter pathinterpreter;

  public ArrayList<GraphNode> traverseAStar(GraphNode startNode, GraphNode endNode) {
    return AStar.traverse(startNode, endNode);
  }

  public ArrayList<GraphNode> traverseDFS(GraphNode startNode, GraphNode endNode) {
    return DFS.traverse(startNode, endNode);
  }

  public ArrayList<GraphNode> getPath(GraphNode startNode, GraphNode endNode) {
    return PathInterpreter.getPath(startNode, endNode);
  }

  public String generatePathString(ArrayList<GraphNode> path) {
    return PathInterpreter.generatePathString(path);
  }

  public void readCSV(String pathForNodes, String pathForEdges, HashMap<String, GraphNode> graph)
      throws IOException {
    CSVReader.readNodes(pathForNodes, graph);
    CSVReader.readEdges(pathForEdges, graph);
  }

  public void drawPath(GraphicsContext gc, ArrayList<GraphNode> path, double scaleFactor) {
    MapDraw.drawPath(gc, path, scaleFactor);
  }
}
