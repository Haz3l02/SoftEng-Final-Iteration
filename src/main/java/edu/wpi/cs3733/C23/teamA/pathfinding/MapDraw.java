package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.controllers.DisplayServiceRequestsPopupController;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
  private static PopOver previousPopup;

  // hospital image aspect ratio: 25:17 (original size: 5000 x 3400)
  // hospital image scale factor to fit on screen (popover - 1250 x 850): 25% (0.25)
  // hospital image scale factor for our prototype (on-page - 250 x 170): 5% (0.05)

  public static void drawLocations(
      List<NodeEntity> allNodes, double scaleFactor, AnchorPane nodeAnchor) {

    nodeAnchor.getChildren().clear();

    for (NodeEntity n : allNodes) {
      double[] updatedCoords = scaleCoordinates(n.getXcoord(), n.getYcoord(), scaleFactor);

      if (!(FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()) == null)) {
        Text locName = new Text();
        locName.setVisible(true);
        locName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 5));
        locName.setText(
            FacadeRepository.getInstance().moveMostRecentLoc(n.getNodeid()).getShortname());
        locName.setLayoutX(updatedCoords[0] - 2.5);
        locName.setLayoutY(updatedCoords[1] - 2.5);
        nodeAnchor.getChildren().add(locName);
      }
    }
  }

  public static double[] scaleCoordinates(double xCoord, double yCoord, double scaleFactor) {
    // get the coordinates from the node

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = (xCoord) * scaleFactor;
    yCoord = (yCoord) * scaleFactor;

    // put the values in an array to return
    double[] scaledCoordinates = {xCoord, yCoord};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  /**
   * @param xCoord the x-coordinate to scale
   * @param yCoord the y-coordinate to scale
   * @param scaleFactor the scale factor for the coordinates and the image they are being placed on
   * @return an int array with the pair of new coordinates
   */
  private static double[] scaleCoordinatesReversed(
      double xCoord, double yCoord, double scaleFactor) {
    // get the coordinates from the node

    // apply the scale factor to the coordinates and floor them (so they remain a whole number)
    xCoord = (xCoord / scaleFactor);
    yCoord = (yCoord / scaleFactor);

    // put the values in an array to return
    double[] scaledCoordinates = {xCoord, yCoord};

    // return the scaled coordinates
    return scaledCoordinates;
  }

  //  public static void drawPathOld2(
  //      GraphicsContext[] gcs, ArrayList<GraphNode> path, double scaleFactor) {
  //
  //    for (GraphicsContext gc : gcs) {
  //      gc.setFill(Color.web("0x224870"));
  //      gc.setStroke(Color.web("0x224870"));
  //      gc.setLineWidth(2);
  //    }
  //
  //    // coordinates for the previous point in the path
  //    int prevX = 0;
  //    int prevY = 0;
  //    int prevFloor = 0;
  //
  //    // set the prev values and draw the starting circle
  //    int size = path.size();
  //    if (size > 0) {
  //      int[] updatedCoords =
  //          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), scaleFactor);
  //      prevX = updatedCoords[0];
  //      prevY = updatedCoords[1];
  //      String floor = path.get(0).getFloor();
  //      prevFloor = Floor.indexFromTableString(floor);
  //      gcs[prevFloor].fillOval(prevX - 5, prevY - 5, 10, 10); // starting circle
  //    }
  //
  //    // current holders for coordinates
  //    int currentX;
  //    int currentY;
  //    int currentFloor;
  //
  //    // get all node x and y coords to draw lines between them
  //    for (GraphNode g : path) {
  //      int[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), scaleFactor);
  //      currentX = updatedCoords[0];
  //      currentY = updatedCoords[1];
  //      currentFloor = Floor.indexFromTableString(g.getFloor());
  //
  //      if (currentFloor == prevFloor) {
  //        gcs[currentFloor].strokeLine(prevX, prevY, currentX, currentY);
  //      }
  //      prevX = currentX;
  //      prevY = currentY;
  //      prevFloor = currentFloor;
  //    }
  //
  //    gcs[prevFloor].strokeOval(prevX - 5, prevY - 5, 10, 10); // ending open circle
  //  }

  public static void drawPathClickable(
      AnchorPane[] aps,
      ArrayList<GraphNode> path,
      ArrayList<String> floorPath,
      double scaleFactor) {

    // coordinates for the previous point in the path
    double prevX = 0;
    double prevY = 0;
    int prevFloor = 0;

    // set the prev values and draw the starting circle
    int size = path.size();

    // get start node
    if (size > 0) {
      double[] updatedCoords =
          scaleCoordinates(path.get(0).getXCoord(), path.get(0).getYCoord(), scaleFactor);
      prevX = updatedCoords[0];
      prevY = updatedCoords[1];
      String floor = path.get(0).getFloor();
      prevFloor = Floor.indexFromTableString(floor);
      Circle currentCircle =
          new Circle(prevX, prevY, radius, Color.web("0x3DA867")); // starting circle
      List<ServiceRequestEntity> srs = FacadeRepository.getInstance().getAllServiceRequest();

      aps[prevFloor].getChildren().add(currentCircle);
    }

    // current holders for coordinates
    double currentX;
    double currentY;
    int currentFloor;
    int floorTracker = 0; // corresponds to an index in the floorPath()

    // get all node x and y coords to draw lines between them
    for (GraphNode g : path) {
      double[] updatedCoords = scaleCoordinates(g.getXCoord(), g.getYCoord(), scaleFactor);
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
      Circle currentCircle;
      // janky but maybe ok?
      if (floorTracker < floorPath.size() - 1) {
        if (!floorPath.get(floorTracker + 1).equals(g.getFloor())) {
          currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
        } else {
          currentCircle = new Circle(currentX, currentY, radius, Color.web("0xFFD470"));
          floorTracker++;
        }
      } else {
        currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
      }
      // currentCircle = new Circle(currentX, currentY, radius, Color.web("0x224870"));
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

    Circle currentCircle = new Circle(prevX, prevY, radius, Color.web("0xAD663D"));
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
        double[] updatedCoords =
            scaleCoordinates(move.getNode().getXcoord(), move.getNode().getYcoord(), scaleFactor);

        System.out.println("Real values");
        System.out.println(move.getNode().getXcoord());
        System.out.println(move.getNode().getYcoord());
        System.out.println("Updated");
        System.out.println(updatedCoords[0]);
        System.out.println(updatedCoords[1]);

        Rectangle rect = new Rectangle(updatedCoords[0], updatedCoords[1], width + 5, width + 5);
        rect.setFill(Color.web("0x000000"));

        rect.setOnMouseClicked(squareChangeColor(anchorPane, scaleFactor));

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
  private static EventHandler<MouseEvent> squareChangeColor(
      AnchorPane anchorPane, double scaleFactor) {
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
              //              int[] updatedCoords =
              //                  scaleCoordinates(
              //                      ((Rectangle) t.getSource()).getX(),
              //                      ((Rectangle) t.getSource()).getY(),
              //                      scaleFactor);
              //              System.out.println(((Rectangle) t.getSource()).getX());
              //              System.out.println(((Rectangle) t.getSource()).getY());
              //              final Point location = MouseInfo.getPointerInfo().getLocation();
              String text = "Service request";
              addSRPopup(
                  anchorPane,
                  ((Rectangle) t.getSource()).getX(),
                  ((Rectangle) t.getSource()).getY(),
                  "L2",
                  scaleFactor);
            }
          }
        };
    return eventHandler;
  }

  public static void addSRLabel(AnchorPane anchorPane, double xcoord, double ycoord, String text) {
    if (previousLabel != null) {
      previousLabel.setVisible(false);
    }

    // PopOver popover = new PopOver();
    Label label = new Label(text);
    label.setVisible(true);
    // label.setBackground(Background.fill(Color.web("0xffffff")));
    label.setTranslateX(xcoord - 2);
    label.setTranslateY(ycoord + 10);
    previousLabel = label;
    anchorPane.getChildren().add(label);
    // AApp.getPrimaryStage();
  }

  public static void addSRPopup(
      AnchorPane anchorPane, double xcoord, double ycoord, String floor, double scaleFactor) {

    double[] invertedCoords = scaleCoordinatesReversed(xcoord, ycoord, scaleFactor);

    if (previousPopup != null) {
      previousPopup.hide();
    }
    PopOver popover;

    try {
      FXMLLoader loader =
          new FXMLLoader(Main.class.getResource("views/DisplayServiceRequestsFXML.fxml"));
      popover = new PopOver(loader.load());
      // popover.setContentNode(loader.load());
      popover.setTitle("Service Request Popover");
      // popover.detach();
      popover.setArrowSize(0);
      // popover.show((anchorPane.getScene().getWindow()));
      popover.setHeaderAlwaysVisible(true);

      final DisplayServiceRequestsPopupController controller = loader.getController();

      System.out.println("Updated2");
      System.out.println(xcoord);
      System.out.println(ycoord);
      System.out.println("Estimated hopefully");
      System.out.println((int) Math.round(invertedCoords[0]));
      System.out.println((int) Math.round(invertedCoords[1]));
      List<ServiceRequestEntity> requests =
          FacadeRepository.getInstance()
              .getRequestAtCoordinate(
                  (int) Math.round(invertedCoords[0]), (int) Math.round(invertedCoords[1]), floor);

      String[] texts = new String[4];
      for (int i = 0; i < Math.min(requests.size(), 4); i++) {
        texts[i] = requests.get(i).getName();
      }
      System.out.println(texts);
      controller.addText(texts);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final Point location = MouseInfo.getPointerInfo().getLocation();
    popover.show(anchorPane, location.getX(), location.getY());
    // AApp.getPrimaryStage();
  }
}
