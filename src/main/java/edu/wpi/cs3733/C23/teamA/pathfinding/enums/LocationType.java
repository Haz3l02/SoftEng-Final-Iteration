package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum LocationType {
  HALL("HALL", "Hallway"),
  ELEV("ELEV", "Elevator"),
  REST("REST", "Restroom"),
  STAI("STAI", "Staircase"),
  DEPT("DEPT", "Medical Department, Clinic, Waiting Room"),
  LABS("LABS", "Lab, Imaging Center, Medical Testing Area"),
  INFO("INFO", "Information Desk, Security Desk, Lost and Found"),
  CONF("CONF", "Conference Room"),
  EXIT("EXIT", "Exit/Entrance"),
  RETL("RETL", "Retail"),
  SERV("SERV", "Other Non-Medical Services");

  private final String tableString;
  private final String extendedString;

  LocationType(String tableString, String extendedString) {
    this.tableString = tableString;
    this.extendedString = extendedString;
  }

  public String getTableString() {
    return tableString;
  }

  public String getExtendedString() {
    return extendedString;
  }

  // takes the extended string and returns the name of the enum
  public static String fromString(String extendedString) {
    for (LocationType l : LocationType.values()) {
      if (l.getExtendedString().equals(extendedString)) {
        return l.getTableString(); // tableString is the same as the enum name
      }
    }
    return null;
  }
}
