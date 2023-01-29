package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;

public class SanitationRequest extends ServiceRequestEntities {
  IssueCategory category;

  public SanitationRequest(
      String name,
      String idNum,
      String location,
      String description,
      String category,
      String ul,
      String requestType) {
    super(name, idNum, location, description, ul, requestType);

    switch (category) {
      case "standard":
        this.category = IssueCategory.STANDARD;
      case "biohazard":
        this.category = IssueCategory.BIOHAZARD;
      default:
        this.category = IssueCategory.STANDARD; // CHECK WHAT IS THE DEFAULT CASE
    }
  }
}
