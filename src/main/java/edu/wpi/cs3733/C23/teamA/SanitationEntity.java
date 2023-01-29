package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;

public class SanitationEntity extends ServiceRequestEntities {
  IssueCategory category;

  public SanitationEntity(
      String name,
      String idNum,
      String location,
      String description,
      String category,
      String ul,
      int requestType) {
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
