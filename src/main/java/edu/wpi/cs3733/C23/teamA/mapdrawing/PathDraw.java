package edu.wpi.cs3733.C23.teamA.mapdrawing;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinates;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PathDraw {

  /**
   * Used by PathfindingSystem to draw a path onto five anchorPanes for multiple controllers with
   * all nodes drawn and lines between nodes on the same floor
   *
   * @param aps is AnchorPane[] of length 5 where [0] is L1, [1] is L2, [2] is F1, [3] is F2, [4] is
   *     F3
   * @param path is the ArrayList<GraphNode> representing a path, typically gotten from running a
   *     pathfinding algorithm
   * @param radius is the radius of the circles on the graph (use a value between 3 - 10)
   * @param SCALE_FACTOR is the double to multiply the coordinates by to get draw the nodes at the
   *     right place (is typically 0.135 or 0.15) *depends on the size of the anchorPanes and
   *     imageView
   */
  public static void drawPath(
      AnchorPane[] aps, ArrayList<GraphNode> path, double radius, double SCALE_FACTOR) {

    // coordinates for the previous point in the path
    double prevX = 0;
    double prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();

    // draw start node
    if (size > 0) {
      // get updated coordinates and floor
      double[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), SCALE_FACTOR);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);

      // draw and add the circle
      Circle currentCircle =
          new Circle(prevX, prevY, radius, Color.web("0x279F89")); // starting circle
      aps[prevFloor].getChildren().add(currentCircle);
    }

    // current holders for coordinates
    double currentX;
    double currentY;
    int currentFloor;

    // cycle through all the nodes
    for (int i = 1; i < size; i++) {
      GraphNode g = path.get(i);

      // get new coordinates and floor
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

      // draw correct colored circle on floor
      if (i < (size - 1)) {
        GraphNode next = path.get(i + 1);
        Circle currentCircle;

        if (!g.getFloor().equals(next.getFloor())) { // draw yellow circle to show change in floor
          currentCircle = new Circle(currentX, currentY, radius, Color.web("0xf6bd3a"));
        } else { // draw regular blue circle
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
    aps[prevFloor].getChildren().add(currentCircle);
  }

  /**
   * Used by PathfindingSystem to draw a path onto five anchorPanes for multiple controllers with
   * start and end nodes on each floor, but lines in between to show path
   *
   * @param aps is AnchorPane[] of length 5 where [0] is L1, [1] is L2, [2] is F1, [3] is F2, [4] is
   *     F3
   * @param path is the ArrayList<GraphNode> representing a path, typically gotten from running a
   *     pathfinding algorithm
   * @param radius is the radius of the circles on the graph (use a value between 3 - 10)
   * @param SCALE_FACTOR is the double to multiply the coordinates by to get draw the nodes at the
   *     right place (is typically 0.135 or 0.15) *depends on the size of the anchorPanes and
   *     imageView
   */
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

    // cycle through all the nodes
    for (int i = 1; i < size; i++) {
      GraphNode g = path.get(i);

      // update coords and floor
      double[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), SCALE_FACTOR);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];
      currentFloor = Floor.indexFromTableString(g.getFloor());

      // draw line on floor
      if (currentFloor == prevFloor) {
        Line currentLine = new Line(prevX, prevY, currentX, currentY);
        currentLine.setStroke(Color.web("0x224870"));
        currentLine.setStrokeWidth(1.7); // make line thicker
        aps[currentFloor].getChildren().add(currentLine);
      } else { // draw starting blue circle on floor
        Circle currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
        aps[currentFloor].getChildren().add(currentCircle);
      }

      // draw final yellow circle on this floor if it is about to change floors
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

  /**
   * Method used by MoveController to draw a single node Note: This method does not alter the show
   * or hide any of the anchor panes
   *
   * @param aps an array of anchor panes of size five with different floors
   * @param g a graph node to be drawn
   * @param message puts a text above the node with the given message
   */
  public static void drawSingleNode(AnchorPane[] aps, GraphNode g, String message) {
    // draw the node
    double[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), 0.135);
    Circle currentCircle = new Circle(updatedCoords[0], updatedCoords[1], 6, Color.web("0xf63c3c"));
    aps[Floor.indexFromTableString(g.getFloor())]
        .getChildren()
        .add(currentCircle); // ending open circle

    // Add text
    Text text = new Text();
    text.setVisible(true);
    text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 8));
    text.setText(message);

    // get the coordinates
    text.setLayoutX(updatedCoords[0] - 20);
    text.setLayoutY(updatedCoords[1] - 6);
    aps[Floor.indexFromTableString(g.getFloor())].getChildren().add(text);
  }
}
