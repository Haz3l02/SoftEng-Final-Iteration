package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

import edu.wpi.cs3733.C23.teamA.pathfinding.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.BFS;
import edu.wpi.cs3733.C23.teamA.pathfinding.DFS;
import edu.wpi.cs3733.C23.teamA.pathfinding.IAlgorithmStrategy;

public enum Algorithm {
    // listed in order of best --> worst
    ASTAR ("A* (\"A-Star\")", new AStar()),
    BFS ("Breadth-First Search", new BFS()),
    DFS ("Depth-First Search", new DFS());

    private final String dropText;
    private final IAlgorithmStrategy algorithm;

    Algorithm(String dropText, IAlgorithmStrategy algorithm) {
        this.dropText = dropText;
        this.algorithm = algorithm;
    }

    public String getDropText() {
        return dropText;
    }
    public IAlgorithmStrategy getAlgorithm() {
        return algorithm;
    }
}
