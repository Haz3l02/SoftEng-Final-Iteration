package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum Mobility {
  MAXIMUM("Maximum"),
  MODERATE("Moderate"),
  MINIMUM("Minimum"),
  NOASSISTMAXIMUMANCE("No Assistance");

  private final String mobility;

  Mobility(String mobility) {
    this.mobility = mobility;
  }

  public String getMobility() {
    return mobility;
  }

  public static Mobility value(String str) {
    switch (str) {
      case "Maximum":
        return MAXIMUM;
      case "Moderate":
        return MODERATE;
      case "Minimum":
        return MINIMUM;
      case "No Assistance":
        return NOASSISTMAXIMUMANCE;
      default:
        return NOASSISTMAXIMUMANCE;
    }
  }

  public static ArrayList<String> mobilityList() {
    ArrayList<String> mobilities = new ArrayList<String>();
    for (Mobility mobility : values()) {
      mobilities.add(mobility.getMobility());
    }
    return mobilities;
  }
}
