package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum UrgencyLevel {
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High"),
  EXTREMELY("Extremely");

  private final String urgency;

  UrgencyLevel(String urgency) {
    this.urgency = urgency;
  }

  public String getUrgency() {
    return urgency;
  }

  //  public static UrgencyLevel value(String str) {
  //    switch (str) {
  //      case "Low":
  //        return LOW;
  //      case "Medium":
  //        return MEDIUM;
  //      case "High":
  //        return HIGH;
  //      case "Extremely Urgent":
  //        return EXTREMELY_URGENT;
  //      default:
  //        return LOW;
  //    }
  //  }

  public static ArrayList<String> urgencyList() {
    ArrayList<String> urgencies = new ArrayList<String>();
    for (UrgencyLevel urgency : values()) {
      urgencies.add(urgency.getUrgency());
    }
    return urgencies;
  }
}
