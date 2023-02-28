package edu.wpi.cs3733.C23.teamA.serviceRequests;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import lombok.Getter;
import lombok.Setter;

public class Kiosk {
  @Getter @Setter private NodeEntity startLocation;
  @Getter @Setter private NodeEntity endLocation;
  @Getter @Setter private String left;
  @Getter @Setter private String right;
  @Getter @Setter private boolean directions;
  @Getter @Setter private String message;
  @Getter @Setter private String moveName;

  public Kiosk(
      NodeEntity startLocation,
      NodeEntity endLocation,
      String left,
      String right,
      boolean directions,
      String message,
      String moveName) {
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.left = left;
    this.right = right;
    this.directions = directions;
    this.message = message;
    this.moveName = moveName;
  }
}
