package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Floor {
  // GF("???", "Ground Floor"), not doing the ground floor this term, apparently
  L1("L1", "Lower Level One", "L1", 0),
  L2("L2", "Lower Level Two", "L2", 1),
  F1("1", "First Floor", "F1", 2),
  F2("2", "Second Floor", "F2", 3),
  F3("3", "Third Floor", "F3", 4);

  private final String tableString;
  private final String extendedString;
  private final String enumName;
  private final int index;

  Floor(String tableString, String extendedString, String enumName, int index) {
    this.tableString = tableString;
    this.extendedString = extendedString;
    this.enumName = enumName;
    this.index = index;
  }

  public String getTableString() {
    return tableString;
  }

  public String getExtendedString() {
    return extendedString;
  }

  public String getEnumName() {
    return enumName;
  }

  public int getIndex() {
    return index;
  }

  // takes the extended string and returns the name of the enum
  public static String fromString(String extendedString) {
    for (Floor f : Floor.values()) {
      if (f.getExtendedString().equals(extendedString)) {
        return f.getEnumName();
      }
    }
    return null;
  }

  // get the index based off of the table string
  public static int indexFromTableString(String tableString) {
    for (Floor f : Floor.values()) {
      if (f.getTableString().equals(tableString)) {
        return f.getIndex();
      }
    }
    return -1;
  }
}
