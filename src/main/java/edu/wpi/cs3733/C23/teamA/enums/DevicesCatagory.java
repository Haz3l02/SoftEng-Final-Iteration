package edu.wpi.cs3733.C23.teamA.enums;

public enum DevicesCatagory {
  DESKTOP("Desktop"),
  TABLET("Tablet"),
  LAPTOP("Laptop"),
  PERIPHERALS("Peripherals"),
  KIOSK("Kiosk"),
  PRINTER("Printer"),
  MONITOR("Monitor");

  private final String devices;

  DevicesCatagory(String devices) {
    this.devices = devices;
  }

  public String getDevices() {
    return devices;
  }
}
