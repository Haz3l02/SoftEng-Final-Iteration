package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Node {

  // attributes from the .csv file
  @Getter private String nodeID; // unique string id of the node
  @Getter private int xCoord; // x coordinate of where it is located
  @Getter private int yCoord; // y coordinate of where it is located
  private String floor; // floor identifier that it is on
  private String building; // name of building it is in
  private String nodeType; // type of node (ie hallway, restroom, etc)
  @Getter private String longName; // name of node
  private String shortName; // shortened name of node

  // attribute for graph searching
  @Getter @Setter private boolean visited; // where it has been visited in a search

  @Getter @Setter
  private Node parent; // the node it was first accessed from (used for backtracking)

  @Getter private ArrayList<Node> neighbors; // nodes that have edges with this node
  @Getter @Setter private double costFromStart;
  @Getter @Setter private double heurCostToEnd;

  /**
   * Node Constructor
   *
   * @param nodeID is a unique id name
   */
  public Node(String nodeID) {
    this.nodeID = nodeID;
    visited = false;
    neighbors = new ArrayList<Node>();
  }

  /**
   * Node Constructor with name, x-coordinates, and y-coordinates included
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
  }

  /**
   * Node Constructor with all features in the .csv file.
   *
   * @param nodeID
   * @param xCoord
   * @param yCoord
   * @param floor
   * @param building
   * @param nodeType
   * @param longName
   * @param shortName
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
  }

  /**
   * Adds a neighbor node to the list of node neighbors for this node
   *
   * @param neighbor node that is connected to this node by an edge
   */
  public void addNeighbor(Node neighbor) {
    neighbors.add(neighbor);
  }
}
