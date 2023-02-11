package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS implements IAlgorithmStrategy {

  public ArrayList<GraphNode> traverse(GraphNode startNode, GraphNode endNode) {

    Queue<GraphNode> queue = new LinkedList<>();
    startNode.setVisited(true); // Mark the source
    queue.add(startNode); // put source on the queue
    while (!queue.isEmpty()) {
      GraphNode node = queue.poll(); // Remove next vertex from the queue
      if (node.equals(endNode)) {
        // add a return statement here: we found it!
        return PathInterpreter.getPath(startNode, endNode);
      }

      for (GraphNode neighbor : node.getNeighbors())
        if (!neighbor.isVisited()) { // For every unmarked adjacent vertex,
          neighbor.setParent(node); // save last edge on a shortest path,
          neighbor.setVisited(true); // mark it because path is known,
          queue.add(neighbor); // and add it to the queue
        }
    }
    return null;
  }
}
