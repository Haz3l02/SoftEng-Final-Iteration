package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.NodeEditorPopupController.floor;
import static edu.wpi.cs3733.C23.teamA.mapdrawing.CoordinateScalar.scaleCoordinatesReversed;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.mapdrawing.NodeDraw;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
  @FXML MFXTextField XCord; // delete later
  @FXML MFXTextField YCord; // delete later
  @FXML MFXComboBox FloorBox; // delete later
  @FXML MFXComboBox BuildingBox; // delete later
  @FXML MFXButton saveButton; // delete later
  @FXML MFXTextField node1; // delete later
  @FXML MFXTextField node2; // delete later
  @FXML VBox fieldBox;
  @FXML MFXButton createNodeButton;
  @FXML MFXFilterComboBox<String> longNameBox = new MFXFilterComboBox<>();
  @FXML MFXTextField locationIDBox;
  @FXML MFXButton createLocation;
  @FXML MFXToggleButton toggleSwitch = new MFXToggleButton();

  @FXML MFXTextField node1Box;
  @FXML MFXTextField node2Box;

  @FXML MFXTextField shortNameBox;
  @FXML MFXTextField locTypeBox;

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

    // createNodeButton.setVisible(false);
    // saveButton.setVisible(false);
    // gc = mainCanvas.getGraphicsContext2D();
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
              System.out.println("delete edge only");
              NodeDraw.delEdge();
            } else if (NodeDraw.getSelected() != null && NodeDraw.getSelectedEdge() == null) {
              System.out.println("delete node only");
              NodeDraw.delNode();
            } else {
              System.out.println("idk");
            }
          }
          if (event.getCode().equals(KeyCode.X) && event.isControlDown()) {
            System.out.println("straighten that  (horizontally)");
            // NodeDraw.straightenNodesHorizontal();
          }
          if (event.getCode().equals(KeyCode.Y) && event.isControlDown()) {
            System.out.println("straighten that  (vertically)");
            // NodeDraw.straightenNodesVertical();
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

    //    if (!(NodeDraw.getSelected() == null &&
    // FacadeRepository.getInstance().getLocation(NodeDraw.getSelected().getNodeid()))) {
    //      longNameBox.setText(
    //          FacadeRepository.getInstance()
    //              .moveMostRecentLoc(NodeDraw.getSelected().getNodeid())
    //              .getLongname());
    //    }

    mainGesturePane.setOnMouseClicked(
        event -> {
          if (event.getButton() == MouseButton.SECONDARY) {

            /*
                        double[] coords =
                            CoordinateScalar.scaleCoordinatesReversed(
                                event.getSceneX(), event.getSceneY(), SCALE_FACTOR);

                        // System.out.println(Math.round(coords[0]) + ", " + Math.round(coords[1]));

                        // NodeEditorPopupController.setMouseX((int) Math.round(coords[0]));
                        // NodeEditorPopupController.setMouseY((int) Math.round(coords[1]));
            */

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
    // NodeDraw.drawEdges(allEdges, SCALE_FACTOR, mainAnchorPane);
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);
    // NodeDraw.drawLocations(allNodes, SCALE_FACTOR, mainTextPane);

    //  public void addLocation(ActionEvent event) {
    //    //    this.locNameEntity.setLongname("Freddy Fazbears Pizzarea 2 ");
    //    //    locNameEntity.setShortname("FNAF");
    //    //    locNameEntity.setLocationtype("LABS");
    //
    //    NodeEntity selected = NodeDraw2.getSelected();
    //    FacadeRepository.getInstance().newLocationOnNode(selected.getNodeid(), locNameEntity);
    //    // longNameBox.setText();
    //    System.out.println("done");
    //    initialize();
    //  }

    mainStackPane.setOnMousePressed(
        e -> {
          if (e.isAltDown()) {
            System.out.println("init rect");
            selectionRectangle.setVisible(true);
            mainGesturePane.setGestureEnabled(false);

            mouseDownX = e.getX();
            mouseDownY = e.getY();
            System.out.println("X:" + mouseDownX + "  Y:" + mouseDownY);
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
            } else {
              System.out.println("Node not selected!!");
            }

            if (!e.isStillSincePress()) {

              selectionRectangle.setVisible(false);
            }
          }
        });

    //  /**
    //   * Method to delete the node that is selected by the user Deletes from database and from the
    // nodes
    //   * on the map
    //   *
    //   * @param event
    //   * @throws IOException
    //   */
    //  public void deleteSelectedNode(ActionEvent event) throws IOException {
    //    NodeEntity currentNode = NodeDraw.getSelected();
    //    Pane currentNodePane = NodeDraw.getSelectedPane();
    //    String id = currentNode.getNodeid();
    //    String currentFloor = currentNode.getFloor();
    //    // Database //
    //    FacadeRepository.getInstance().collapseNode(currentNode); // edge repair and deletes node
    //    // FacadeRepository.getInstance().deleteNode(id); // delete from database
    //
    //    // Redraw map using database //
    //    // initializeFloorMap(currentFloor); // may need to use Floor.something to get tableview
    //
    //    // Redraw Map not using database //
    //    currentNodePane.setVisible(false); // delete node from map view
    //    List<EdgeEntity> allEdges = FacadeRepository.getInstance().getEdgesOnFloor(currentFloor);
    //    if (Floor.indexFromTableString(currentFloor) != -1) {
    //      // NodeDraw.drawEdges(
    //      // allEdges, SCALE_FACTOR, edgeAnchorPane); // delete then redraw edges for this floor
    //    }
    //    NodeEditorPopupController.setFloor(floor);
    //  }
  }

  public List<NodeEntity> findNodesInBounds(List<NodeEntity> allNodes) {
    double boxUpperX = mouseDownX + selectionRectangle.getWidth();
    double boxUpperY = mouseDownY + selectionRectangle.getHeight();
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

    System.out.println("max:" + (maxX) + "  min:" + (minX));

    List<NodeEntity> selectedList = new ArrayList<>();

    List<Pane> panesOnFloor = NodeDraw.getPaneList();

    for (NodeEntity n : allNodes) {
      if ((minX < n.getXcoord() && minY < n.getYcoord())
          && (maxX > n.getXcoord() && maxY > n.getYcoord())) {
        System.out.println("Added a node");
        selectedList.add(n);
      }
    }

    //    for (Pane p : panesOnFloor) {
    //
    //      double updatedPaneXY[] =
    //          scaleCoordinatesReversed(p.getLayoutX(), p.getLayoutY(), SCALE_FACTOR);
    //      if ((minX < updatedPaneXY[0] && minY < updatedPaneXY[1])
    //          && (maxX > updatedPaneXY[0] && maxY > updatedPaneXY[1])) {
    //
    //        p.setStyle(
    //            "-fx-background-color: 'yellow'; "
    //                + "-fx-background-radius: 12.5; "
    //                + "-fx-border-color: '#224870'; "
    //                + "-fx-border-width: 1;"
    //                + "-fx-border-radius: 13.5");
    //      }
    //    }

    // if (updatedXY[0] < updatedXYUpper[0]) {
    //    for (NodeEntity n : allNodes) {
    //      if ((updatedXY[0] < n.getXcoord() && updatedXY[1] < n.getYcoord())
    //          && (updatedXYUpper[0] > n.getXcoord() && updatedXYUpper[1] > n.getYcoord())) {
    //        System.out.println("Added a node");
    //        selectedList.add(n);
    //      }
    //    }
    //
    //    for (Pane p : panesOnFloor) {
    //
    //      double updatedPaneXY[] =
    //          scaleCoordinatesReversed(p.getLayoutX(), p.getLayoutY(), SCALE_FACTOR);
    //      System.out.println("X: " + updatedPaneXY[0] + "Y:" + updatedPaneXY[1]);
    //      System.out.println("X: " + p.getLayoutX() + "Y:" + p.getLayoutY());
    //      System.out.println("X: " + updatedXY[0] + "Y:" + updatedXY[1]);
    //
    //      if ((updatedXY[0] < updatedPaneXY[0] && updatedXY[1] < updatedPaneXY[1])
    //          && (updatedXYUpper[0] > updatedPaneXY[0] && updatedXYUpper[1] > updatedPaneXY[1])) {
    //
    //        System.out.println("change color");
    //        p.setStyle(
    //            "-fx-background-color: 'yellow'; "
    //                + "-fx-background-radius: 12.5; "
    //                + "-fx-border-color: '#224870'; "
    //                + "-fx-border-width: 1;"
    //                + "-fx-border-radius: 13.5");
    //      }
    //    }

    for (int i = 0; i < selectedList.size(); i++) {
      System.out.println(selectedList.get(i).getNodeid());
    }

    return selectedList;
  }

  //  public void addLocation(ActionEvent event) {
  //    //    this.locNameEntity.setLongname("Freddy Fazbears Pizzarea 2 ");
  //    //    locNameEntity.setShortname("FNAF");
  //    //    locNameEntity.setLocationtype("LABS");
  //
  //    NodeEntity selected = NodeDraw2.getSelected();
  //    FacadeRepository.getInstance().newLocationOnNode(selected.getNodeid(), locNameEntity);
  //    // longNameBox.setText();
  //    System.out.println("done");
  //    initialize();
  //  }

  //    NodeEditorPopupController.setFloor(floor);
  //  }

  public void goToNewNodeScene(ActionEvent event) {
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

    // draw node on map using database //
    // initializeFloorMap(tableString);

    // draw node onto the map (nonDatabase) //

    /*
    List<NodeEntity> oneNode;

    oneNode = FacadeRepository.getInstance().getNodesOnFloor("L1");
    // draw node
    assert mainAnchorPane != null;
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mapEditor.mainAnchorPane, this);

     */
  }

  public void editNode(ActionEvent event) {
    fieldBox.setStyle("-fx-background-color: 'red'; ");
    saveButton.setVisible(true);
  }

  /**
   * edits the selected node when "Save" button is clicked
   *
   * @param event
   */
  public void saveNodeEdit(ActionEvent event) {

    // Save info as a new node called currentNode
    NodeEntity currentNode = NodeDraw.getSelected();
    String id = currentNode.getNodeid();
    currentNode.setXcoord(Integer.parseInt(XCord.getText()));
    currentNode.setYcoord(Integer.parseInt(YCord.getText()));
    currentNode.setBuilding(BuildingBox.getText());
    String newFloor = (Floor.valueOf(Floor.fromString(FloorBox.getText()))).getTableString();
    currentNode.setFloor(newFloor);
    currentNode.setNodeid(
        makeNewNodeID(currentNode.getFloor(), currentNode.getXcoord(), currentNode.getYcoord()));

    //    currentPane.setLayoutX(currentNode.getXcoord());
    //    currentPane.setLayoutY(currentNode.getYcoord());
    //    NodeDraw.setSelectedPane(currentPane);

    // old id, with new updated node
    FacadeRepository.getInstance().updateNode(id, currentNode);
    // node.delete(id);
    fieldBox.setStyle("-fx-background-color: '#bad1ea'; ");
    saveButton.setVisible(false);

    // database way to add in new node //
    // initializeFloorMap(newFloor);

    // Remove old and draw new (nondatabase) //
    // Hide old node on map
    Pane currentPane = NodeDraw.getSelectedPane();
    if (currentPane != null) {
      currentPane.setVisible(false);
    }
    // draw on map
    ArrayList<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(currentNode);
    NodeDraw.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);
  }

  public static String makeNewNodeID(String floor, int x, int y) {
    String xCoord = String.format("%04d", x);
    String yCoord = String.format("%04d", y);

    return (floor + "X" + xCoord + "Y" + yCoord);
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

  public void setLocationIDBox(String idString) {
    locationIDBox.setText(idString);
  }

  public void setLongNameBox(String loc) {
    longNameBox.setValue(loc);
  }

  public void setShortNameBox(String sName) {
    shortNameBox.setText(sName);
  }

  public void setLocTypeBox(String type) {
    locTypeBox.setText(type);
  }

  public void changeLocations() {
    mainTextPane.setVisible(!mainTextPane.isVisible());
    //  public void setLocButtonVisibility(boolean eye) {
    //    createLocation.setVisible(eye);
    //  }
  }
  // TODO
  public void transitionToNewNodeBox(ActionEvent event) {}

  // TODO
  public void editEdge(ActionEvent event) {}

  // TODO
  public void deleteEdge(ActionEvent event) {}

  // TODO
  public void addLocationName(ActionEvent event) {}

  // TODO
  public void editLocationName(ActionEvent event) {}

  // TODO
  public void delLocationName(ActionEvent event) {}

  public void changeLocations(boolean flag) {
    NodeDraw.toggleLocationDisplay(false);
  }

  @FXML
  public void popupNodeEditor(ActionEvent event) throws IOException {

    FXMLLoader nodeLoader =
        new FXMLLoader(Main.class.getResource("views/NodeEditorPopupFXML.fxml"));

    // popup stuff
    nodeEditorPopup = new PopOver(nodeLoader.load());

    nodeEditorPopup.show((mainAnchorPane.getScene().getWindow()));

    nodeEditorPopup.setAnchorX(mouseXCoord);
    nodeEditorPopup.setAnchorY(mouseYCoord);

    /*
    NodeEditorPopupController.mouseX = NodeDraw.getSelected().getXcoord();
    NodeEditorPopupController.mouseY = NodeDraw.getSelected().getYcoord();
    NodeEditorPopupController.floor = NodeDraw.getSelected().getFloor();



    System.out.println(
        "updated coords to: ("
            + mainGesturePane.getCurrentX()
            + ", "
            + mainGesturePane.getCurrentY()
            + ")");
            */

  }

  @FXML
  public void popupEdgeEditor(ActionEvent event) throws IOException {

    System.out.println("popup edge editor");

    FXMLLoader edgeLoader =
        new FXMLLoader(Main.class.getResource("views/EdgeEditorPopupFXML.fxml"));

    edgeEditorPopup = new PopOver(edgeLoader.load());
    edgeEditorPopup.show((mainAnchorPane.getScene().getWindow()));
    edgeEditorPopup.setAnchorX(mouseXCoord);
    edgeEditorPopup.setAnchorY(mouseYCoord);
  }

  @FXML
  public void popupLocationEditor(ActionEvent event) throws IOException {
    if (NodeDraw.getSelected() == null) {
      System.out.println("No node selected");
    } else {
      System.out.println("popup location editor");

      FXMLLoader locationLoader =
          new FXMLLoader(Main.class.getResource("views/LocationEditorPopupFXML.fxml"));

      locationEditorPopup = new PopOver(locationLoader.load());
      locationEditorPopup.show((mainAnchorPane.getScene().getWindow()));

      locationEditorPopup.setAnchorX(mouseXCoord);
      locationEditorPopup.setAnchorY(mouseYCoord);
    }
  }

  @FXML
  public void popupEditNode(ActionEvent event) throws IOException {

    FXMLLoader locationLoader =
        new FXMLLoader(Main.class.getResource("views/NodeEditorPopupEditFXML.fxml"));

    nodeEditorEditPopup = new PopOver(locationLoader.load());
    nodeEditorEditPopup.show((mainAnchorPane.getScene().getWindow()));

    nodeEditorEditPopup.setAnchorX(mouseXCoord);
    nodeEditorEditPopup.setAnchorY(mouseYCoord);
  }

  @FXML
  public void popupEditLoc(ActionEvent event) throws IOException {
    if (NodeDraw.getSelected() == null) {
      System.out.println("No node selected");
    } else {
      System.out.println("popup location editor");

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

  public static void hideLastNode(NodeEntity newNode) {

    // nodeEditorPopup.hide();
    // take care of last selected node
    Pane recentPane = NodeDraw.getSelectedPane();
    if (recentPane != null) {
      recentPane.setPrefSize(5, 5);
      recentPane.setStyle(
          "-fx-background-color: '#224870'; "
              + "-fx-background-radius: 12.5; "
              + "-fx-border-color: '#224870'; "
              + "-fx-border-width: 1;"
              + "-fx-border-radius: 13.5");
      //      int[] updatedCoords = NodeDraw.scaleCoordinates();
      //      recentPane.setLayoutX(updatedCoords[0] - 2.5);
      //      recentPane.setLayoutY(updatedCoords[1] - 2.5);

      // draw node onto the map (nonDatabase) //
      List<NodeEntity> oneNode = FacadeRepository.getInstance().getAllNode();
      oneNode.add(newNode);

      NodeDraw.drawNodes(
          oneNode, mapEditor.SCALE_FACTOR, mapEditor.mainAnchorPane, mapEditor); // draw node
    }
  }
}
