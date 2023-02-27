package edu.wpi.cs3733.C23.teamA.mapdrawing;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinates;
import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinatesReversed;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.controllers.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.controlsfx.control.PopOver;

public class NodeDraw {

  static Pane previousSelectedNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectedNodeEntity = null;

  static List<Pane> listOfPanes;

  static Pane currentPane = new Pane();

  static Line selectedLine = null;
  static Line previousLine = null;
  static EdgeEntity selectedEdge = null;

  static NodeEntity node1;
  static NodeEntity node2;
  /*
  static int xCoordUpdate = 0;
  static int yCoordUpdate = 0;
   */
  static PopOver nodeInfoPopup;

  static double[] previousCoords = new double[2];

  static ArrayList<NodeEntity> selectedNodes = new ArrayList<>();
  static NodeEntity firstNode;

  public static void closePopup() {
    nodeInfoPopup.hide();
  }

  public static void popupNodeInfo(Pane parent, double X, double Y) throws IOException {

    FXMLLoader edgeLoader =
        new FXMLLoader(Main.class.getResource("views/MapEditorNodeInfoPopupFXML.fxml"));

    nodeInfoPopup = new PopOver(edgeLoader.load());
    nodeInfoPopup.show((parent.getScene().getWindow()));

    // sets the popup coords to the mouse/node
    nodeInfoPopup.setAnchorX(X);
    nodeInfoPopup.setAnchorY(Y - 16); // slight offset was needed
  }

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

  public static List<Pane> getPaneList() {
    return listOfPanes;
  }

  public static void drawLocations(
      List<NodeEntity> allNodes, double scaleFactor, AnchorPane nodeAnchor) {

    LocationsDraw.drawLocations(allNodes, nodeAnchor, scaleFactor, true);
  }

  public static void drawNodes(
      List<NodeEntity> allNodes,
      double scaleFactor,
      AnchorPane nodeAnchor,
      MapEditorController nmc) {
    // nodeAnchor.getChildren().clear();
    listOfPanes = new ArrayList<>();
    // draw circle for each node
    for (NodeEntity n : allNodes) {
      double[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);
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

      listOfPanes.add(nodeGraphic);

      // when mouse is clicked
      EventHandler<MouseEvent> eventHandler =
          event -> {
            boolean shiftPressed = event.isShiftDown();
            selectNodePane = nodeGraphic;
            selectedEdge = null;
            if (previousSelectedNode != null && !(previousSelectedNode.equals(nodeGraphic))) {

              previousSelectedNode.setStyle(
                  "-fx-background-color: '#224870'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: '#224870'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 13.5");
              previousSelectedNode.setPrefSize(5, 5);
              previousSelectedNode.setLayoutX(previousCoords[0] - 2.5);
              previousSelectedNode.setLayoutY(previousCoords[1] - 2.5);

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
            nodeGraphic.setLayoutX(updatedCoords[0] - 3.5);
            nodeGraphic.setLayoutY(updatedCoords[1] - 3.5);

            previousCoords = updatedCoords;
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

            MapEditorController.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord());

            if (!(FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()) == null)) {
              nmc.setLongNameBox(
                  FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getLongname());

              nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));

              nmc.setLocationIDBox(
                  MapEditorController.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));

