package edu.wpi.cs3733.C23.teamA.mapdrawing;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinates;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class PathDraw {

  public static void drawPath(
      AnchorPane[] aps, ArrayList<GraphNode> path, double radius, double SCALE_FACTOR) {

    // coordinates for the previous point in the path
    double prevX = 0;
    double prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();

    // get start node
    if (size > 0) {
      double[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), SCALE_FACTOR);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);
      Circle currentCircle =
          new Circle(prevX, prevY, radius, Color.web("0x279F89")); // starting circle

      aps[prevFloor].getChildren().add(currentCircle);
    }

    // current holders for coordinates
    double currentX;
    double currentY;
    int currentFloor;

    // get all node x and y coords to draw lines between them
    for (int i = 1; i < size; i++) {
      GraphNode g = path.get(i);

      double[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), SCALE_FACTOR);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];
      currentFloor = Floor.indexFromTableString(g.getFloor());

      // draw line on floor
      if (currentFloor == prevFloor) {
        Line currentLine = new Line(prevX, prevY, currentX, currentY);
        currentLine.setStroke(Color.web("0x224870"));
        aps[currentFloor].getChildren().add(currentLine);
      }

      if (i < (size - 1)) {
        // draw node
        GraphNode next = path.get(i + 1);
        Circle currentCircle;

        if (!g.getFloor().equals(next.getFloor())) {
          currentCircle = new Circle(currentX, currentY, radius, Color.web("0xf6bd3a"));
        } else {
          currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
        }
        aps[currentFloor].getChildren().add(currentCircle);
      }

      prevX = currentX;
      prevY = currentY;
      prevFloor = currentFloor;
    }

    // draw the ending node
    Circle currentCircle = new Circle(prevX, prevY, radius, Color.web("0xf63c3c"));
    aps[prevFloor].getChildren().add(currentCircle); // ending open circle
  }

  public static void drawPathLines(
      AnchorPane[] aps, ArrayList<GraphNode> path, double radius, double SCALE_FACTOR) {

    // coordinates for the previous point in the path
    double prevX = 0;
    double prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();

    // current holders for coordinates
    double currentX;
    double currentY;
    int currentFloor;

    // get all node x and y coords to draw lines between them
    for (int i = 1; i < size; i++) {
      GraphNode g = path.get(i);

      double[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), SCALE_FACTOR);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];
      currentFloor = Floor.indexFromTableString(g.getFloor());

      // draw line on floor
      if (currentFloor == prevFloor) {
        Line currentLine = new Line(prevX, prevY, currentX, currentY);
        currentLine.setStroke(Color.web("0x224870"));
        currentLine.setStrokeWidth(1.7);
        aps[currentFloor].getChildren().add(currentLine);
      } else { // draw starting blue circle on floor
        Circle currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
        aps[currentFloor].getChildren().add(currentCircle);
      }

      // draw final yellow circle on this floor
      if (i < (size - 1)) {
        GraphNode next = path.get(i + 1);

        if (!g.getFloor().equals(next.getFloor())) {
          Circle currentCircle = new Circle(currentX, currentY, radius, Color.web("0xf6bd3a"));
          aps[currentFloor].getChildren().add(currentCircle);
        }
      }

      prevX = currentX;
      prevY = currentY;
      prevFloor = currentFloor;
    }

    // draw the ending node
    Circle endCircle = new Circle(prevX, prevY, radius, Color.web("0xf63c3c"));
    aps[prevFloor].getChildren().add(endCircle); // ending open circle

    // get start node
    if (size > 0) {
      double[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), SCALE_FACTOR);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);
      Circle currentCircle =
          new Circle(prevX, prevY, radius, Color.web("0x279F89")); // starting circle

      aps[prevFloor].getChildren().add(currentCircle);
    }
  }
}
