package edu.wpi.cs3733.C23.teamA.pathfinding.algorithms;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInterpreter;
import java.util.LinkedList;
import java.util.Queue;

public class BFS implements IAlgorithmStrategy {

  /**
   * Uses Breadth-First Search to find a path between two nodes, if one exists. Implements a FIFO
   * queue.
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return a PathInfo object containing an ArrayList containing the nodes in the path from
   *     startNode to endNode, or null if there is no path.
   */
  public PathInfo traverse(GraphNode startNode, GraphNode endNode) {

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

  /**
   * Uses Breadth-First Search to find a path between two nodes, if one exists. Implements a FIFO
   * queue. Avoids nodes that have a stair ("STAI") location tied to them.
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return a PathInfo object containing an ArrayList containing the nodes in the path from
   *     startNode to endNode, or null if there is no path.
   */
  public PathInfo traverseNoStairs(GraphNode startNode, GraphNode endNode) {

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
        // For every unmarked adjacent vertex:
        if (!neighbor.isVisited() && !neighbor.getNodeType().equals("STAI")) {
          neighbor.setParent(node); // save last edge on a shortest path,
          neighbor.setVisited(true); // mark it because path is known,
          queue.add(neighbor); // and add it to the queue
        }
    }
    return null;
  }
}
