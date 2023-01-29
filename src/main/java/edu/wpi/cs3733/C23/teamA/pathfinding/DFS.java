package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class DFS {

  /**
   * The traverse method uses a Depth First Search Algorithm to go from the startNode to the endNode
   * by using a LIFO stack
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return an ArrayList containing the nodes in the path from startNode to endNode, or null if
   *     there isn't one.
   */
  public static ArrayList<Node> traverse(Node startNode, Node endNode) {
    // first, check if the starting and ending nodes are the same

    // initialize the stack and add the starting node to it
    Deque<Node> stack = new LinkedList<>();
    stack.push(startNode);

    while (!stack.isEmpty()) {
      Node current = stack.pop();
      if (current == endNode) {
        // add a return statement here: we found it!
        return PathInterpreter.getPath(startNode, endNode);
      }

      if (!current.isVisited()) {
        // System.out.println(current.getNodeID());
        current.setVisited(true);
        for (Node n : current.getNeighbors()) {
          stack.push(n); // add edges to the stack
          if (n.getParent() == null) {
            // set the parent for each node in neighbors that doesn't already have a parent
            // IMPORTANT: THIS OPERATES UNDER THE ASSUMPTION THAT A NEW HASHMAP IS MADE EVERY TIME;
            // OTHERWISE, FIND A WAY TO RESET THE PARENT FIELD EVERY TIME
            n.setParent(current);
          }
        }
      }
    }

    // return null - no path can be found
    return null;
  }
}
