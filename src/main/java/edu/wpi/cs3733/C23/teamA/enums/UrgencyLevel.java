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

  public static ArrayList<String> urgencyList() {
    ArrayList<String> urgencies = new ArrayList<String>();
    for (UrgencyLevel urgency : values()) {
      urgencies.add(urgency.getUrgency());
    }
    return urgencies;
  }
}
