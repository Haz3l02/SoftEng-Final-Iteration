package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.BFS;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.DFS;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.IAlgorithmStrategy;

public enum Algorithm {
  // listed in order of best --> worst
  ASTAR("A* (\"A-Star\")", "ASTAR", new AStar()),
  BFS("Breadth-First Search", "BFS", new BFS()),
  DFS("Depth-First Search", "DFS", new DFS());

  private final String dropText;
  private final String enumName;
  private final IAlgorithmStrategy algorithm;

  Algorithm(String dropText, String enumName, IAlgorithmStrategy algorithm) {
    this.dropText = dropText;
    this.enumName = enumName;
    this.algorithm = algorithm;
  }

  public String getDropText() {
    return dropText;
  }

  public String getEnumName() {
    return enumName;
  }

  private IAlgorithmStrategy getAlgorithm() {
    return algorithm;
  }

  public static IAlgorithmStrategy fromString(String dropText) {
    for (Algorithm a : Algorithm.values()) {
      if (a.getDropText().equals(dropText)) {
        return a.getAlgorithm();
      }
    }
    return null;
  }
}
