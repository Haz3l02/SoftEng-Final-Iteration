package edu.wpi.cs3733.C23.teamA.pathfinding.algorithms;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInterpreter;
import java.util.*;

/*
Class that runs an A star graph search algorithm
 */
public class AStar implements IAlgorithmStrategy {

  /**
   * Uses the A* algorithm to find the shortest path between startNode and endNode, with the
   * Euclidean distance between nodes used to track the distance from startNode and the estimated
   * heuristic distance to the endNode.
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return an ArrayList containing the nodes in the path from startNode to endNode, or null if
   *     there isn't one.
   */
  public ArrayList<GraphNode> traverse(GraphNode startNode, GraphNode endNode) {
    // initialize open and closed lists
    Queue<GraphNode> open = new PriorityQueue<>();

    // set the scoring values for the open list
    startNode.setCostFromStart(0.0); // g(x) = 0, since it's the start
    startNode.setHeurCostToEnd(getDirectDistance(startNode, endNode)); // h(x)
    startNode.setScore(
        startNode.getCostFromStart() + startNode.getHeurCostToEnd()); // f(x) = g(x) + h(x)

    // add the starting node to the open list
    open.add(startNode);

    // start loopin'
    while (!open.isEmpty()) {
      // get the node from the top of the priority queue
      GraphNode current = open.poll();

      // if the current node equals the end node, we're done!
      if (current.equals(endNode)) {
        return PathInterpreter.getPath(startNode, endNode);
      }

      // if we're not at the end, add current's children to the queue
      for (GraphNode n : current.getNeighbors()) {

        // compute g(x) for the successor
        double tentativeGScore = current.getCostFromStart() + getDirectDistance(current, n);

        //
        if (tentativeGScore < n.getCostFromStart()) {
          // set the parent to current
          n.setParent(current);

          // set attributes of n
          n.setCostFromStart(tentativeGScore);
          n.setHeurCostToEnd(getDirectDistance(n, endNode));
          n.setScore(n.getCostFromStart() + n.getHeurCostToEnd());

          // if n is not in the set, add it (can happen multiple times for a node)
          if (!open.contains(n)) {
            open.add(n);
          }
        }
      }
    }

    // if we get here, no path was found. Return null
    return null;
  }

  /**
   * Uses the Pythagorean Theorem to find the direct distance between two nodes. Used as a heuristic
   * for finding the shortest path between two nodes using A*.
   *
   * @param a The first node to find the direct distance between
   * @param b The second node to find the direct distance between
   * @return the direct distance between the two nodes, in the form of a singular line between them
   */
  private static double getDirectDistance(GraphNode a, GraphNode b) {
    // x and y coordinates of a
    int aX = a.getXCoord();
    int aY = a.getYCoord();

    // x and y coordinates of b
    int bX = b.getXCoord();
    int bY = b.getYCoord();

    // compute the direct distance between the nodes using the pythagorean theorem
    return Math.sqrt((Math.pow((bX - aX), 2)) + (Math.pow((bY - aY), 2)));
  }
}
