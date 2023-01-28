package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

public class DFS {

  /**
   * The traverse method uses a Depth First Search Algorithm to go from the startNode to the endNode
   * by using a LIFO stack
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return a boolean representing whether a path exists from startNode to endNode
   */
  public static ArrayList<Node> traverse(Node startNode, Node endNode) {
    // initialize the stack and add the starting node to it
    Deque<Node> stack = new LinkedList<>();
    stack.push(startNode);

    while (!stack.isEmpty()) {
      Node current = stack.pop();
      if (current == endNode) {
        // add a return statement here: we found it!
        return getPath(startNode, endNode);
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

  /**
   * After DFS has successfully been executed, this method is called to get the path between
   * startNode and endNode.
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return the path from the starting node to the ending node
   */
  private static ArrayList<Node> getPath(Node startNode, Node endNode) {
    // initialize the ArrayList which will contain all the Nodes present in the path, and add the
    // ending node to it
    ArrayList<Node> path = new ArrayList<>();

    // first, check if endNode even has a parent. If not, something is definitely wrong; throw an
    // error
    if (endNode.getParent() == null) {
      throw new NullPointerException(
          "Error: The ending node (endNode) specified has no parent, so it is not reachable.");
    }

    // add the ending node to the path, which exists
    path.add(endNode);
    // create a tracker "node" to go through the path created by parent relationships
    Node tracker = endNode;

    // loop until the startNode is found as a parent
    while (!tracker.getParent().equals(startNode)) {
      tracker = tracker.getParent();
      path.add(tracker);
    }

    // add the start node to the list
    path.add(startNode);

    // reverse the path (because it is currently backwards) and return it
    Collections.reverse(path);
    return path;
  }
}
