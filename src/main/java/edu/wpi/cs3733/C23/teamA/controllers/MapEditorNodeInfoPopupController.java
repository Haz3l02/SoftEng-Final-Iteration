package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.mapdrawing.NodeDraw;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MapEditorNodeInfoPopupController {

  @FXML MFXTextField nodeCoordsBox;
  @FXML MFXTextField nodeFloorBox;
  @FXML MFXTextField nodeBuildingBox;
  @FXML MFXTextField nodeIDBox;
  @FXML MFXTextField nodeLocBox;
  @FXML MFXTextField nodeLongnameBox;

  public static NodeEntity node;
  public static LocationNameEntity location;

  @FXML
  public void initialize() {

    nodeIDBox.setText(node.getNodeid());
    nodeBuildingBox.setText(node.getBuilding());
    nodeFloorBox.setText(node.getFloor());
    nodeCoordsBox.setText("(" + node.getXcoord() + ", " + node.getYcoord() + ")");
    if (location != null) {
      nodeLocBox.setText(location.getShortname() + "  (" + location.getLocationtype() + ")");
      nodeLongnameBox.setText(location.getLongname());
    } else {
      nodeLocBox.setText("Node has no associated location");
      nodeLongnameBox.setText("Node has no associated location");
    }
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    NodeDraw.closePopup();
  }
}
