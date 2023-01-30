package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.enums.DevicesCatagory;

public class ComputerEntity extends ServiceRequestEntities {

  String deviceIDnum;
  DevicesCatagory devices;

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

    switch (devices) {
      case "Desktop":
        this.devices = DevicesCatagory.DESKTOP;
      case "Tablet":
        this.devices = DevicesCatagory.TABLET;
      case "Laptop":
        this.devices = DevicesCatagory.LAPTOP;
      case "Moniter":
        this.devices = DevicesCatagory.MONITOR;
      case "Peripherals":
        this.devices = DevicesCatagory.PERIPHERALS;
      case "Kiosks":
        this.devices = DevicesCatagory.KIOSK;
      case "Printers":
        this.devices = DevicesCatagory.PRINTER;
      default:
        this.devices = DevicesCatagory.DESKTOP; // CHECK WHAT IS THE DEFAULT CASE
    }
  }
}
