package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

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

  public static ArrayList<String> statusList() {
    ArrayList<String> statuses = new ArrayList<String>();
    for (Status status : values()) {
      statuses.add(status.getStatus());
    }
    return statuses;
  }
}
