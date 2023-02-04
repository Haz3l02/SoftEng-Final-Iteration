package edu.wpi.cs3733.C23.teamA.enums;

public enum Colors {
  COCKED("C0CCED"),
  BADIDEA("BAD1EA"),
  BANNER("224870"),
  TEXT_WHITE("WHITE"),
  BUTTON("D3E9F6"),
  DARK_TEXT("122C34"),
  TEXT_BLACK("BLACK");

  private final String colors;

  Colors(String colors) {
    this.colors = colors;
  }

  public String getColors() {
    return colors;
  }
}
