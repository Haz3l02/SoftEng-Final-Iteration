package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;
import org.hibernate.Session;

public class NodeMapController extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;
  @FXML private GesturePane gPane;
  @FXML StackPane sPane;

  @FXML Label loc;
  @FXML Label XCord;
  @FXML Label YCord;

  // Anchor panes
  @FXML AnchorPane nodeAnchorL1;
  @FXML AnchorPane nodeAnchorL2;
  @FXML AnchorPane nodeAnchorF1;
  @FXML AnchorPane nodeAnchorF2;
  @FXML AnchorPane nodeAnchorF3;

  // Stack panes
  @FXML StackPane stackL1;
  @FXML StackPane stackL2;
  @FXML StackPane stackF1;
  @FXML StackPane stackF2;
  @FXML StackPane stackF3;

  // Gesture panes
  @FXML GesturePane gestureL1;
  @FXML GesturePane gestureL2;
  @FXML GesturePane gestureF1;
  @FXML GesturePane gestureF2;
  @FXML GesturePane gestureF3;

  @Setter NodeEntity selectedNode = null;



  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;

  // scaling constant
  private double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulation

  public void initialize() {

    initializeFloorMap("L1", nodeAnchorL1, stackL1, gestureL1);
    initializeFloorMap("L2", nodeAnchorL2, stackL2, gestureL2);
    initializeFloorMap("F1", nodeAnchorF1, stackF1, gestureF1);
    initializeFloorMap("F2", nodeAnchorF2, stackF2, gestureF2);
    initializeFloorMap("F3", nodeAnchorF3, stackF3, gestureF3);
  }
  /**
   * Attaches the gesturepane with the stackpane and reads and adds all the nodes on a floor to the
   * correct anchorPane
   */
  private void initializeFloorMap(
      String floor, AnchorPane nodeAnchor, StackPane stack, GesturePane gesture) {
    // Get all nodes on floor names floor
    Session session = getSessionFactory().openSession();
    allNodes = NodeEntity.getNodeOnFloor(floor, session); // get all nodes from Database
    session.close();

    // Add nodes as circles
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, nodeAnchor, this);

    Node node = stack;
    gesture.setContent(node);
    gesture.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    System.out.println("Assigned the stackPane to gesture");

    nodeAnchor.setOnMouseClicked(
        e -> {
          int x = (int) e.getX();
          int y = (int) e.getY();

          if (e.getButton() == MouseButton.PRIMARY) {

          } else if (e.getButton() == MouseButton.SECONDARY) {
            final Pane nodeGraphic = new Pane();

            /* Set the style of the node */
            nodeGraphic.setPrefSize(4, 4);
            nodeGraphic.setLayoutX(x - 4);
            nodeGraphic.setLayoutY(y - 4);
            nodeGraphic.setStyle(
                "-fx-background-color: '#F6BD38'; "
                    + "-fx-background-radius: 12.5; "
                    + "-fx-border-color: '013A75'; "
                    + "-fx-border-width: 1;"
                    + "-fx-border-radius: 12.5");

            nodeAnchor.getChildren().add(nodeGraphic);
          }

          NodeEntity n = new NodeEntity();
          n.setXcoord(x);
          n.setYcoord(y);

        });
  }

  @FXML
  public void switchToNodeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.NODE);
  }

  public void setLoc(String location) {
    this.loc.setText(location);
  }

  public void setXCord(String x) {
    this.XCord.setText(x);
  }

  public void setYCord(String y) {
    this.YCord.setText(y);
  }
}
