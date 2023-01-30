package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.enums.RequestCategory;

public class SecurityEntity extends ServiceRequestEntities {
  String phone;
  RequestCategory requests;

  public SecurityEntity(
      String name,
      String phone,
      String idNum,
      String location,
      String description,
      String ul,
      String requests,
      String requestType,
      String status) {
    super(name, idNum, location, description, ul, requestType, status);
    this.phone = phone;
    switch (requests) {
      case "SecurityEscort":
        this.requests = RequestCategory.SECURITY_ESCORT;
      case "Harassment":
        this.requests = RequestCategory.HARASSMENT;
      case "PotentialThreat":
        this.requests = RequestCategory.POTENTIAL_THREAT;
      default:
        this.requests = RequestCategory.POTENTIAL_THREAT; // CHECK WHAT IS THE DEFAULT CASE
    }
  }
}
