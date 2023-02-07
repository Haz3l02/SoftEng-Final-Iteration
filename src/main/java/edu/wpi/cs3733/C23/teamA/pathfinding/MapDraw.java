package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.databases.Node;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Class for Controller to call to add the map
public class MapDraw {

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

  public static void drawPath(GraphicsContext gc, ArrayList<GraphNode> path, double scaleFactor) {
    gc.setFill(Color.web("0x224870"));
    gc.setStroke(Color.web("0x224870"));
    gc.setLineWidth(2);

    // coordinates for the previous point in the path
    int prevX = 0;
    int prevY = 0;

    // set the prev values and draw the starting circle
    int size = path.size();
    if (size > 0) {
      int[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), scaleFactor);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      gc.fillOval(prevX - 6, prevY - 6, 12, 12); // starting circle
    }

    // current holders for coordinates
    int currentX;
    int currentY;

    // get all node x and y coords to draw lines between them
    for (GraphNode g : path) {
      int[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), scaleFactor);
      currentX = updatedCoords[0];
      currentY = updatedCoords[1];

      gc.strokeLine(prevX, prevY, currentX, currentY);
      prevX = currentX;
      prevY = currentY;
    }
    gc.strokeOval(prevX - 8, prevY - 8, 16, 16); // ending open circle
  }

  public static void drawNodes(GraphicsContext gc, ArrayList<Node> allNodes, double scaleFactor) {
    gc.setFill(Color.web("0x224870"));

    // draw circle for each node
    for (Node n : allNodes) {
      int[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);
      gc.fillOval(updatedCoords[0] - 4, updatedCoords[1] - 4, 8, 8);
    }
  }
}
