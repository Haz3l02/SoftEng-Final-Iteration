package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinatesReversed;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.mapdrawing.NodeDraw;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

public class MapEditorController extends MenuController {

  // FXML Elements
  @FXML ImageView mainImageView = new ImageView();
  @FXML @Getter GesturePane mainGesturePane = new GesturePane();
  @FXML AnchorPane mainAnchorPane = new AnchorPane();
  @FXML AnchorPane mainSelectPane = new AnchorPane();
  @FXML AnchorPane edgeAnchorPane = new AnchorPane();
  @FXML StackPane mainStackPane = new StackPane();
  @FXML AnchorPane mainTextPane = new AnchorPane();

  @FXML ContextMenu contextMenu = new ContextMenu();

  // Buttons to switch pages
  @FXML MFXButton l1Button;
  @FXML MFXButton l2Button;
  @FXML MFXButton f1Button;
  @FXML MFXButton f2Button;
  @FXML MFXButton f3Button;

  // Buttons and Text
  @FXML MFXToggleButton toggleSwitch = new MFXToggleButton();

  @FXML
  Text reminder; // text field for a "remember to fill out all fields before submitting form" thingy

  Rectangle selectionRectangle = new Rectangle();

  @Setter NodeEntity selectedNode = null;

  // scaling constant
  private final double SCALE_FACTOR = 0.24; // constant for map size/coordinate manipulations

  private static PopOver nodeEditorPopup;
  private static PopOver edgeEditorPopup;
  private static PopOver locationEditorPopup;
  private static PopOver nodeEditorEditPopup;
  private static PopOver locationEditorEditPopup;
  private static PopOver straightSelectionPopup;

  static MapEditorController mapEditor;

  static List<NodeEntity> Nodes = new ArrayList<>();
  private MFXButton[] buttons = new MFXButton[5];

  private int previousFloor = 1;

  private double mouseDownX;
  private double mouseDownY;

  private double mouseDownReleasedX;
  private double mouseDownReleasedY;

  double mouseXCoord;
  double mouseYCoord;

  public static List<NodeEntity> getAllNodes() {
    return Nodes;
  }

  /** Starting method called when screen is opened: Draws nodes and edges for floor L1 */
  public void initialize() {

    mainSelectPane.getChildren().add(selectionRectangle);
    selectionRectangle.setStroke(Color.BLACK);
    selectionRectangle.setFill(Color.LIGHTBLUE);
    selectionRectangle.setOpacity(0.35);
    selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);

    mainGesturePane.setOnKeyPressed(
        event -> {
          if (event.getCode().equals(KeyCode.BACK_SPACE)
              || event.getCode().equals(KeyCode.DELETE)) {

            if (NodeDraw.getSelectedEdge() != null && NodeDraw.getSelected() == null) {
              NodeDraw.delEdge();
            } else if (NodeDraw.getSelected() != null && NodeDraw.getSelectedEdge() == null) {
              NodeDraw.delNode();
            }
          }
        });

    // Makes gesture pane connect to correct parts
    this.mainGesturePane.setContent(mainStackPane);
    mainGesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    // Action Listener for toggle switch
    toggleSwitch
        .selectedProperty()
        .addListener(
            Observable -> {
              NodeDraw.toggleLocationDisplay(toggleSwitch.isSelected());
            });
    mapEditor = new MapEditorController();

    mainGesturePane.setOnMouseClicked(
        event -> {
          if (event.getButton() == MouseButton.SECONDARY) {
            mouseXCoord = event.getSceneX();
            mouseYCoord = event.getSceneY() - 16;
          }
        });

    mainAnchorPane.setPickOnBounds(false);
    mainSelectPane.setPickOnBounds(false);

