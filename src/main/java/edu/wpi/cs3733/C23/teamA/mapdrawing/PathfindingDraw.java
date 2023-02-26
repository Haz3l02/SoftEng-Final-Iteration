package edu.wpi.cs3733.C23.teamA.mapdrawing;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinates;
import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinatesReversed;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.controllers.DisplayServiceRequestsPopupController;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;

// Class for Controller to call to add the map
public class PathfindingDraw {

  private static final double radius = 3;
  private static final double width = 6;

  private static final double SCALE_FACTOR = 0.135;

  private static Rectangle previousRect;
  private static PopOver previousPopup;

  /**
   * Draws the location names on the map using a method from LocationsDraw
   *
   * @param allNodes is allNodes on the current floor
   * @param nodeAnchor is the anchor on which to draw the nodes
   */
  public static void drawLocations(List<NodeEntity> allNodes, AnchorPane nodeAnchor) {

    LocationsDraw.drawLocations(allNodes, nodeAnchor, SCALE_FACTOR, false);
  }

  /**
   * Draws the path across the five anchorPanes
   *
   * @param aps is an array of the anchorPanes for all 5 floors in question
   * @param path is the arrayList of GraphNodes representing the path to take
   */
  public static void drawPath(AnchorPane[] aps, ArrayList<GraphNode> path) {
    PathDraw.drawPath(aps, path, radius, SCALE_FACTOR);
  }

  /**
   * Draws service request icons that are clickable
   *
   * @includes call to squareChangeColor
   * @param anchorPane is the current floor's anchorPane to draw the serviceRequest icons on
   * @param floor is the tableview string for the curren floor
   */
  public static void drawServiceRequestIcons(AnchorPane anchorPane, String floor) {
    anchorPane.getChildren().clear();
    List<MoveEntity> moves = FacadeRepository.getInstance().getAllMove();

    for (MoveEntity move : moves) {
      if (FacadeRepository.getInstance()
                  .getRequestAtLocation(move.getLocationName().getLongname())
                  .size()
              > 0
          && floor.equals(move.getNode().getFloor())) {
        double[] updatedCoords =
            scaleCoordinates(move.getNode().getXcoord(), move.getNode().getYcoord(), SCALE_FACTOR);

        Rectangle rect = new Rectangle(updatedCoords[0], updatedCoords[1], width + 5, width + 5);
        rect.setFill(Color.web("0x6143D7"));

        rect.setOnMouseClicked(squareChangeColor(anchorPane, floor));

        anchorPane.getChildren().add(rect);
      }
    }
  }

  /**
   * Changes color of the square service request icon on click and resets previously selected square
   * color
   *
   * @includes call to addSRPopup
   * @return a mouse event handler that changes the color of a square on click
   */
  private static EventHandler<MouseEvent> squareChangeColor(AnchorPane anchorPane, String floor) {
    EventHandler<MouseEvent> eventHandler =
        t -> {
          if (t.getSource() instanceof Rectangle) {

            if (previousRect != null) {
              previousRect.setFill(Color.web("0x6143D7"));
            }
            Rectangle rect = ((Rectangle) (t.getSource()));
            rect.setFill(Color.web("0xbc8fff"));
            previousRect = rect;

            addSRPopup(
                anchorPane,
                ((Rectangle) t.getSource()).getX(),
                ((Rectangle) t.getSource()).getY(),
                floor);
          }
        };
    return eventHandler;
  }

  /**
   * Builds popup for the selected service request
   *
   * @param anchorPane the anchorPane of the current floor
   * @param xCoord is the xCoord of the serviceRequest
   * @param yCoord is the yCoord of the serviceRequest
   * @param floor is the tableString floor of the serviceRequest
   */
  private static void addSRPopup(
      AnchorPane anchorPane, double xCoord, double yCoord, String floor) {

    double[] invertedCoords = scaleCoordinatesReversed(xCoord, yCoord, SCALE_FACTOR);

    if (previousPopup != null) {
      previousPopup.hide();
    }
    PopOver popover;

    try {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource(Screen.SR_POPUP.getFilename()));
      popover = new PopOver(loader.load());
      popover.setTitle("Service Requests");
      // popover.detach();
      popover.setArrowSize(0);
      popover.setHeaderAlwaysVisible(true);
      final DisplayServiceRequestsPopupController controller = loader.getController();

      // Set text boxes on popup --------
      List<ServiceRequestEntity> requests =
          FacadeRepository.getInstance()
              .getRequestAtCoordinate(
                  (int) Math.round(invertedCoords[0]), (int) Math.round(invertedCoords[1]), floor);
      String[] texts = new String[4];
      // IdNumberHolder holder = IdNumberHolder.getInstance();
      for (int i = 0; i < Math.min(requests.size(), 4); i++) {
        texts[i] =
            requests.get(i).getRequestType().toString() + " : " + requests.get(i).getDescription();
      }
      controller.addText(texts);

      if (requests.size() > 0) {
        String location = requests.get(0).getLocation().getShortname();
        if (requests.get(0).getLocation() != null) {
          controller.setMainText(location);
        } else {
          controller.setMainText("Service Requests");
        }
      }
      // end set text boxes -------

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final Point location = MouseInfo.getPointerInfo().getLocation();
    popover.show(anchorPane, location.getX(), location.getY());
    previousPopup = popover;
    // AApp.getPrimaryStage();
  }
}
