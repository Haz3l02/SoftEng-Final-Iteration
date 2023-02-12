package edu.wpi.cs3733.C23.teamA.pathfinding.algorithms;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import java.util.ArrayList;

public interface IAlgorithmStrategy {

  ArrayList<GraphNode> traverse(GraphNode startNode, GraphNode endNode);
}
