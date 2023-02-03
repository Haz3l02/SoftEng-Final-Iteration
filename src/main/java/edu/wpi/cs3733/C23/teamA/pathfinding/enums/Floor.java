package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Floor {
  // GF("???", "Ground Floor"), not doing the ground floor this term, apparently
  L1("L1", "Lower Level One"),
  L2("L2", "Lower Level Two"),
  F1("1", "First Floor"),
  F2("2", "Second Floor"),
  F3("3", "Third Floor");

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
