package edu.wpi.cs3733.C23.teamA.enums;

public enum RequestCategory {
  HARASSMENT("Harassment"),
  POTENTIAL_THREAT("Potential Threat"),
  SECURITY_ESCORT("Security Threat");

  private final String request;

  RequestCategory(String request) {
    this.request = request;
  }

  public String getRequest() {
    return request;
  }
}
