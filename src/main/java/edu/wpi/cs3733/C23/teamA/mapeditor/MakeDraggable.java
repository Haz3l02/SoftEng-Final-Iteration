package edu.wpi.cs3733.C23.teamA.mapeditor;

import javafx.scene.Node;
import net.kurobako.gesturefx.GesturePane;

public class MakeDraggable {

  private double mouseX;
  private double mouseY;

  GesturePane gp = new GesturePane();

  public void makeDraggable(Node node) {

    gp.setGestureEnabled(false);
    node.setOnMousePressed(
        mouseEvent -> {
          mouseX = mouseEvent.getX();
          mouseY = mouseEvent.getY();
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          node.setLayoutX(mouseEvent.getSceneX() - mouseX);
          node.setLayoutY(mouseEvent.getSceneY() - mouseY);
        });
  }
}
