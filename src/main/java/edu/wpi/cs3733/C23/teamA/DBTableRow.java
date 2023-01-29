package edu.wpi.cs3733.C23.teamA;

import lombok.Getter;
import lombok.Setter;

public class DBTableRow {

  @Getter @Setter private String node;
  @Getter @Setter private String locName;

  @Getter @Setter private String move;

  @Getter @Setter private String edgeData;

  public DBTableRow(String node, String locName, String move, String edgeData) {

    this.edgeData = edgeData;
    this.move = move;
    this.locName = locName;
    this.node = node;
  }
}
