package edu.wpi.cs3733.C23.teamA.mapdrawing;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinates;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LocationsDraw {

  public static void drawLocations(
      List<NodeEntity> allNodes, AnchorPane nodeAnchor, double scaleFactor, boolean includeHalls) {

    nodeAnchor.getChildren().clear();

    for (NodeEntity n : allNodes) {
      double[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);

      // TODO: this doesn't use the latest locations as of the navigation date, but the most recent
      // locations in general.
      LocationNameEntity locNameEnt =
          FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid());

      // check if the location name entity is null
      if (locNameEnt != null) {
        // if it isn't null, make sure that it isn't a hallway
        if (includeHalls || !locNameEnt.getLocationtype().equals("HALL")) {
          Text locName = new Text();
          locName.setVisible(true);
          locName.rotateProperty().set(45);
          locName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 3));
          locName.setText(locNameEnt.getShortname());

          // get the coordinates
          locName.setLayoutX(updatedCoords[0] - 4.5);
          locName.setLayoutY(updatedCoords[1] - 4.5);
          nodeAnchor.getChildren().add(locName);
        }
      }
    }
  }
}
