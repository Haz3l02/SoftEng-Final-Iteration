package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;

public interface IAlgorithmStrategy {

  ArrayList<GraphNode> traverse(GraphNode startNode, GraphNode endNode);
}
