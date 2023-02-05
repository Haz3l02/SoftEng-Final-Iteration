package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Class for Controller to call to add the map
public class MapDraw {

  // hospital image aspect ratio: 25:17 (original size: 5000 x 3400)
  // hospital image scale factor to fit on screen (popover): 30% (0.30)
  // hospital image scale factor fo

  /**
   * @param node the GraphNode you want the scaled coordinates for
   * @param scaleFactor the scale factor for the coordinates and the image they are being placed on;
   *     for us, this is going to be 0.30 (subject to change)
   * @return an int array with the pair of new coordinates
   */
  public static int[] scaleCoordinates(GraphNode node, double scaleFactor) {
    // get the coordinates from the node
    double xCoord = node.getXCoord();
    double yCoord = node.getYCoord();

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = Math.floor(xCoord * scaleFactor);
    yCoord = Math.floor(yCoord * scaleFactor);

    // put the values in an array to return
    int[] scaledCoordinates = {(int) xCoord, (int) yCoord};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  public static void drawShapes(GraphicsContext gc, ArrayList<GraphNode> path) {
    gc.setFill(Color.web("0x224870"));
    gc.setStroke(Color.web("0x224870"));
    gc.setLineWidth(2);

    // Circles for start and end node
    int prevX = 0;
    int prevY = 0;
    int size = path.size();
    if (size > 0) {
      prevX = path.get(0).getXCoord() / 20;
      prevY = path.get(0).getYCoord() / 20;
      gc.fillOval(prevX - 4, prevY - 4, 8, 8);
    }

    int currentX;
    int currentY;
    // get all node x and y coords
    for (GraphNode g : path) { //
      currentX = g.getXCoord() / 20;
      currentY = g.getYCoord() / 20;
      gc.strokeLine(prevX, prevY, currentX, currentY);
      prevX = currentX;
      prevY = currentY;
    }
    gc.strokeOval(prevX - 2.5, prevY - 2.5, 5, 5);
  }
}
