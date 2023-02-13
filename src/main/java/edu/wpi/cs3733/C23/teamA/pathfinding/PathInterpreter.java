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
    String startName = path.get(0).getLongName();
    String endName = path.get(path.size() - 1).getLongName();

    // make a stringBuilder object to get a giant string
    StringBuilder sb = new StringBuilder();

    sb.append("Path from " + startName + " to " + endName + ":\n\n");

    int numNodes = path.size();

    // loop through all of them to print the full path
    for (int i = 0; i < numNodes; i++) {
      String longName = path.get(i).getLongName();

      if (longName != null) {
        if (i == 0) {
          sb.append("Start at " + longName + ".\n");
        } else if (i == 1) {
          sb.append("Go toward " + longName + ".\n");
        } else {
          String direction = getDirection(path.get(i - 2), path.get(i - 1), path.get(i));
          sb.append(direction);
        }
      }
    }
    sb.append("You have reached " + endName + ".\n");

    // return the built string
    return sb.toString();
  }

  public static String getDirection(GraphNode start, GraphNode middle, GraphNode end) {
    String direction;

    int ax = start.getXCoord();
    int ay = start.getYCoord();
    int bx = middle.getXCoord();
    int by = middle.getYCoord();
    int cx = end.getXCoord();
    int cy = end.getYCoord();

    // todo: add some kind of check for the same coordinates; if any pair has the same ones?

    // distance between the first and second graphNodes
    int dist = (int) Math.sqrt(Math.pow(bx - ax, 2) + Math.pow(by - ay, 2)) / 3;
    // angle of direction between the first and second graphNodes
    double angle1 = Math.atan2((by - ay), (bx - ax));
    // angle of direction between the first and third graphNodes
    double angle2 = Math.atan2((cy - ay), (cx - ax));
    double changeInAngle = angle2 - angle1;
    double epsilon = 0.1;

    if (Math.PI - epsilon > changeInAngle && changeInAngle > epsilon) {
      direction = "Turn left ";
    } else if (-Math.PI - epsilon > changeInAngle) {
      direction = "Turn left ";
    } else if (changeInAngle >= -epsilon && changeInAngle <= epsilon) {
      direction = "Continue straight ";
    } else if (-Math.PI + epsilon < changeInAngle && changeInAngle < -epsilon) {
      direction = "Turn right ";
    } else if (Math.PI + epsilon < changeInAngle) {
      direction = "Turn right ";
    } else { // Dir = pi or -pi
      direction = "Turn all of the way around ";
    }
    direction = direction + "and go to " + end.getLongName() + ".\n";

    return direction;
  }
}
