package edu.wpi.cs3733.C23.teamA.pathfinding.algorithms;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;

public interface IAlgorithmStrategy {

  PathInfo traverse(GraphNode startNode, GraphNode endNode);

  PathInfo traverseNoStairs(GraphNode startNode, GraphNode endNode);
}
