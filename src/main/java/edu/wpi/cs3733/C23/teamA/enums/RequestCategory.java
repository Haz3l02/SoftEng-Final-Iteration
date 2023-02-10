package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum RequestCategory {
  HARASSMENT("Harassment"),
  POTENTIAL_THREAT("Potential Threat"),
  SECURITY_ESCORT("Security Escort");

  private final String request;

  RequestCategory(String request) {
    this.request = request;
  }

  public static RequestCategory value(String str) {
    switch (str) {
      case "Harrassment":
        return HARASSMENT;
      case "Potential Threat":
        return POTENTIAL_THREAT;
      case "Security Escort":
        return SECURITY_ESCORT;
      default:
        return POTENTIAL_THREAT;
    }
  }

  public String getRequest() {
    return request;
  }

  public static ArrayList<String> categoryList() {
    ArrayList<String> categories = new ArrayList<String>();
    for (RequestCategory category : values()) {
      categories.add(category.getRequest());
    }
    return categories;
  }
}
