package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

// Class for Controller to call to add the map
public class MapDraw {

  private static double radius = 3;

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
      AnchorPane[] aps, ArrayList<GraphNode> path, double scaleFactor, Group[] groups) {

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

      if (FacadeRepository.getInstance().getRequestAtLocation(path.get(0).getLongName()).size()
          > 0) {
        Circle otherCircle = new Circle(prevX + 5, prevY, radius, Color.web("0x000000"));
        // group.getChildren().add(currentCircle);
        groups[prevFloor].getChildren().add(otherCircle); // service request icon
      }

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
      if (FacadeRepository.getInstance().getRequestAtLocation(g.getLongName()).size() > 0) {
        Circle otherCircle = new Circle(currentX + 5, currentY, radius, Color.web("0x000000"));
        groups[currentFloor].getChildren().add(otherCircle);
      }
      prevX = currentX;
      prevY = currentY;
      prevFloor = currentFloor;
    }

    Circle currentCircle = new Circle(prevX, prevY, radius, Color.web("0x224870"));
    aps[prevFloor].getChildren().add(currentCircle); // ending open circle

    for (int i = 0; i < 5; i++) {
      aps[i].getChildren().add(groups[i]);
    }
  }
}
