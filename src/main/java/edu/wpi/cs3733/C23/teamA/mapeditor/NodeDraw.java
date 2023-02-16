package edu.wpi.cs3733.C23.teamA.mapeditor;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.controllers.NodeMapController;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.awt.*;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class NodeDraw {

  static Pane previousNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectNode = null;

  static boolean setLocationVisibility;

  static MoveImpl locations = new MoveImpl();

  public static void setVisibility(boolean b) {}

  public static NodeEntity getSelected() {
    return selectNode;
  }

  public static Pane getSelectedPane() {
    return selectNodePane;
  }

  public static void setSelectedPane(Pane p) {
    previousNode = p;
  }

  public static int[] scaleCoordinates(double xCoord, double yCoord, double scaleFactor) {
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
      List<NodeEntity> allNodes, double scaleFactor, AnchorPane nodeAnchor, NiniTest nmc) {
    // gc.setFill(Color.web("0x224870"));
    //locations.refresh();

    // draw circle for each node
    for (NodeEntity n : allNodes) {
      int[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);
      Pane nodeGraphic = new Pane();

      /* Set the style of the node */
      nodeGraphic.setPrefSize(5, 5);
      nodeGraphic.setLayoutX(updatedCoords[0] - 2.5);
      nodeGraphic.setLayoutY(updatedCoords[1] - 2.5);
      nodeGraphic.setStyle(
          "-fx-background-color: '#224870'; "
              + "-fx-background-radius: 12.5; "
              + "-fx-border-color: '#224870'; "
              + "-fx-border-width: 1;"
              + "-fx-border-radius: 12.5");
      //      Text locName = new Text();
      //      locName.setVisible(false);
      //      if (!(locations.mostRecentLoc(n.getNodeid()) == null)) {
      //        locName.setVisible(true);
      //        locName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 5));
      //        locName.setText(locations.mostRecentLoc(n.getNodeid()).getShortname());
      //        locName.setLayoutX(updatedCoords[0] - 2.5);
      //        locName.setLayoutY(updatedCoords[1] - 2.5);
      //        NodeMapController nmcToggle = new NodeMapController();
      //        //      if (nmcToggle.toggleLocations()) {
      //        //        locName.setVisible(false);
      //        //      }
      //      }

      EventHandler<MouseEvent> eventHandler =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              selectNodePane = nodeGraphic;

              if ((previousNode != null)) {
                if (!previousNode.equals(nodeGraphic)) {
                  previousNode.setStyle(
                      "-fx-background-color: '#224870'; "
                          + "-fx-background-radius: 12.5; "
                          + "-fx-border-color: '#224870'; "
                          + "-fx-border-width: 1;"
                          + "-fx-border-radius: 13.5");
                  previousNode.setPrefSize(5, 5);
                  //                  previousNode.setLayoutX(updatedCoords[0] - 2.5);
                  //                  previousNode.setLayoutY(updatedCoords[1] - 2.5);
                }
              }

              nodeGraphic.setStyle(
                  "-fx-background-color: '#D3E9F6'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: '#224870'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 13.5");
              nodeGraphic.setPrefSize(7, 7);
              //              nodeGraphic.setLayoutX(updatedCoords[0] - 3.5);
              //              nodeGraphic.setLayoutY(updatedCoords[1] - 3.5);

              previousNode = nodeGraphic;
              selectNode = n;

              nmc.setXCord(n.getXcoord().toString());
              nmc.setYCord(n.getYcoord().toString());
              nmc.setFloorBox(Floor.extendedStringFromTableString(n.getFloor()));
              // nmc.setFloorBox(n.getFloor());
              nmc.setBuildingBox(n.getBuilding());
              nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord());

              if (!(locations.mostRecentLoc(n.getNodeid()) == null)) {
                nmc.setLongNameBox(locations.mostRecentLoc(n.getNodeid()).getLongname());
                nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
                nmc.setLocButtonVisibility(false);
              } else {
                // nmc.setLongNameBox(null);
                nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
                nmc.setLocButtonVisibility(true);
              }
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      nodeAnchor.getChildren().add(nodeGraphic);
      // nodeAnchor.getChildren().add(locName);
    }
  }

  public static void drawEdges(List<EdgeEntity> allEdges, double scaleFactor, GraphicsContext gc) {
    gc.setStroke(Color.web("0x224870"));
    gc.setLineWidth(1);

    for (EdgeEntity edge : allEdges) {
      // get x and y values
      int[] updatedCoordsNode1 =
          scaleCoordinates(edge.getNode1().getXcoord(), edge.getNode1().getYcoord(), scaleFactor);
      int[] updatedCoordsNode2 =
          scaleCoordinates(edge.getNode2().getXcoord(), edge.getNode2().getYcoord(), scaleFactor);

      gc.strokeLine(
          updatedCoordsNode1[0],
          updatedCoordsNode1[1],
          updatedCoordsNode2[0],
          updatedCoordsNode2[1]);
    }
  }
}
