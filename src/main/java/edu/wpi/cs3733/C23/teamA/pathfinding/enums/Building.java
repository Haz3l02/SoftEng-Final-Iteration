package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Building {
  FR45("45 Francis"),
  TOWR("Tower"),
  SHPR("Shapiro"),
  _BTM("BTM");
  // may need more

  private final String tableString;

  Building(String tableString) {
    this.tableString = tableString;
  }

  public String getTableString() {
    return tableString;
  }
}
