package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.*;

/*
Class to be made in Prototype 2 to run an A star graph search algorithm
 */
public class AStar {
  // attributes: ???

  /**
   * Uses the Pythagorean Theorem to find the direct distance between two nodes. Used as a heuristic
   * for finding the shortest path between two nodes using A*.
   *
   * @param a The first node to find the direct distance between
   * @param b The second node to find the direct distance between
   * @return the direct distance between the two nodes, in the form of a singular line between them
   */
  public static double pythagThrm(Node a, Node b) {
    // x and y coordinates of a
    int aX = a.getXCoord();
    int aY = a.getYCoord();

    // x and y coordinates of b
    int bX = b.getXCoord();
    int bY = b.getYCoord();

    // compute the direct distance between the nodes using the pythagorean theorem
    return Math.sqrt((Math.pow((bX - aX), 2)) + (Math.pow((bY - aY), 2)));
  }

  public static ArrayList<Node> aStarTraverse(Node startNode, Node endNode) {
    // initialize open and closed lists
    Deque<Node> open = new LinkedList<>();
    Deque<Node> closed = new LinkedList<>();

    // add the starting node to the open list
    open.push(startNode);

    // add the starting node to the open list
    open.add(startNode);

    // f(x) = g(x) + h(x), where g(x) is the distance from the start and h(x) is the heuristic
    // distance from the end

    // to get IntelliJ to stop yelling at me :)
    ArrayList<Node> test = new ArrayList<>();
    return test;
  }
}
