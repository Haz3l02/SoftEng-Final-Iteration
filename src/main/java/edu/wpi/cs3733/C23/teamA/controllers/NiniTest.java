package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EdgeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw2;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

public class NiniTest extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView mainImageView;
  @FXML private GesturePane mainGesturePane;
  @FXML AnchorPane mainAnchorPane;
  @FXML StackPane mainStackPane;
  @FXML ImageView mainMapImage;
  @FXML AnchorPane mainTextPane = new AnchorPane();
  @FXML private Canvas mainCanvas = new Canvas();

  @FXML MFXButton l1Button;
  @FXML MFXButton l2Button;
  @FXML MFXButton f1Button;
  @FXML MFXButton f2Button;
  @FXML MFXButton f3Button;

  // Buttons and Text
  @FXML MFXTextField XCord;
  @FXML MFXTextField YCord;
  @FXML MFXComboBox FloorBox;
  @FXML MFXComboBox BuildingBox;
  @FXML MFXButton saveButton;
  @FXML MFXTextField node1;
  @FXML MFXTextField node2;
  @FXML VBox fieldBox;
  @FXML MFXButton createNodeButton;
  @FXML MFXFilterComboBox<String> longNameBox;
  @FXML MFXTextField locationIDBox;
  @FXML MFXButton createLocation;
  GraphicsContext gc;

  @FXML
  Text reminder; // text field for a "remember to fill out all fields before submitting form" thingy

  @Setter NodeEntity selectedNode = null;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;
  private List<EdgeEntity> allEdges;
  NodeImpl nodeimpl = new NodeImpl();
  EdgeImpl edgeimpl = new EdgeImpl();
  MoveImpl moveimpl = new MoveImpl();
  LocationNameImpl locNameImp = new LocationNameImpl();

  // scaling constant
  private double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulations

  /** Starting method called when screen is opened: Draws nodes and edges */
  public void initialize() {

    NodeDraw2.setSelectedPane(null);

    createNodeButton.setVisible(false);
    saveButton.setVisible(false);
    allNodes = nodeimpl.getNodeOnFloor("L1");
    allEdges = edgeimpl.getEdgeOnFloor("L1");
    gc = mainCanvas.getGraphicsContext2D();
    NodeDraw2.drawEdges(allEdges, SCALE_FACTOR, gc);
    addFloorMapImage("L1", mainImageView);
    NodeDraw2.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);
    NodeDraw2.drawLocations(allNodes, SCALE_FACTOR, mainTextPane, this);

    this.mainGesturePane.setContent(mainStackPane);
  }

  public void generateFloor(ActionEvent event) {
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
    NodeDraw2.setSelectedPane(null);
    createNodeButton.setVisible(false);
    saveButton.setVisible(false);
    allNodes = nodeimpl.getNodeOnFloor(floor);
    allEdges = edgeimpl.getEdgeOnFloor(floor);
    NodeDraw2.drawEdges(allEdges, SCALE_FACTOR, gc);
    addFloorMapImage(floor, mainImageView);
    NodeDraw2.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);

    NodeDraw2.drawLocations(allNodes, SCALE_FACTOR, mainTextPane, this);
    this.mainGesturePane.setContent(mainStackPane);
  }

  /*
  public void getTab() {
    System.out.println("HERE");
    Tab selectedTab = mapTabPane.getSelectionModel().getSelectedItem();
    String tabID = selectedTab.getId();

    System.out.println(tabID);

    // if (tabID.equals("tabL1") && floorInitialized[0] == false) {
      /// nothing since already loaded
    // } else
    if (tabID.equals("tabL2") && floorInitialized[1] == false) {
      initializeFloorMap("L2");
      floorInitialized[1] = true;
    } else if (tabID.equals("tabF1") && floorInitialized[2] == false) {
      initializeFloorMap("1");
      floorInitialized[2] = true;
    } else if (tabID.equals("tabF2") && floorInitialized[3] == false) {
      initializeFloorMap("2");
      floorInitialized[3] = true;
    } else if (tabID.equals("tabF3") && floorInitialized[4] == false) {
      initializeFloorMap("3");
      floorInitialized[4] = true;
    }
  }
  */

  //  /**
  //   * Attaches the gesturepane with the stackpane and reads and adds all the nodes on a floor to
  // the
  //   * correct anchorPane
  //   */
  //  private void initializeFloorMap(String floor) {
  //    int floorIndex = Floor.indexFromTableString(floor);
  //    // add image
  //    // addFloorMapImage(floor, ivs[floorIndex]); // !!!
  //
  //    // Get all nodes on floor names floor!
  //    allNodes = nodeimpl.getNodeOnFloor(floor);
  //    allEdges = edgeimpl.getEdgeOnFloor(floor);
  //
  //    LocationNameEntity locNameEnt;
  //    ArrayList<NodeEntity> nullNodes = new ArrayList<>();
  //
  //    //    // for loop
  //    //    for (NodeEntity n : allNodes) {
  //    //      locNameEnt = location.mostRecentLoc(n.getNodeid());
  //    //      if (locNameEnt == null) {
  //    //        nullNodes.add(n);
  //    //      }
  //    //    }
  //
  //    GraphicsContext gc = gcs[floorIndex];
  //
  //    // Add nodes as circles
  //    // mainAnchorPane = NodeDraw2.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);
  //    // NodeDraw2.drawEdges(allEdges, SCALE_FACTOR, gc);
  //
  //    ObservableList<String> floors =
  //        FXCollections.observableArrayList(
  //            Floor.L1.getExtendedString(),
  //            Floor.L2.getExtendedString(),
  //            Floor.F1.getExtendedString(),
  //            Floor.F2.getExtendedString(),
  //            Floor.F3.getExtendedString());
  //    FloorBox.setItems(floors);
  //
  //    ObservableList<String> buildings =
  //        FXCollections.observableArrayList(
  //            Building.FR45.getTableString(),
  //            Building.TOWR.getTableString(),
  //            Building._BTM.getTableString(),
  //            Building.SHPR.getTableString(),
  //            Building.FR15.getTableString());
  //    BuildingBox.setItems(buildings);
  //
  //    Node node = stacks[floorIndex];
  //    gestures[floorIndex].setContent(node);
  //    gestures[floorIndex].setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
  //  }
  //
  //  public void loadLocNames(ActionEvent event) {}
  //
  @FXML
  public void switchToNodeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_DATABASE);
  }

  //    public String makeNewNodeID(String floor, int x, int y) {
  //      String xCoord = String.format("%04d", x);
  //      String yCoord = String.format("%04d", y);
  //
  //      return (floor + "X" + xCoord + "Y" + yCoord);
  //    }
  //
  //  public static String toString(char[] a) {
  //    // Creating object of String class
  //    String string = new String(a);
  //    return string;
  //  }

  public void deleteSelectedNode(ActionEvent event) throws IOException {
    NodeEntity currentNode = NodeDraw2.getSelected();
    Pane currentNodePane = NodeDraw2.getSelectedPane();
    String id = currentNode.getNodeid();
    edgeimpl.collapseNode(currentNode);
    nodeimpl.delete(id);
    currentNodePane.setVisible(false);
    int index = Floor.indexFromTableString(currentNode.getFloor());
    gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

    // fix this
    String currentFloor = currentNode.getFloor();
    allEdges = edgeimpl.getEdgeOnFloor(currentFloor);
    if (Floor.indexFromTableString(currentFloor) != -1)
      NodeDraw.drawEdges(allEdges, SCALE_FACTOR, gc);
  }

  public void transitionToNewNodeBox(ActionEvent event) {
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

  public void addEdge(ActionEvent event) {}

  public void createNode(ActionEvent event) {

    // Create a new node entity
    NodeEntity newNode = new NodeEntity();
    fieldBox.setStyle("-fx-background-color: '013A75'; ");
    newNode.setXcoord(Integer.parseInt(XCord.getText()));
    newNode.setYcoord(Integer.parseInt(YCord.getText()));
    Floor floor = Floor.valueOf(Floor.fromString(FloorBox.getText()));
    String tableString = floor.getTableString();
    newNode.setFloor(tableString);
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(makeNewNodeID(newNode.getFloor(), newNode.getXcoord(), newNode.getYcoord()));

    //    System.out.println("X: " + newNode.getXcoord());
    //    System.out.println("Y: " + newNode.getYcoord());
    //    System.out.println("Floor: " + newNode.getFloor());
    //    System.out.println("Building: " + newNode.getBuilding());
    //    System.out.println("ID: " + newNode.getNodeid());

    // Add new Node to database
    nodeimpl.add(newNode);

    // switch box screen
    createNodeButton.setVisible(false);
    fieldBox.setStyle("-fx-background-color: '#bad1ea'; ");

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
    }

    // draw node onto the map
    ArrayList<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(newNode);
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);
  }

  public void editNode(ActionEvent event) {
    fieldBox.setStyle("-fx-background-color: 'red'; ");
    saveButton.setVisible(true);
  }

  public void saveNodeEdit(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    Pane currentPane = NodeDraw.getSelectedPane();
    if (currentPane != null) {
      currentPane.setVisible(false);
    }
    String id = currentNode.getNodeid();
    currentNode.setXcoord(Integer.parseInt(XCord.getText()));
    currentNode.setYcoord(Integer.parseInt(YCord.getText()));
    currentNode.setBuilding(BuildingBox.getText());
    Floor floor = Floor.valueOf(Floor.fromString(FloorBox.getText())); // !!
    currentNode.setFloor(floor.getTableString());

    System.out.println("X: " + currentNode.getXcoord());
    System.out.println("Y: " + currentNode.getYcoord());
    System.out.println("Floor: " + currentNode.getFloor());
    System.out.println("Building: " + currentNode.getBuilding());
    System.out.println("ID: " + currentNode.getNodeid());

    currentNode.setNodeid(
        makeNewNodeID(currentNode.getFloor(), currentNode.getXcoord(), currentNode.getYcoord()));

    System.out.println("IDNew: " + currentNode.getNodeid());

    //    currentPane.setLayoutX(currentNode.getXcoord());
    //    currentPane.setLayoutY(currentNode.getYcoord());
    //    NodeDraw.setSelectedPane(currentPane);

    // old id, with new updated node
    nodeimpl.update(id, currentNode);
    // node.delete(id);
    fieldBox.setStyle("-fx-background-color: '#bad1ea'; ");
    saveButton.setVisible(false);

    // Remove old and draw new
    Pane currentNodePane = NodeDraw.getSelectedPane();
    currentNodePane.setVisible(false);
    ArrayList<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(currentNode);
    String tableString = currentNode.getFloor();
    System.out.println("Floor: " + tableString);
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);

    //     initialize();
  }

  @FXML
  public void addLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
    moveimpl.add(newLocation);
    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
    createLocation.setVisible(false);

    System.out.println("LongName");
    System.out.println(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
    System.out.println();

    // added to redraw
    Pane currentNodePane = NodeDraw.getSelectedPane();
    currentNodePane.setVisible(false);
    List<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(currentNode);
    String tableString = currentNode.getFloor();
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);

    // initializeFloorMap("L1", stackL1, gestureL1);
  }

  @FXML
  public void editLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
    List<String> data = new ArrayList<>();
    data.add(currentNode.getNodeid());
    data.add(longNameBox.getText());
    data.add(LocalDate.now().toString());
    moveimpl.update(data, newLocation);
    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
  }

  @FXML
  public void delLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
    List<String> data = new ArrayList<>();
    data.add(currentNode.getNodeid());
    data.add(longNameBox.getText());
    data.add(LocalDate.now().toString());
    moveimpl.delete(data);
    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
  }

  @FXML
  public void showLocations(ActionEvent event) {
    // TODO
    System.out.println("show locations");
  }

  @FXML
  public void hideLocations(ActionEvent event) {

    // TODO
    System.out.println("show locations");
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

  @FXML
  public void editEdge(ActionEvent event) {}

  @FXML
  public void deleteEdge(ActionEvent event) {}

  public void setLocationIDBox(String idString) {
    locationIDBox.setText(idString);
  }

  public void setLongNameBox(String loc) {
    longNameBox.setValue(loc);
  }

  public void setLocButtonVisibility(boolean eye) {
    createLocation.setVisible(eye);
  }

  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param floor is the tablename of the floor
   * @param iv is the image view to be updated
   */
  private void addFloorMapImage(String floor, ImageView iv) {
    Image image = ImageLoader.getImage(floor);
    iv.setImage(image);
  }
}
