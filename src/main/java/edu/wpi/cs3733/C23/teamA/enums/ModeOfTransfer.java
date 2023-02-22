package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum ModeOfTransfer {
  BED("Bed"),
  WHEELCHAIR("Wheelchair"),
  WALKER("Walker"),
  ASSISTANCE("Assistance"),
  INCUBATOR("Incubator"),
  NONE("None");

  private final String mode;

  ModeOfTransfer(String mode) {
    this.mode = mode;
  }

  public String getMode() {
    return mode;
  }

  public static ModeOfTransfer value(String str) {
    switch (str) {
      case "Bed":
        return BED;
      case "Wheelchair":
        return WHEELCHAIR;
      case "Walker":
        return WALKER;
      case "Assistance":
        return ASSISTANCE;
      case "Incubator":
        return INCUBATOR;
      case "None":
        return NONE;
      default:
        return NONE;
    }
  }

  public static ArrayList<String> modeOfTransfersList() {
    ArrayList<String> modes = new ArrayList<>();
    for (ModeOfTransfer mode : values()) {
      modes.add(mode.getMode());
    }
    return modes;
  }
}
