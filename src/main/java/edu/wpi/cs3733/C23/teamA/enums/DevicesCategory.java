package edu.wpi.cs3733.C23.teamA.enums;

public enum DevicesCategory {
  DESKTOP("Desktop"),
  TABLET("Tablet"),
  LAPTOP("Laptop"),
  PERIPHERALS("Peripherals"),
  KIOSK("Kiosk"),
  PRINTER("Printer"),
  MONITOR("Monitor");

  private final String devices;

  DevicesCategory(String devices) {
    this.devices = devices;
  }

  public String getDevices() {
    return devices;
  }
}
