package edu.wpi.cs3733.C23.teamA.mapeditor;

import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class NodeDraw {

  private static int[] scaleCoordinates(double xCoord, double yCoord, double scaleFactor) {
    // get the coordinates from the node

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = Math.floor(xCoord) * scaleFactor;
    yCoord = Math.floor(yCoord) * scaleFactor;

    // put the values in an array to return
    int[] scaledCoordinates = {(int) xCoord, (int) yCoord};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  public static void drawNodes(
      List<NodeEntity> allNodes, double scaleFactor, AnchorPane nodeAnchor) {
    // gc.setFill(Color.web("0x224870"));

    // draw circle for each node
    for (NodeEntity n : allNodes) {
      int[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);
      // gc.fillOval(updatedCoords[0] - 4, updatedCoords[0] - 4, 8, 8);
      // Circle nodePoint = new Circle(updatedCoords[0], updatedCoords[1], 4,
      // Color.web("0x224870"));
      // System.out.pr
      final Pane nodeGraphic = new Pane();

      /* Set the style of the node */
      nodeGraphic.setPrefSize(4, 4);
      nodeGraphic.setLayoutX(updatedCoords[0] - 4);
      nodeGraphic.setLayoutY(updatedCoords[1] - 4);
      nodeGraphic.setStyle(
          "-fx-background-color: '#F6BD38'; "
              + "-fx-background-radius: 12.5; "
              + "-fx-border-color: '013A75'; "
              + "-fx-border-width: 1;"
              + "-fx-border-radius: 12.5");

      EventHandler<MouseEvent> eventHandler =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
              System.out.println("Hello World");
              nodeGraphic.setStyle(
                  "-fx-background-color: 'red'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: '013A75'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 12.5");
            }
          };

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }
}
