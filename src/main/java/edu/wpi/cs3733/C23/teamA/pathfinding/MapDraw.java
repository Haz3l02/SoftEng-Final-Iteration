package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Class for Controller to call to add the map
public class MapDraw {

  // hospital image aspect ratio: 25:17 (original size: 5000 x 3400)
  // hospital image scale factor to fit on screen (popover - 1250 x 850): 25% (0.25)
  // hospital image scale factor for our prototype (on-page - 250 x 170): 5% (0.05)

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

    // coordinates for the previous point in the path
    int prevX = 0;
    int prevY = 0;

    // set the prev values and draw the starting circle
    int size = path.size();
    if (size > 0) {
      int[] updatedCoords = scaleCoordinates(path.get(0), 0.05);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      gc.fillOval(prevX - 4, prevY - 4, 8, 8); // starting circle
    }

    // current holders for coordinates
    int currentX;
    int currentY;

    // get all node x and y coords to draw lines between them
    for (GraphNode g : path) {
      int[] updatedCoords = scaleCoordinates(g, 0.05);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];

      gc.strokeLine(prevX, prevY, currentX, currentY);
      prevX = currentX;
      prevY = currentY;
    }
    gc.strokeOval(prevX - 2.5, prevY - 2.5, 5, 5); // ending open circle
  }
}
