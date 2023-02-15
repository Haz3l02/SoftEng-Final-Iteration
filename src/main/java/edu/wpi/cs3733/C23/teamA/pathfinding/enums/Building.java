package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Building {
  FR45("45 Francis", "FR45"),
  FR15("15 Francis", "FR15"),
  TOWR("Tower", "TOWR"),
  SHPR("Shapiro", "SHPR"),
  _BTM("BTM", "_BTM");

  private final String tableString;
  private final String enumName;

  Building(String tableString, String enumName) {
    this.tableString = tableString;
    this.enumName = enumName;
  }

  public String getTableString() {
    return tableString;
  }

  public String getEnumName() {
    return enumName;
  }

  // takes the table string (which will display in dropdowns) and returns the name of the enum
  public static String fromString(String tableString) {
    for (Building b : Building.values()) {
      if (b.tableString.equals(tableString)) {
        return b.enumName;
      }
    }
    return null;
  }

  public static String toLongName(String enumName) {
    for (Building b : Building.values()) {
      if (b.enumName.equals(enumName)) {
        return b.getTableString();
      }
    }
    return null;
  }
}
