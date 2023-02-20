package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum AVDevice {
  SPEAKER("Speaker"),
  HEADPHONES("Headphones"),
  MICROPHONE("Microphone"),
  TV("TV"),
  MONITOR("Monitor"),
  PROJECTOR("PROJECTOR");

  private final String devices;

  AVDevice(String devices) {
    this.devices = devices;
  }

  public String getDevice() {
    return devices;
  }

  public static ArrayList<String> deviceList() {
    ArrayList<String> devices = new ArrayList<String>();
    for (AVDevice dev : values()) {
      devices.add(dev.getDevice());
    }
    return devices;
  }
}
