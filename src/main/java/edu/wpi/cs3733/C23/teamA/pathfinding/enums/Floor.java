package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Floor {
  // WIP
  GF("???", "Ground Floor"),
  L1("L1", "Lower Level One"),
  L2("???", "Lower Level Two"),
  F1("???", "First Floor"),
  F2("???", "Second Floor"),
  F3("???", "Third Floor");

  private final String tableString;
  private final String extendedString;

  Floor(String tableString, String extendedString) {
    this.tableString = tableString;
    this.extendedString = extendedString;
  }

  public String getTableString() {
    return tableString;
  }

  public String getExtendedString() {
    return extendedString;
  }
}
