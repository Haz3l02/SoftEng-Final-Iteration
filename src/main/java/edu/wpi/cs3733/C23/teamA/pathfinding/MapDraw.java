package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;

// Class for Controller to call to add the map
public class MapDraw {

  static Pane previousSR = null;
  static Pane selectSRPane = null;
  static NodeEntity selectNode = null;

  private IdNumberHolder holder = IdNumberHolder.getInstance();

  private static final double radius = 3;
  private static final double width = 6;

  private static Rectangle previousRect;
  private static Label previousLabel;

  // hospital image aspect ratio: 25:17 (original size: 5000 x 3400)
  // hospital image scale factor to fit on screen (popover - 1250 x 850): 25% (0.25)
  // hospital image scale factor for our prototype (on-page - 250 x 170): 5% (0.05)

  /**
   * @param xCoord the x-coordinate to scale
   * @param yCoord the y-coordinate to scale
   * @param scaleFactor the scale factor for the coordinates and the image they are being placed on;
   *     for us, this is going to be 0.30 (subject to change)
   * @return an int array with the pair of new coordinates
   */
  private static int[] scaleCoordinates(double xCoord, double yCoord, double scaleFactor) {
    // get the coordinates from the node

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = Math.floor(xCoord * scaleFactor);
    yCoord = Math.floor(yCoord * scaleFactor);

    // put the values in an array to return
    int[] scaledCoordinates = {(int) xCoord, (int) yCoord};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  public static void drawPathOld2(
      GraphicsContext[] gcs, ArrayList<GraphNode> path, double scaleFactor) {

    for (GraphicsContext gc : gcs) {
      gc.setFill(Color.web("0x224870"));
      gc.setStroke(Color.web("0x224870"));
      gc.setLineWidth(2);
    }

    // coordinates for the previous point in the path
    int prevX = 0;
    int prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();
    if (size > 0) {
      int[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), scaleFactor);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);
      gcs[prevFloor].fillOval(prevX - 5, prevY - 5, 10, 10); // starting circle
    }

    // current holders for coordinates
    int currentX;
    int currentY;
    int currentFloor;

    // get all node x and y coords to draw lines between them
    for (GraphNode g : path) {
      int[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), scaleFactor);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];
      currentFloor = Floor.indexFromTableString(g.getFloor());

      if (currentFloor == prevFloor) {
        gcs[currentFloor].strokeLine(prevX, prevY, currentX, currentY);
      }
      prevX = currentX;
      prevY = currentY;
      prevFloor = currentFloor;
    }

    gcs[prevFloor].strokeOval(prevX - 5, prevY - 5, 10, 10); // ending open circle
  }

  public static void drawPathClickable(
      AnchorPane[] aps, ArrayList<GraphNode> path, double scaleFactor) {

    // coordinates for the previous point in the path
    int prevX = 0;
    int prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();

    // get start node
    if (size > 0) {
      int[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), scaleFactor);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);
      Circle currentCircle =
          new Circle(prevX, prevY, radius, Color.web("0x224870")); // starting circle
      List<ServiceRequestEntity> srs = FacadeRepository.getInstance().getAllServiceRequest();

      //      if
      // (FacadeRepository.getInstance().getRequestAtLocation(path.get(0).getLongName()).size()
      //          > 0) {
      //        Rectangle rect = new Rectangle(prevX + 5, prevY + 5, radius + 1, radius + 1);
      //        rect.setFill(Color.web("0x000000"));
      //        // group.getChildren().add(currentCircle);
      //        groups[prevFloor].getChildren().add(rect); // service request icon
      //      }

      aps[prevFloor].getChildren().add(currentCircle);
    }

    // current holders for coordinates
    int currentX;
    int currentY;
    int currentFloor;

    // get all node x and y coords to draw lines between them
    for (GraphNode g : path) {
      int[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), scaleFactor);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];
      currentFloor = Floor.indexFromTableString(g.getFloor());

      // draw line on floor
      if (currentFloor == prevFloor) {
        Line currentLine = new Line(prevX, prevY, currentX, currentY);
        currentLine.setStroke(Color.web("0x224870"));
        aps[currentFloor].getChildren().add(currentLine);
      }

      // draw node
      Circle currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
      aps[currentFloor].getChildren().add(currentCircle);
      //      if (FacadeRepository.getInstance().getRequestAtLocation(g.getLongName()).size() > 0) {
      //        Rectangle rect = new Rectangle(currentX + 5, currentY - 5, radius + 1, radius + 1);
      //        rect.setFill(Color.web("0x000000"));
      //        groups[currentFloor].getChildren().add(rect);
      //      }
      prevX = currentX;
      prevY = currentY;
      prevFloor = currentFloor;
    }

    Circle currentCircle = new Circle(prevX, prevY, radius, Color.web("0x224870"));
    aps[prevFloor].getChildren().add(currentCircle); // ending open circle

    //    for (int i = 0; i < 5; i++) {
    //      aps[i].getChildren().add(groups[i]);
    //    }
  }

  public static void drawServiceRequestIcons(
      AnchorPane anchorPane, double scaleFactor, String floor) {
    anchorPane.getChildren().clear();
    List<MoveEntity> moves = FacadeRepository.getInstance().getAllMove();

    for (MoveEntity move : moves) {
      if (FacadeRepository.getInstance()
                  .getRequestAtLocation(move.getLocationName().getLongname())
                  .size()
              > 0
          && floor.equals(move.getNode().getFloor())) {
        int[] updatedCoords =
            scaleCoordinates(move.getNode().getXcoord(), move.getNode().getYcoord(), scaleFactor);

        Rectangle rect = new Rectangle(updatedCoords[0], updatedCoords[1], width + 1, width + 1);
        rect.setFill(Color.web("0x000000"));

        rect.setOnMouseClicked(squareChangeColor(anchorPane));

        anchorPane.getChildren().add(rect);
      }
    }
  }

  /**
   * Changes color of the square service request icon on click and resets previously selected square
   * color
   *
   * @return a mouse event handler that changes the color of a square on click
   */
  private static EventHandler<MouseEvent> squareChangeColor(AnchorPane anchorPane) {
    EventHandler<MouseEvent> eventHandler =
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {

            if (t.getSource() instanceof Rectangle) {

              if (previousRect != null) {
                previousRect.setFill(Color.web("0x000000"));
              }
              Rectangle rect = ((Rectangle) (t.getSource()));
              rect.setFill(Color.web("0x00FF00"));
              previousRect = rect;
              addSRLabel(
                  anchorPane,
                  ((Rectangle) t.getSource()).getX(),
                  ((Rectangle) t.getSource()).getY());
            }
          }
        };
    return eventHandler;
  }

  public static void addSRLabel(AnchorPane anchorPane, double xcoord, double ycoord) {
    PopOver popover = new PopOver();
    Label label = new Label("My Service Request");
    label.setTranslateX(xcoord);
    label.setTranslateY(ycoord + 5);
    System.out.println(xcoord);
    System.out.println(ycoord);
    previousLabel = label;
    // Circle circ = new Circle(xcoord, ycoord, 10);
    // anchorPane.getChildren().add(circ);
  }
}
