package edu.wpi.cs3733.C23.teamA.mapdrawing;

/** Class called by NodeDraw and PathfindingDraw to scale coordinates for their maps */
public class CoordinateScalar {

  /**
   * Scales the coordinates down in size from their original coordinates by multiplying by a scale
   * factor
   *
   * @param xCoord the xCoord of a node
   * @param yCoord the yCoord of the same node
   * @param scaleFactor the factor to which to multiply these by
   * @return a double[] of length 2, where the first entry is the new xCoord and the second entry is
   *     the new yCoord
   */
  public static double[] scaleCoordinates(double xCoord, double yCoord, double scaleFactor) {
    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = (xCoord) * scaleFactor;
    yCoord = (yCoord) * scaleFactor;

    // return the scaled coordinates
    return new double[] {xCoord, yCoord};
  }

  /**
   * @param xCoord the x-coordinate to scale
   * @param yCoord the y-coordinate to scale
   * @return an int array with the pair of new coordinates
   */
  public static double[] scaleCoordinatesReversed(
      double xCoord, double yCoord, double scaleFactor) {
    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = (xCoord / scaleFactor);
    yCoord = (yCoord / scaleFactor);

    // return the scaled coordinates
    return new double[] {xCoord, yCoord};
  }
}
