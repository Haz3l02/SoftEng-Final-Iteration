package edu.wpi.cs3733.C23.teamA.mapeditor;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class NodeDraw {

  static Node previousNode = null;

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
      Pane nodeGraphic = new Pane();

      /* Set the style of the node */
      nodeGraphic.setPrefSize(4, 4);
      nodeGraphic.setLayoutX(updatedCoords[0] - 2);
      nodeGraphic.setLayoutY(updatedCoords[1] - 2);
      nodeGraphic.setStyle(
          "-fx-background-color: '#F6BD38'; "
              + "-fx-background-radius: 12.5; "
              + "-fx-border-color: '013A75'; "
              + "-fx-border-width: 1;"
              + "-fx-border-radius: 12.5");

      EventHandler<MouseEvent> eventHandler =
          new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              System.out.println("Hello World");

              if ((previousNode != null)) {
                System.out.println("made it");
                if (!previousNode.equals(nodeGraphic)) {
                  System.out.println("setting yellow");
                  previousNode.setStyle(
                      "-fx-background-color: '#F6BD38'; "
                          + "-fx-background-radius: 12.5; "
                          + "-fx-border-color: '013A75'; "
                          + "-fx-border-width: 1;"
                          + "-fx-border-radius: 12.5");
                }
              }

              nodeGraphic.setStyle(
                  "-fx-background-color: 'red'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: 'red'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 12.5");

              previousNode = nodeGraphic;
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
      // nodeGraphic.setOnMouseClicked(event -> System.out.println("click"));

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }
}