              nmc.setShortNameBox(
                  FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getShortname());
              nmc.setLocTypeBox(
                  FacadeRepository.getInstance()
                      .moveMostRecentLoc(n.getNodeid())
                      .getLocationtype());
              // nmc.setLocButtonVisibility(false);
            } else {
              nmc.setLongNameBox("NO LOCATION ASSIGNED");

              nmc.setLocationIDBox(nmc.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
              // nmc.setLocButtonVisibility(true);
            }

            // pass in node and location to popup controller
            MapEditorNodeInfoPopupController.node = selectedNodeEntity;
            MapEditorNodeInfoPopupController.location =
                FacadeRepository.getInstance().moveMostRecentLoc(selectedNodeEntity.getNodeid());

            // pop up node info popup
            try {

              popupNodeInfo(nodeGraphic, event.getSceneX(), event.getSceneY());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }

            if (shiftPressed) {
              System.out.println("shift pressed");

              nmc.setLocationIDBox(
                  MapEditorController.makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
              // nmc.setLocButtonVisibility(true);
            }
            if (!shiftPressed
                && !event.isShortcutDown()
                && !event.isAltDown()
                && !(event.getButton() == MouseButton.SECONDARY)) {

              // pass in node and location to popup controller
              MapEditorNodeInfoPopupController.node = selectedNodeEntity;
              MapEditorNodeInfoPopupController.location =
                  FacadeRepository.getInstance().moveMostRecentLoc(selectedNodeEntity.getNodeid());

              // pop up node info popup
              try {

                popupNodeInfo(nodeGraphic, event.getSceneX(), event.getSceneY());
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
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
                alert.setHeaderText(
                    "Are you sure you want to create an edge between "
                        + FacadeRepository.getInstance()
                            .getLocation(selectedNodeEntity.getNodeid()));
                LocationNameEntity locNameEnt =
                    FacadeRepository.getInstance().getLocation(selectedNodeEntity.getNodeid());

                alert.setHeaderText(
                    "Are you sure you want to create an edge between: ("
                        + node1.getNodeid()
                        + ", "
                        //                          + selectedEdge.getNode1().getYcoord()
                        + " and ("
                        + node2.getNodeid()
                        + ", "
                        //                          + selectedEdge.getNode2().getYcoord()
                        + FacadeRepository.getInstance()
                            .moveMostRecentLoc(node1.getNodeid())
                            .getShortname()
                        + " and "
                        + FacadeRepository.getInstance()
                            .moveMostRecentLoc(node2.getNodeid())
                            .getShortname());

                alert.setContentText(
                    "Are you sure you want to create an edge between: ("
                        + node1.getXcoord()
                        + ", "
                        + node1.getYcoord()
                        + ") and ("
                        + node2.getXcoord()
                        + ", "
                        + node2.getYcoord()
                        + ") ?");

                if (alert.showAndWait().get() == ButtonType.OK) {
                  Line l =
                      new Line(
                          node1.getXcoord(),
                          node1.getYcoord(),
                          node2.getXcoord(),
                          node2.getYcoord());

                  l.setStrokeWidth(500);
                  l.setVisible(true);

                  FacadeRepository.getInstance().addEdge(new EdgeEntity(node1, node2));
                  node1 = null;
                  node2 = null;

                  nmc.initialize();
                }

              } else if (node1 == null) { // don't fucking touch this <3
                node1 =
                    new NodeEntity(
                        selectedNodeEntity.getNodeid(),
                        selectedNodeEntity.getXcoord(),
                        selectedNodeEntity.getYcoord(),
                        selectedNodeEntity.getFloor(),
                        selectedNodeEntity.getBuilding());
              }
            }
            if (event.isAltDown()) {
              NodeEntity selectedNode = getSelected();

              if (selectedNode != null) {
                selectedNodes.add(selectedNode);
                if (firstNode == null) {
                  firstNode = selectedNode;
                }
              }
            }
            if (!event.isShortcutDown() && !event.isAltDown()) {

              if (selectedNodes != null) {
                selectedNodes.clear();
              }
              firstNode = null;
            }
            // end of The Straightener tm

          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      // for hover over node
      EventHandler<MouseEvent> eventHandler2 =
          event -> {
            if ((!nodeGraphic.equals(selectNodePane))) {
              nodeGraphic.setStyle(
                  "-fx-background-color: 'green'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: 'green'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 13.5");
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler2);

      EventHandler<MouseEvent> eventHandler3 =
          event -> {
            if ((!nodeGraphic.equals(selectNodePane))) {
              nodeGraphic.setStyle(
                  "-fx-background-color: '#224870'; "
                      + "-fx-background-radius: 12.5; "
                      + "-fx-border-color: '#224870'; "
                      + "-fx-border-width: 1;"
                      + "-fx-border-radius: 13.5");
            }
          };
      nodeGraphic.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler3);

      //      final int newX = (int) nodeGraphic.getLayoutX();
      //      final int newY = (int) nodeGraphic.getLayoutY();

      // pass in a node entity and new ID
      nodeGraphic.setOnMouseDragged(dragEvent(nmc));

      nodeGraphic.setOnMouseReleased(
          event -> {
            nmc.getMainGesturePane().setGestureEnabled(true);
            if (!event.isStillSincePress()) {

              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Node Drag and Drop");
              alert.setHeaderText("Would you like to make this change?");

              if (alert.showAndWait().get() == ButtonType.OK) {
                double[] revertedCoords =
                    scaleCoordinatesReversed(
                        selectNodePane.getLayoutX(), selectNodePane.getLayoutY(), scaleFactor);
                selectedNodeEntity.setXcoord((int) Math.round(revertedCoords[0]));
                selectedNodeEntity.setYcoord((int) Math.round(revertedCoords[1]));
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
          });

      nodeGraphic.setOnMouseClicked(
          event -> {
            if (event.getButton() == MouseButton.SECONDARY) {

              NodeEditorEditPopupController.setNode(selectedNodeEntity);
              NodeEditorEditPopupController.setXCord(selectedNodeEntity.getXcoord());
              NodeEditorEditPopupController.setYCord(selectedNodeEntity.getYcoord());
              NodeEditorEditPopupController.setFloor(selectedNodeEntity.getFloor());
              NodeEditorEditPopupController.setBuilding(selectedNodeEntity.getBuilding());

              LocationNameEntity loc =
                  FacadeRepository.getInstance().moveMostRecentLoc(selectedNodeEntity.getNodeid());
              if (loc != null) {
                LocationEditorEditPopupController.setLocNameEntity(loc);
                LocationEditorEditPopupController.setLongname(loc.getLongname());
                LocationEditorEditPopupController.setShortname(loc.getShortname());
                LocationEditorEditPopupController.setLocType(loc.getLocationtype());
              }
            }
          });

      /*
      nodeAnchor.setOnMouseClicked(
          event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
              coords =
                  CoordinateScalar.scaleCoordinatesReversed(
                      selectNodePane.getLayoutX(), selectNodePane.getLayoutY(), scaleFactor);

              System.out.println(Math.round(coords[0]) + ", " + Math.round(coords[1]));

              NodeEditorPopupController.setFloor(selectedNodeEntity.getFloor());
              NodeEditorPopupController.setMouseX((int) Math.round(coords[0]));
              NodeEditorPopupController.setMouseY((int) Math.round(coords[1]));
            }
          });

       */

      nodeAnchor.getChildren().add(nodeGraphic);
    }
  }

  public static void delNode() {

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
      selectNodePane.setMaxSize(0, 0);
      selectNodePane.setMinSize(0, 0);
      selectNodePane.setLayoutX(-100);
      selectNodePane.setLayoutY(-100);

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

  public static void delEdge() {
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

  private static EventHandler<? super MouseEvent> dragEvent(MapEditorController nmc) {

    return (EventHandler<MouseEvent>)
        mouseEvent1 -> {
          if (selectNodePane != null) {
            nmc.getMainGesturePane().setGestureEnabled(false);

            selectNodePane.setLayoutX(selectNodePane.getLayoutX() + mouseEvent1.getX());
            selectNodePane.setLayoutY(selectNodePane.getLayoutY() + mouseEvent1.getY());
          }
        };
  };

  // end _________________________________________________________________

  public static void straightenNodesHorizontal() {

    int yAlign = firstNode.getYcoord();

    for (NodeEntity node : selectedNodes) {

      node.setYcoord(yAlign);
      FacadeRepository.getInstance().updateNode(node.getNodeid(), node);
    }
  }

  public static void straightenNodesVertical() {

    int xAlign = firstNode.getXcoord();

    for (NodeEntity node : selectedNodes) {

      node.setXcoord(xAlign);

      FacadeRepository.getInstance().updateNode(node.getNodeid(), node);
    }
  }

  public static void drawEdges(List<EdgeEntity> allEdges, double scaleFactor, AnchorPane ap) {
    ap.getChildren().clear();

    for (EdgeEntity edge : allEdges) {
      double[] updatedCoordsNode1 =
          scaleCoordinates(edge.getNode1().getXcoord(), edge.getNode1().getYcoord(), scaleFactor);
      double[] updatedCoordsNode2 =
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
            selectedNodeEntity = null;
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
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

      // for hover over node
      EventHandler<MouseEvent> eventHandler2 =
          event -> {
            if ((!currentLine.equals(selectedLine))) {
              currentLine.setStroke(Color.web("green"));
              currentLine.setStrokeWidth(2);
            }
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler2);

      EventHandler<MouseEvent> eventHandler3 =
          event -> {
            if ((!currentLine.equals(selectedLine))) {
              currentLine.setStroke(Color.web("0x224870"));
              currentLine.setStrokeWidth(1);
            }
          };
      currentLine.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler3);

      ap.getChildren().add(currentLine);
    }
  }
}
