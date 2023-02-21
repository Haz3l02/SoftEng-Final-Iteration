package edu.wpi.cs3733.C23.teamA.mapeditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.controllers.MapEditorController;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class NodeDraw2 {

  static Pane previousNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectNode = null;
  static Line previousLine = null;

  static boolean setLocationVisibility;

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

  public static void drawLocations(
      List<NodeEntity> allNodes,
      double scaleFactor,
      AnchorPane nodeAnchor,
      MapEditorController nmc) {

    nodeAnchor.getChildren().clear();

    for (NodeEntity n : allNodes) {
      int[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);

      if (!(FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()) == null)) {
        Text locName = new Text();
        locName.setVisible(true);
        locName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 5));
        locName.setText(
            FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getShortname());
        locName.setLayoutX(updatedCoords[0] - 2.5);
        locName.setLayoutY(updatedCoords[1] - 2.5);
        nodeAnchor.getChildren().add(locName);
      }
    }
  }

  public static void drawNodes(
      List<NodeEntity> allNodes,
      double scaleFactor,
      AnchorPane nodeAnchor,
      MapEditorController nmc) {

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

      EventHandler<MouseEvent> eventHandler = nodeChangeColor(nodeGraphic, nmc, n);

      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }

  public static void drawEdgesNoClick(
      List<EdgeEntity> allEdges, double scaleFactor, AnchorPane ap) {
    ap.getChildren().clear();

    for (EdgeEntity edge : allEdges) {
      int[] updatedCoordsNode1 =
          scaleCoordinates(edge.getNode1().getXcoord(), edge.getNode1().getYcoord(), scaleFactor);
      int[] updatedCoordsNode2 =
          scaleCoordinates(edge.getNode2().getXcoord(), edge.getNode2().getYcoord(), scaleFactor);

      // make line
      Line currentLine =
          new Line(
              updatedCoordsNode1[0],
              updatedCoordsNode1[1],
              updatedCoordsNode2[0],
              updatedCoordsNode2[1]);
      currentLine.setStroke(Color.web("0x224870"));
      ap.getChildren().add(currentLine);
    }
  }

  public static void drawEdges(List<EdgeEntity> allEdges, double scaleFactor, AnchorPane ap) {
    ap.getChildren().clear();

    for (EdgeEntity edge : allEdges) {
      int[] updatedCoordsNode1 =
          scaleCoordinates(edge.getNode1().getXcoord(), edge.getNode1().getYcoord(), scaleFactor);
      int[] updatedCoordsNode2 =
          scaleCoordinates(edge.getNode2().getXcoord(), edge.getNode2().getYcoord(), scaleFactor);

      // make line
      Line currentLine =
          new Line(
              updatedCoordsNode1[0],
              updatedCoordsNode1[1],
              updatedCoordsNode2[0],
              updatedCoordsNode2[1]);
      currentLine.setStroke(Color.web("0x224870"));
      currentLine.setOnMouseClicked(edgeChangeColor());

      // currentLine.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
      ap.getChildren().add(currentLine);
    }
  }

  /**
   * Mouse event handler that changes color of node and enlarges it when selected and put colors
   * back for previously selected node or edge
   *
   * @param nodeGraphic Pane that this node is on
   * @param nmc a MapEditorController
   * @param n the node that we are trying to change
   * @return
   */
  private static EventHandler<MouseEvent> nodeChangeColor(
      Pane nodeGraphic, MapEditorController nmc, NodeEntity n) {
    EventHandler<MouseEvent> eventHandler =
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            selectNodePane = nodeGraphic;

            if (previousLine != null) {
              previousLine.setStroke(Color.web("0x224870"));
              previousLine = null;
            }

            if ((previousNode != null)) {
              if (!previousNode.equals(nodeGraphic)) {
                previousNode.setStyle(
                    "-fx-background-color: '#224870'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: '#224870'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
                previousNode.setPrefSize(5, 5);
              }
            }

            nodeGraphic.setStyle(
                "-fx-background-color: '#D3E9F6'; "
                    + "-fx-background-radius: 12.5; "
                    + "-fx-border-color: '#224870'; "
                    + "-fx-border-width: 1;"
                    + "-fx-border-radius: 13.5");
            nodeGraphic.setPrefSize(7, 7);
            //                            nodeGraphic.setLayoutX(nodeGraphic.getXcoord() - 3.5);
            //                            nodeGraphic.setLayoutY(nodeGraphic.getYcoord() - 3.5);

            previousNode = nodeGraphic;
            selectNode = n;

            nmc.setXCord(n.getXcoord().toString());
            nmc.setYCord(n.getYcoord().toString());
            nmc.setFloorBox(Floor.extendedStringFromTableString(n.getFloor()));
            // nmc.setFloorBox(n.getFloor());
            nmc.setBuildingBox(n.getBuilding());
            nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord());

            if (!(FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()) == null)) {
              nmc.setLongNameBox(
                  FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getLongname());
              nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
              nmc.setLocButtonVisibility(false);
            } else {
              // nmc.setLongNameBox(null);
              nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
              nmc.setLocButtonVisibility(true);
            }
          }
        };
    return eventHandler;
  }

  /**
   * Changes color of the line edge on click and resets previously selected node and line
   *
   * @return a mouse event handler that changes te color of a line on click
   */
  private static EventHandler<MouseEvent> edgeChangeColor() {
    EventHandler<MouseEvent> eventHandler =
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {

            if (t.getSource() instanceof Line) {

              if (previousLine != null) {
                previousLine.setStroke(Color.web("0x224870"));
              }
              if (previousNode != null) {
                previousNode.setStyle(
                    "-fx-background-color: '#224870'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: '#224870'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
                previousNode.setPrefSize(5, 5);
                previousNode = null;
              }

              Line line = ((Line) (t.getSource()));
              line.setStroke(Color.web("0xf6bd3a"));
              previousLine = line;
            }
          }
        };
    return eventHandler;
  }
}
