package edu.wpi.cs3733.C23.teamA;

public class SecurityEntity extends ServiceRequestEntities {
  String phone;
  String requests;

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
    this.requests = requests;
  }
}
