package edu.wpi.cs3733.C23.teamA;

public class ComputerEntity extends ServiceRequestEntities {

  String deviceIDnum;
  String devices;

  public ComputerEntity(
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String devices,
      String deviceIDNum,
      String requestType,
      String status) {
    super(name, idNum, location, description, ul, requestType, status);
    this.deviceIDnum = deviceIDNum;
    this.devices = devices;
  }
}