    // buttons
    buttons[0] = l1Button;
    buttons[1] = l2Button;
    buttons[2] = f1Button;
    buttons[3] = f2Button;
    buttons[4] = f3Button;
    mainTextPane.setVisible(false);
    initializeFloorMap("L2");
    buttons[previousFloor].setStyle("-fx-background-color: #D0D2D7");
  }

  public void generateFloor(ActionEvent event) {
    toggleSwitch.setSelected(false);
    String floor = "L1";
    if (event.getSource().equals(l1Button)) {
      floor = "L1";
    } else if (event.getSource().equals(l2Button)) {
      floor = "L2";
    } else if (event.getSource().equals(f1Button)) {
      floor = "1";
    } else if (event.getSource().equals(f2Button)) {
      floor = "2";
    } else if (event.getSource().equals(f3Button)) {
      floor = "3";
    }
    initializeFloorMap(floor);
  }

  /**
   * Adds the image of the floor, nodes, edges, and location names
   *
   * @param floor is the String of the floor's name in tableview either "L1", "L2", "1", "2", "3"
   */
  private void initializeFloorMap(String floor) {
    NodeDraw.setSelectedPane(null);
    List<NodeEntity> allNodes = FacadeRepository.getInstance().getNodesOnFloor(floor);
    List<EdgeEntity> allEdges = FacadeRepository.getInstance().getEdgesOnFloor(floor);
    Nodes = allNodes;
    Image image = ImageLoader.getImage(floor);
    mainImageView.setImage(image);

    buttons[previousFloor].setStyle("-fx-background-color: #ffffff");
    previousFloor =
        Floor.indexFromTableString(floor); // initialize previous to be the current floor
    buttons[previousFloor].setStyle("-fx-background-color: #D0D2D7");
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);

    mainStackPane.setOnMousePressed(
        e -> {
          if (e.isAltDown()) {
            selectionRectangle.setVisible(true);
            mainGesturePane.setGestureEnabled(false);

            mouseDownX = e.getX();
            mouseDownY = e.getY();
            selectionRectangle.setX(mouseDownX);
            selectionRectangle.setY(mouseDownY);
            selectionRectangle.setWidth(0);
            selectionRectangle.setHeight(0);
          }
        });

    mainStackPane.setOnMouseDragged(
        e -> {
          if (e.isAltDown()) {

            selectionRectangle.setX(Math.min(e.getX(), mouseDownX));
            selectionRectangle.setWidth(Math.abs(e.getX() - mouseDownX));
            selectionRectangle.setY(Math.min(e.getY(), mouseDownY));
            selectionRectangle.setHeight(Math.abs(e.getY() - mouseDownY));
          }
        });
    mainStackPane.setOnMouseReleased(
        e -> {
          if (e.isAltDown()) {
            mainGesturePane.setGestureEnabled(true);
            mouseDownReleasedX = e.getX();
            mouseDownReleasedY = e.getY();
            if (NodeDraw.getSelected() != null) {
              NodeEntity referenceNode = NodeDraw.getSelected();
              List<NodeEntity> selectedNodeList = findNodesInBounds(allNodes);
              StraightConfirmPopupController.setReferenceNode(referenceNode);
              StraightConfirmPopupController.setNodeList(selectedNodeList);
              StraightConfirmPopupController.setAllNodes(allNodes);
              StraightConfirmPopupController.setSCALE_FACTOR(SCALE_FACTOR);
              StraightConfirmPopupController.setMainAnchorPane(mainAnchorPane);
              StraightConfirmPopupController.setMEC(this);
              StraightConfirmPopupController.setAllEdges(allEdges);
              try {
                popUpStraight();
              } catch (IOException ex) {
                throw new RuntimeException(ex);
              }
            }
            if (!e.isStillSincePress()) {

              selectionRectangle.setVisible(false);
            }
          }
        });
  }

  public List<NodeEntity> findNodesInBounds(List<NodeEntity> allNodes) {
    double[] updatedXY = scaleCoordinatesReversed(mouseDownX, mouseDownY, SCALE_FACTOR);
    double[] updatedXYUpper =
        scaleCoordinatesReversed(mouseDownReleasedX, mouseDownReleasedY, SCALE_FACTOR);

    double maxX = Math.max(updatedXY[0], updatedXYUpper[0]);
    double minX = Math.min(updatedXY[0], updatedXYUpper[0]);

    double maxY = Math.max(updatedXY[1], updatedXYUpper[1]);
    double minY = Math.min(updatedXY[1], updatedXYUpper[1]);

    mouseDownX = 0;
    mouseDownY = 0;
    mouseDownReleasedY = 0;
    mouseDownReleasedX = 0;

    List<NodeEntity> selectedList = new ArrayList<>();

    for (NodeEntity n : allNodes) {
      if ((minX < n.getXcoord() && minY < n.getYcoord())
          && (maxX > n.getXcoord() && maxY > n.getYcoord())) {
        selectedList.add(n);
      }
    }

    return selectedList;
  }

  /**
   * Method that creates a new node on click "Create" with CreateNodeButton Adds into database and
   * draws on map
   */
  public void closePopup(String type) {
    // hide popups
    if (Objects.equals(type, "node")) {
      nodeEditorPopup.hide();
    } else if (Objects.equals(type, "edge")) {
      edgeEditorPopup.hide();
    } else if (Objects.equals(type, "location")) {
      locationEditorPopup.hide();
    } else if (Objects.equals(type, "node edit")) {
      nodeEditorEditPopup.hide();
    } else if (Objects.equals(type, "location edit")) {
      locationEditorEditPopup.hide();
    } else if (Objects.equals(type, "straightening")) {
      straightSelectionPopup.hide();
    }
  }

  public static String makeNewNodeID(String floor, int x, int y) {
    String xCoord = String.format("%04d", x);
    String yCoord = String.format("%04d", y);

    return (floor + "X" + xCoord + "Y" + yCoord);
  }

  @FXML
  public void popupNodeEditor() throws IOException {

    FXMLLoader nodeLoader =
        new FXMLLoader(Main.class.getResource("views/NodeEditorPopupFXML.fxml"));

    // popup stuff
    nodeEditorPopup = new PopOver(nodeLoader.load());

    nodeEditorPopup.show((mainAnchorPane.getScene().getWindow()));

    nodeEditorPopup.setAnchorX(mouseXCoord);
    nodeEditorPopup.setAnchorY(mouseYCoord);
  }

  @FXML
  public void popupEdgeEditor() throws IOException {

    FXMLLoader edgeLoader =
        new FXMLLoader(Main.class.getResource("views/EdgeEditorPopupFXML.fxml"));

    edgeEditorPopup = new PopOver(edgeLoader.load());
    edgeEditorPopup.show((mainAnchorPane.getScene().getWindow()));
    edgeEditorPopup.setAnchorX(mouseXCoord);
    edgeEditorPopup.setAnchorY(mouseYCoord);
  }

  @FXML
  public void popupLocationEditor() throws IOException {
    if (NodeDraw.getSelected() != null) {

      FXMLLoader locationLoader =
          new FXMLLoader(Main.class.getResource("views/LocationEditorPopupFXML.fxml"));

      locationEditorPopup = new PopOver(locationLoader.load());
      locationEditorPopup.show((mainAnchorPane.getScene().getWindow()));

      locationEditorPopup.setAnchorX(mouseXCoord);
      locationEditorPopup.setAnchorY(mouseYCoord);
    }
  }

  @FXML
  public void popupEditNode() throws IOException {

    FXMLLoader locationLoader =
        new FXMLLoader(Main.class.getResource("views/NodeEditorPopupEditFXML.fxml"));

    nodeEditorEditPopup = new PopOver(locationLoader.load());
    nodeEditorEditPopup.show((mainAnchorPane.getScene().getWindow()));

    nodeEditorEditPopup.setAnchorX(mouseXCoord);
    nodeEditorEditPopup.setAnchorY(mouseYCoord);
  }

  @FXML
  public void popupEditLoc() throws IOException {
    if (NodeDraw.getSelected() != null) {

      FXMLLoader locationLoader =
          new FXMLLoader(Main.class.getResource("views/LocationEditorPopupEditFXML.fxml"));

      locationEditorEditPopup = new PopOver(locationLoader.load());
      locationEditorEditPopup.show((mainAnchorPane.getScene().getWindow()));

      locationEditorEditPopup.setAnchorX(mouseXCoord);
      locationEditorEditPopup.setAnchorY(mouseYCoord);
    }
  }

  public void popUpStraight() throws IOException {
    FXMLLoader locationLoader =
        new FXMLLoader(Main.class.getResource("views/StraightConfirmFXML.fxml"));

    straightSelectionPopup = new PopOver(locationLoader.load());
    straightSelectionPopup.show((mainAnchorPane.getScene().getWindow()));
  }
}
