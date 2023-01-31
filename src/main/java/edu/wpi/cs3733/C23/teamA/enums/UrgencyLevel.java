package edu.wpi.cs3733.C23.teamA.enums;

public enum UrgencyLevel {
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High"),
  EXTREMELY_URGENT("Extremely Urgent");

  private final String urgency;

  UrgencyLevel(String urgency) {
    this.urgency = urgency;
  }

  public String getUrgency() {
    return urgency;
  }
}
