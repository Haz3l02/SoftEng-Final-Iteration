package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Node implements Comparable<Node> {

  // attributes from the .csv file
  @Getter private String nodeID; // unique string id of the node
  @Getter private int xCoord; // x coordinate of where it is located
  @Getter private int yCoord; // y coordinate of where it is located
  private String floor; // floor identifier that it is on
  private String building; // name of building it is in
  private String nodeType; // type of node (ie hallway, restroom, etc)
  @Getter private String longName; // name of node
  private String shortName; // shortened name of node

  // attributes for graph searching
  @Getter @Setter private boolean visited; // where it has been visited in a search

  @Getter @Setter
  private Node parent; // the node it was first accessed from (used for backtracking)

  @Getter private ArrayList<Node> neighbors; // nodes that have edges with this node

  // attributes specifically for A* searching
  @Getter @Setter private double costFromStart; // g(x)
  @Getter @Setter private double heurCostToEnd; // f(x)

  @Getter @Setter
  private double score; // score (f(x)) = costFromStart (g(x)) + heurCostToEnd (h(x))

  /**
   * Node Constructor
   *
   * @param nodeID is a unique id name
   */
  public Node(String nodeID) {
    this.nodeID = nodeID;
    visited = false;
    neighbors = new ArrayList<Node>();
    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;
  }

  /**
   * Node Constructor with the name, x-coordinates, and y-coordinates included
   *
   * @param nodeID is a unique id name
   * @param xCoord is the x-coordinate (in pixels) on the image of the floor where the node is
   *     located
   * @param yCoord is the y-coordinate (in pixels) on the image of the floor where the node is
   *     located
   * @param longName is the long name of the node
   */
  public Node(String nodeID, int xCoord, int yCoord, String longName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.longName = longName;

    visited = false;
    neighbors = new ArrayList<Node>();
    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;
  }

  /**
   * Node Constructor with all features in the original .csv file.
   *
   * @param nodeID is a unique id name
   * @param xCoord is the x-coordinate (in pixels) on the image of the floor where the node is
   *     located
   * @param yCoord is the y-coordinate (in pixels) on the image of the floor where the node is
   *     located
   * @param floor is the floor where this node of located
   * @param building is the building in which this node is located
   * @param nodeType is the type of location (ELEV = elevator, SERV = service, etc.) at this node
   * @param longName is the long name of the node
   * @param shortName is the shortened name of the node (not the same as the ID)
   */
  public Node(
      String nodeID,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;

    visited = false;
    parent = null;
    neighbors = new ArrayList<Node>();
    costFromStart = Double.POSITIVE_INFINITY;
    heurCostToEnd = Double.POSITIVE_INFINITY;
    score = Double.POSITIVE_INFINITY;
  }

  /**
   * Adds a neighbor node to the list of node neighbors for this node
   *
   * @param neighbor node that is connected to this node by an edge
   */
  public void addNeighbor(Node neighbor) {
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
  public int compareTo(Node other) {
    if (this.score > other.score) {
      return 1;
    } else if (this.score < other.score) {
      return -1;
    } else {
      return 0;
    }
  }
}
