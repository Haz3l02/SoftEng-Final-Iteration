package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

public class PathInterpreter {
  /**
   * After a search has successfully been executed, this method is called to get the path between
   * startNode and endNode.
   *
   * @param startNode Node that the search begins at
   * @param endNode Node that the search ends at or looks for
   * @return the path from the starting node to the ending node
   */
  public static ArrayList<GraphNode> getPath(GraphNode startNode, GraphNode endNode) {
    // initialize the array which will contain all the Nodes in the path and add the end node
    ArrayList<GraphNode> path = new ArrayList<>();

    // Check if startNode and endNode are the same. If they are, return the path with one Node
    if (startNode.equals(endNode)) {
      path.add(startNode);
      return path;
    }

    // Check if endNode even has a parent. If not, something is definitely wrong; throw an error.
    if (endNode.getParent() == null) {
      throw new NullPointerException(
          "Error: The ending node (endNode) specified has no parent, so it is not reachable.");
    }

    // add the ending node to the path, which exists
    path.add(endNode);
    // create a tracker "node" to go through the path created by parent relationships
    GraphNode tracker = endNode;

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

  /**
   * Creates a string representation of the path specified by the parameter path. Shows each node
   * with their ID and long name. Implies that the path being given is not null or empty.
   *
   * @param path An ArrayList which represents a path from the node in index zero to the node in the
   *     last index
   */
  public static String generatePathString(ArrayList<GraphNode> path) {
    // get the first and last node names to print
    String startID = path.get(0).getNodeID();
    String endID = path.get(path.size() - 1).getNodeID();

    // make a stringBuilder object to get a giant string
    StringBuilder sb = new StringBuilder();

    sb.append("Path from " + startID + " to " + endID + ":\n");

    // loop through all of them to print the full path
    for (GraphNode n : path) {
      sb.append("Node ID: " + n.getNodeID());
      if (n.getLongName() != null) {
        sb.append("; Long Name: " + n.getLongName() + "\n");
      }
    }

    // return the built string
    return sb.toString();
  }
}
