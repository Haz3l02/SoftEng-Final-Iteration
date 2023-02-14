package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

public class NodeMapController extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;
  @FXML private GesturePane gPane;
  @FXML StackPane sPane;

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

  @FXML MFXTextField XCord;
  @FXML MFXTextField YCord;
  @FXML MFXComboBox FloorBox;

  @FXML MFXComboBox BuildingBox;

  Pane newCircle = new Pane();

  @FXML VBox fieldBox;
  @FXML MFXButton createNodeButton;

  @Setter NodeEntity selectedNode = null;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;

  // scaling constant
  private double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulation

  public void initialize() {
    createNodeButton.setVisible(false);
    initializeFloorMap("L1", nodeAnchorL1, stackL1, gestureL1);
    initializeFloorMap("L2", nodeAnchorL2, stackL2, gestureL2);
    initializeFloorMap("1", nodeAnchorF1, stackF1, gestureF1);
    initializeFloorMap("2", nodeAnchorF2, stackF2, gestureF2);
    initializeFloorMap("3", nodeAnchorF3, stackF3, gestureF3);
  }
  /**
   * Attaches the gesturepane with the stackpane and reads and adds all the nodes on a floor to the
   * correct anchorPane
   */
  private void initializeFloorMap(
      String floor, AnchorPane nodeAnchor, StackPane stack, GesturePane gesture) {
    // Get all nodes on floor names floor
    NodeImpl nodeimpl = new NodeImpl();
    allNodes = nodeimpl.getAll();

    // Add nodes as circles
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, nodeAnchor, this);

    Node node = stack;
    gesture.setContent(node);
    gesture.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    System.out.println("Assigned the stackPane to gesture");

    //    nodeAnchor.setOnMouseClicked(
    //        e -> {
    //          int x = (int) e.getX();
    //          int y = (int) e.getY();
    //
    //          if (e.getButton() == MouseButton.PRIMARY) {
    //
    //          } else if (e.getButton() == MouseButton.SECONDARY) {
    //            final Pane nodeGraphic = new Pane();
    //
    //            /* Set the style of the node */
    //            nodeGraphic.setPrefSize(4, 4);
    //            nodeGraphic.setLayoutX(x - 4);
    //            nodeGraphic.setLayoutY(y - 4);
    //            nodeGraphic.setStyle(
    //                "-fx-background-color: '#F6BD38'; "
    //                    + "-fx-background-radius: 12.5; "
    //                    + "-fx-border-color: '013A75'; "
    //                    + "-fx-border-width: 1;"
    //                    + "-fx-border-radius: 12.5");
    //
    //            nodeAnchor.getChildren().add(nodeGraphic);
    //          }
    //
    //          NodeEntity n = new NodeEntity();
    //          n.setNodeid(makeNewNodeID(floor, x, y));
    //          n.setXcoord(x);
    //          n.setYcoord(y);
    //          n.setFloor(floor);
    //          n.setBuilding("Tower");
    //          NodeImpl newNode = new NodeImpl();
    //          newNode.add(n);
    //
    //          // initialize();
    //
    //        });
  }

  @FXML
  public void switchToNodeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.NODE);
  }

  public String makeNewNodeID(String floor, int x, int y) {
    String xCoord = String.format("%04d", x);
    String yCoord = String.format("%04d", y);

    return (floor + "X" + xCoord + "Y" + yCoord);
  }

  public static String toString(char[] a) {
    // Creating object of String class
    String string = new String(a);

    return string;
  }

  public void deleteSelectedNode(ActionEvent event) throws IOException {
    NodeEntity currentNode = NodeDraw.getSelected();
    Pane currentNodePane = NodeDraw.getSelectedPane();
    String id = currentNode.getNodeid();
    System.out.println(id);
    NodeImpl newNode = new NodeImpl();
    newNode.delete(id);
    currentNodePane.setVisible(false);
    // initialize();
  }

  public void newNode(ActionEvent event) {
    XCord.clear();
    YCord.clear();
    FloorBox.clear();
    BuildingBox.clear();

    ObservableList<String> floors =
        FXCollections.observableArrayList(
            Floor.L1.getExtendedString(),
            Floor.L2.getExtendedString(),
            Floor.F1.getExtendedString(),
            Floor.F2.getExtendedString(),
            Floor.F3.getExtendedString());
    FloorBox.setItems(floors);

    ObservableList<String> buildings =
        FXCollections.observableArrayList(
            Building.FR45.getTableString(),
            Building.TOWR.getTableString(),
            Building._BTM.getTableString(),
            Building.SHPR.getTableString(),
            Building.FR15.getTableString());
    BuildingBox.setItems(buildings);

    fieldBox.setStyle("-fx-background-color: '013A75'; ");
    createNodeButton.setVisible(true);
    //
    //    newNode.setXcoord(Integer.parseInt(XCord.getText()));
    //    newNode.setYcoord(Integer.parseInt(YCord.getText()));
    //

  }

  public void createNode(ActionEvent event) {

    NodeEntity newNode = new NodeEntity();
    NodeImpl newNodeCreation = new NodeImpl();
    fieldBox.setStyle("-fx-background-color: '013A75'; ");

    newNode.setXcoord(Integer.parseInt(XCord.getText()));
    newNode.setYcoord(Integer.parseInt(YCord.getText()));
    newNode.setFloor(FloorBox.getText());
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(
        makeNewNodeID(
            Floor.fromString(newNode.getFloor()), newNode.getXcoord(), newNode.getYcoord()));

    System.out.println("X: " + newNode.getXcoord());
    System.out.println("Y: " + newNode.getYcoord());
    System.out.println("Floor: " + newNode.getFloor());
    System.out.println("Building: " + newNode.getBuilding());
    System.out.println("ID: " + newNode.getNodeid());

    newNodeCreation.add(newNode);
    createNodeButton.setVisible(false);
    fieldBox.setStyle("-fx-background-color: '#bad1ea'; ");

    initialize();
  }

  public void setXCord(String xLoc) {
    this.XCord.setText(xLoc);
  }

  public void setYCord(String yLoc) {
    this.YCord.setText(yLoc);
  }

  public void setFloorBox(String floor) {
    this.FloorBox.setValue(floor);
  }

  public void setBuildingBox(String building) {
    this.BuildingBox.setValue(building);
  }
}
