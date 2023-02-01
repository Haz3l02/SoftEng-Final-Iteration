package edu.wpi.cs3733.C23.teamA.enums;

public enum Status {
  BLANK("Blank"),
  PROCESSING("Processing"),
  DONE("Done");

  private final String status;

  Status(String status) {

    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
