package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

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

  public String getDevice() {
    return devices;
  }

  public static ArrayList<String> deviceList() {
    ArrayList<String> devices = new ArrayList<String>();
    for (DevicesCategory dev : values()) {
      devices.add(dev.getDevice());
    }
    return devices;
  }
}
