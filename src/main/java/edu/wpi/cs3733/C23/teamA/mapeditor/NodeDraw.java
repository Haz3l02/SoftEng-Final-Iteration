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

public class NodeDraw {

  static Pane previousSelectedNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectedNodeEntity = null;

  static Pane currentPane = new Pane();

  static Line selectedLine = null;
  static Line previousLine = null;
  static EdgeEntity selectedEdge = null;

  static boolean setLocationVisibility;

  public void setNewLocation() {}

  public static void setVisibility(boolean b) {}

  public static NodeEntity getSelected() {
    return selectedNodeEntity;
  }

  public static Pane getSelectedPane() {
    return selectNodePane;
  }

  public static void setSelectedPane(Pane p) {
    previousSelectedNode = p;
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
    nodeAnchor.getChildren().clear();

    // draw circle for each node
    for (NodeEntity n : allNodes) {
      int[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);
      Pane nodeGraphic = new Pane();

      currentPane = nodeGraphic;

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

      // when mouse is clicked
      EventHandler<MouseEvent> eventHandler =
          event -> {
            selectNodePane = nodeGraphic;

            if ((previousSelectedNode != null)) {
              if (!previousSelectedNode.equals(nodeGraphic)) {
                previousSelectedNode.setStyle(
                    "-fx-background-color: '#224870'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: '#224870'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
                previousSelectedNode.setPrefSize(5, 5);
                selectedLine.setStroke(Color.web("0x224870"));
                previousLine.setStrokeWidth(1);
              }
            }

            nodeGraphic.setStyle(
                "-fx-background-color: '#D3E9F6'; "
                    + "-fx-background-radius: 12.5; "
                    + "-fx-border-color: '#224870'; "
                    + "-fx-border-width: 1;"
                    + "-fx-border-radius: 13.5");
            nodeGraphic.setPrefSize(7, 7);
            //                            nodeGraphic.setLayoutX(nodeGraphic.getXcoord() -3.5);
            //                            nodeGraphic.setLayoutY(nodeGraphic.getYcoord() -3.5);

            previousSelectedNode = nodeGraphic;
            selectedNodeEntity = n;
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
              nmc.setLongNameBox("NO LOCATION ASSIGNED");
              nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
              nmc.setLocButtonVisibility(true);
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      // for hover over node
      //            EventHandler<MouseEvent> eventHandler2 =
      //                new EventHandler<MouseEvent>() {
      //                  @Override
      //                  public void handle(MouseEvent event) {
      //                    if ((!nodeGraphic.equals(selectNodePane))) {
      //                      nodeGraphic.setStyle(
      //                          "-fx-background-color: 'green'; "
      //                              + "-fx-background-radius: 12.5; "
      //                              + "-fx-border-color: 'green'; "
      //                              + "-fx-border-width: 1;"
      //                              + "-fx-border-radius: 13.5");
      //                    }
      //                  }
      //                };
      //            nodeGraphic.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler2);
      //
      //      EventHandler<MouseEvent> eventHandler3 =
      //          new EventHandler<MouseEvent>() {
      //            @Override
      //            public void handle(MouseEvent event) {
      //              if ((!nodeGraphic.equals(selectNodePane))) {
      //                nodeGraphic.setStyle(
      //                    "-fx-background-color: '#224870'; "
      //                        + "-fx-background-radius: 12.5; "
      //                        + "-fx-border-color: '#224870'; "
      //                        + "-fx-border-width: 1;"
      //                        + "-fx-border-radius: 13.5");
      //              }
      //            }
      //          };
      //      nodeGraphic.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler3);

      nodeGraphic.setOnMouseDragged(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              if (selectNodePane != null) {
                nmc.getMainGesturePane().setGestureEnabled(false);
                selectNodePane.setLayoutX(selectNodePane.getLayoutX() + mouseEvent.getX());
                selectNodePane.setLayoutY(selectNodePane.getLayoutY() + mouseEvent.getY());
              }
            }
          });

      nodeGraphic.setOnMouseReleased(
          new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              if (!event.isStillSincePress()) {
                System.out.println("node dropped");
                nmc.getMainGesturePane().setGestureEnabled(true);
                System.out.println((int) selectNodePane.getLayoutX());
                System.out.println((int) selectNodePane.getLayoutY());
                n.setXcoord((int) selectNodePane.getLayoutX());
                n.setYcoord((int) selectNodePane.getLayoutY());

                drawNodes(allNodes, scaleFactor, nodeAnchor, nmc);
              }
            }
          });

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }
  // end _________________________________________________________________

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

      // when mouse is clicked
      EventHandler<MouseEvent> eventHandler =
          event -> {
            selectedLine = currentLine;

            if ((previousLine != null)) {
              if (!previousLine.equals(currentLine)) {
                previousLine.setStroke(Color.web("0x224870"));
                previousLine.setStrokeWidth(1);

                previousSelectedNode.setStyle(
                    "-fx-background-color: '#224870'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: '#224870'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
                previousSelectedNode.setPrefSize(5, 5);
              }
            }

            currentLine.setStroke(Color.web("yellow"));
            currentLine.setStrokeWidth(2);

            previousLine = currentLine;
            selectedEdge = edge;

            //                    nmc.setXCord(n.getXcoord().toString());
            //                    nmc.setYCord(n.getYcoord().toString());
            //
            // nmc.setFloorBox(Floor.extendedStringFromTableString(n.getFloor()));
            //                    // nmc.setFloorBox(n.getFloor());
            //                    nmc.setBuildingBox(n.getBuilding());
            //                    nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord());
            //
            //                    if
            // (!(FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()) == null)) {
            //                        nmc.setLongNameBox(
            //
            // FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getLongname());
            //                        nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(),
            // n.getXcoord(), n.getYcoord()));
            //                        nmc.setLocButtonVisibility(false);
            //                    } else {
            //                        nmc.setLongNameBox("NO LOCATION ASSIGNED");
            //                        nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(),
            // n.getXcoord(), n.getYcoord()));
            //                        nmc.setLocButtonVisibility(true);
            //                    }
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      // for hover over node
      EventHandler<MouseEvent> eventHandler2 =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              if ((!currentLine.equals(selectedLine))) {
                currentLine.setStroke(Color.web("green"));
                currentLine.setStrokeWidth(2);
                System.out.println("Hovering");
              }
            }
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler2);

      EventHandler<MouseEvent> eventHandler3 =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              if ((!currentLine.equals(selectedLine))) {
                currentLine.setStroke(Color.web("0x224870"));
                currentLine.setStrokeWidth(1);
                System.out.println("exit");
              }
            }
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler3);

      ap.getChildren().add(currentLine);
    }
  }
}
