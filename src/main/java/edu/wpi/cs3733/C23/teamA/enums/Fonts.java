package edu.wpi.cs3733.C23.teamA.enums;

public enum Fonts {
  BWH_TITLE("Arial Black"),
  REGULAR_TEXT("System");

  private final String fonts;

  Fonts(String fonts) {
    this.fonts = fonts;
  }

  public String getFonts() {
    return fonts;
  }
}
