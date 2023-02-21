package edu.wpi.cs3733.C23.teamA.mapeditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.controllers.MapEditorController;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class NodeDraw implements KeyListener {

  static Pane previousSelectedNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectedNodeEntity = null;

  static Pane currentPane = new Pane();

  static Line selectedLine = null;
  static Line previousLine = null;
  static EdgeEntity selectedEdge = null;

  static NodeEntity node1;
  static NodeEntity node2;
  static int xCoordUpdate = 0;
  static int yCoordUpdate = 0;

  static boolean setLocationVisibility;

  public void setNewLocation() {}

  public static void setVisibility(boolean b) {}

  public static NodeEntity getSelected() {
    return selectedNodeEntity;
  }

  public static EdgeEntity getSelectedEdge() {
    return selectedEdge;
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
    xCoord = (xCoord) * scaleFactor;
    yCoord = (yCoord) * scaleFactor;

    // put the values in an array to return
    int[] scaledCoordinates = {(int) Math.round(xCoord), (int) Math.round(yCoord)};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  /**
   * @param xCoord the x-coordinate to scale
   * @param yCoord the y-coordinate to scale
   * @param scaleFactor the scale factor for the coordinates and the image they are being placed on.
   * @return an int array with the pair of new coordinates
   */
  private static int[] scaleCoordinatesReversed(double xCoord, double yCoord, double scaleFactor) {
    // get the coordinates from the node

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = (xCoord / scaleFactor);
    yCoord = (yCoord / scaleFactor);

    // put the values in an array to return
    int[] scaledCoordinates = {(int) Math.round(xCoord), (int) Math.round(yCoord)};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  public static void drawLocations(
      List<NodeEntity> allNodes,
      double scaleFactor,
      AnchorPane nodeAnchor,
      MapEditorController nmc) {

    // nodeAnchor.getChildren().clear();

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
    // nodeAnchor.getChildren().clear();

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
            boolean shiftPressed = event.isShiftDown();
            selectNodePane = nodeGraphic;

            if (previousSelectedNode != null && !(previousSelectedNode.equals(nodeGraphic))) {

              previousSelectedNode.setStyle(
                  "-fx-background-color: '#224870'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: '#224870'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 13.5");
              previousSelectedNode.setPrefSize(5, 5);

              if (previousLine != null) {
                previousLine.setStroke(Color.web("0x224870"));
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

            if (selectedLine != null) {
              selectedLine.setStroke(Color.web("0x224870"));
              selectedLine.setStrokeWidth(1);
            }

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
            if (shiftPressed) {

              if (node1 != null) {
                // save 2nd node stuff and add edge
                node2 =
                    new NodeEntity(
                        selectedNodeEntity.getNodeid(),
                        selectedNodeEntity.getXcoord(),
                        selectedNodeEntity.getYcoord(),
                        selectedNodeEntity.getFloor(),
                        selectedNodeEntity.getBuilding());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Edge Creation");
                alert.setHeaderText("Are you sure you want to create an edge between " + FacadeRepository.getInstance().getLocation(selectedNodeEntity.getNodeid()));

                if (alert.showAndWait().get() == ButtonType.OK) {
                  Line l =
                      new Line(
                          node1.getXcoord(),
                          node1.getYcoord(),
                          node2.getXcoord(),
                          node2.getYcoord());
                  l.setStrokeWidth(5);
                  FacadeRepository.getInstance().addEdge(new EdgeEntity(node1, node2));
                }

              } else {
                node1 =
                    new NodeEntity(
                        selectedNodeEntity.getNodeid(),
                        selectedNodeEntity.getXcoord(),
                        selectedNodeEntity.getYcoord(),
                        selectedNodeEntity.getFloor(),
                        selectedNodeEntity.getBuilding());
              }
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      // for hover over node
      EventHandler<MouseEvent> eventHandler2 =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              if ((!nodeGraphic.equals(selectNodePane))) {
                nodeGraphic.setStyle(
                    "-fx-background-color: 'green'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: 'green'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
              }
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler2);

      EventHandler<MouseEvent> eventHandler3 =
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              if ((!nodeGraphic.equals(selectNodePane))) {
                nodeGraphic.setStyle(
                    "-fx-background-color: '#224870'; "
                        + "-fx-background-radius: 12.5; "
                        + "-fx-border-color: '#224870'; "
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 13.5");
              }
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler3);

      //      final int newX = (int) nodeGraphic.getLayoutX();
      //      final int newY = (int) nodeGraphic.getLayoutY();

      // pass in a node entity and new ID
      nodeGraphic.setOnMouseDragged(dragEvent(nmc));

      nodeGraphic.setOnMouseReleased(
          new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              if (!event.isStillSincePress()) {
                System.out.println("node dropped");
                nmc.getMainGesturePane().setGestureEnabled(true);
                System.out.println((int) selectNodePane.getLayoutX());
                System.out.println((int) selectNodePane.getLayoutY());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Node Drag and Drop");
                alert.setHeaderText("Would you like to make this change?");

                if (alert.showAndWait().get() == ButtonType.OK) {
                  int[] revertedCoords =
                      scaleCoordinatesReversed(
                          selectNodePane.getLayoutX(), selectNodePane.getLayoutY(), scaleFactor);
                  selectedNodeEntity.setXcoord(revertedCoords[0]);
                  selectedNodeEntity.setYcoord(revertedCoords[1]);
                  FacadeRepository.getInstance()
                      .updateNode(selectedNodeEntity.getNodeid(), selectedNodeEntity);
                }
                nodeAnchor.getChildren().clear();
                drawEdges(
                    FacadeRepository.getInstance().getEdgesOnFloor(n.getFloor()),
                    scaleFactor,
                    nodeAnchor);
                drawNodes(allNodes, scaleFactor, nodeAnchor, nmc);
              }
            }
          });

      nodeGraphic.setOnMouseClicked(
          event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
              Alert a = new Alert(Alert.AlertType.CONFIRMATION);
              a.setTitle("Delete Node?");
              a.setHeaderText("Are you sure you want to delete this node?");
              a.setContentText(
                  "Node to be deleted has ID "
                      + selectedNodeEntity.getNodeid()
                      + " and coordinates ("
                      + selectedNodeEntity.getXcoord()
                      + ", "
                      + selectedNodeEntity.getYcoord()
                      + ")");

              if (a.showAndWait().get() == ButtonType.OK) {

                // transports the node over to fucking narnia (gives the appearance of being updated
                // immediately)
                nodeGraphic.setMaxSize(0, 0);
                nodeGraphic.setMinSize(0, 0);
                nodeGraphic.setLayoutX(-100);
                nodeGraphic.setLayoutY(-100);

                Alert aa = new Alert(Alert.AlertType.CONFIRMATION);
                aa.setTitle("Commence Edge Repair?");
                aa.setHeaderText("Would you like to repair the edges of the deleted node?");
                aa.setContentText("If not repaired, connected edges will be permanently deleted!!");
                if (aa.showAndWait().get() == ButtonType.OK) { // && node has connected edges
                  FacadeRepository.getInstance().collapseNode(selectedNodeEntity);
                } else {
                  FacadeRepository.getInstance().deleteNode(selectedNodeEntity.getNodeid());
                }
              }
            }
          });

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }

  private static EventHandler<? super MouseEvent> dragEvent(MapEditorController nmc) {
    EventHandler event =
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            if (selectNodePane != null) {
              nmc.getMainGesturePane().setGestureEnabled(false);

              System.out.println("get mouse location");
              System.out.println(mouseEvent.getX());
              System.out.println(mouseEvent.getY());
              System.out.println("Get layout");
              System.out.println(selectNodePane.getLayoutX());
              System.out.println(selectNodePane.getLayoutY());

              selectNodePane.setLayoutX(selectNodePane.getLayoutX() + mouseEvent.getX());
              selectNodePane.setLayoutY(selectNodePane.getLayoutY() + mouseEvent.getY());

              System.out.println((int) selectNodePane.getLayoutX());
              System.out.println((int) selectNodePane.getLayoutX());
            }
          }
        };
    return event;
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
                if (previousSelectedNode != null) {
                  previousSelectedNode.setStyle(
                      "-fx-background-color: '#224870'; "
                          + "-fx-background-radius: 12.5; "
                          + "-fx-border-color: '#224870'; "
                          + "-fx-border-width: 1;"
                          + "-fx-border-radius: 13.5");
                  previousSelectedNode.setPrefSize(5, 5);
                }
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

      // TODO: fix delete key stuff
      EventHandler<javafx.scene.input.KeyEvent> deleteHandler =
          event -> {
            System.out.println(event.getCharacter());
            System.out.println(event.getCode());
            System.out.println(event.getText());
            System.out.println(event.getEventType().toString());
            System.out.println("help");
          };

      currentLine.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, deleteHandler);

      currentLine.setOnMouseClicked(
          event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
              Alert a = new Alert(Alert.AlertType.CONFIRMATION);
              a.setTitle("Delete Edge?");
              a.setHeaderText("Are you sure you want to delete this edge?");
              a.setContentText(
                  "edge to be deleted has start coordinates: ("
                      + selectedLine.getStartX()
                      + ", "
                      + selectedLine.getStartY()
                      + ") and end coordinates: ("
                      + selectedLine.getEndX()
                      + ", "
                      + selectedLine.getEndY()
                      + ")");

              if (a.showAndWait().get() == ButtonType.OK) {

                // transports the edge over to fucking narnia (gives the appearance of being updated
                // immediately)
                selectedLine.setStartX(-100);
                selectedLine.setStartY(-100);
                selectedLine.setEndX(-100);
                selectedLine.setEndY(-100);
                FacadeRepository.getInstance().deleteEdge(selectedEdge.getEdgeid());
              }
            }
          });

      ap.getChildren().add(currentLine);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    System.out.println("a");
  }

  @Override
  public void keyPressed(java.awt.event.KeyEvent e) {
    int key = e.getKeyCode();
    System.out.println(key);
    if (key == KeyEvent.VK_DELETE) {
      System.out.println("delete that fucker");

      FacadeRepository.getInstance().deleteEdge(getSelectedEdge().getEdgeid());
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("aaa");
  }
}
