package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class PathInfo {
  @Getter @Setter private ArrayList<GraphNode> path;
  @Getter @Setter private ArrayList<String> floorPath; // ???
  @Getter @Setter private boolean containsStairs;
  // @Getter @Setter private LocalDate associatedDate; // not sure if this is needed

  public PathInfo(ArrayList<GraphNode> path, ArrayList<String> floorPath, boolean containsStairs) {
    this.path = path;
    this.floorPath = floorPath;
    this.containsStairs = containsStairs;
  }
}
