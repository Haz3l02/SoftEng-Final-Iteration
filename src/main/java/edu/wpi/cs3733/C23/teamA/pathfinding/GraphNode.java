package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class GraphNode implements Comparable<GraphNode> {

  // attributes from the .csv file
  @Getter private String nodeID; // unique string id of the node
  @Getter @Setter private int xCoord; // x coordinate of where it is located
  @Getter @Setter private int yCoord; // y coordinate of where it is located
  @Getter @Setter private String floor; // floor identifier that it is on
  private String building; // name of building it is in
  private String nodeType; // type of node (ie hallway, restroom, etc)
  @Getter private String longName; // name of node
  private String shortName; // shortened name of node

  // attributes for graph searching
  @Getter @Setter private boolean visited; // where it has been visited in a search

  @Getter @Setter
  private GraphNode parent; // the node it was first accessed from (used for backtracking)

  @Getter private ArrayList<GraphNode> neighbors; // nodes that have edges with this node

  // attributes specifically for A* searching
  @Getter @Setter private double costFromStart; // g(x)
  @Getter @Setter private double heurCostToEnd; // h(x)

  @Getter @Setter
  private double score; // score (f(x)) = costFromStart (g(x)) + heurCostToEnd (h(x))

  @Getter private double penalty; // distance penalty to encourage/discourage A*

  /**
   * @param nodeID
   * @param xCoord
   * @param yCoord
   * @param longName
   */
  public GraphNode(String nodeID, int xCoord, int yCoord, String longName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.longName = longName;
    visited = false;
    parent = null;
    neighbors = new ArrayList<GraphNode>();
    floor = "";

    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;
    penalty = 0;
  }

  /**
   * @param nodeID
   * @param xCoord
   * @param yCoord
   * @param longName
   * @param nodeType
   */
  public GraphNode(String nodeID, int xCoord, int yCoord, String longName, String nodeType) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.longName = longName;
    this.nodeType = nodeType;

    visited = false;
    parent = null;
    neighbors = new ArrayList<GraphNode>();
    floor = "";

    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;

    // set the penalty based on the nodeType
    penalty = 0;
  }

  /**
   * @param nodeID
   * @param xCoord
   * @param yCoord
   * @param longName
   * @param nodeType
   * @param floor
   */
  public GraphNode(
      String nodeID, int xCoord, int yCoord, String longName, String nodeType, String floor) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.longName = longName;
    this.nodeType = nodeType;
    this.floor = floor;

    visited = false;
    parent = null;
    neighbors = new ArrayList<GraphNode>();

    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;

    // set the penalty based on the nodeType
    penalty = 0;
  }

  /**
   * Adds a neighbor node to the list of node neighbors for this node
   *
   * @param neighbor node that is connected to this node by an edge
   */
  public void addNeighbor(GraphNode neighbor) {
    neighbors.add(neighbor);
  }

  /**
   * Compares two Node objects using the 'score' attribute. Nodes with a higher (worse) are 'greater
   * than' Nodes with a lower (better) score.
   *
   * @param other the object to be compared.
   * @return an integer which represents the result of the comparison.
   */
  @Override
  public int compareTo(GraphNode other) {
    if (this.score > other.score) {
      return 1;
    } else if (this.score < other.score) {
      return -1;
    } else {
      return 0;
    }
  }
}
