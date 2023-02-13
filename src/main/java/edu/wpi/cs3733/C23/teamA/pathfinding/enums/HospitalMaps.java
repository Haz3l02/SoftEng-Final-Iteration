package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum HospitalMaps {
  L1(
      "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/00_thelowerlevel1_unlabeled.png"),
  L2(
      "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/00_thelowerlevel2_unlabeled.png"),
  F1(
      "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/01_thefirstfloor_unlabeled.png"),
  F2(
      "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/02_thesecondfloor_unlabeled.png"),
  F3(
      "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/03_thethirdfloor_unlabeled.png");

  private final String filename;

  HospitalMaps(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
