package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw2;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.*;
import java.io.IOException;
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

public class MapEditorController extends MenuController {

  // FXML Elements
  @FXML private ImageView mainImageView;
  @FXML private GesturePane mainGesturePane;
  @FXML private AnchorPane mainAnchorPane;
  @FXML private AnchorPane edgeAnchorPane;
  @FXML private StackPane mainStackPane;
  @FXML private AnchorPane mainTextPane = new AnchorPane();
  @FXML private Canvas mainCanvas = new Canvas();

  // Buttons to switch pages
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
  @FXML MFXToggleButton toggleSwitch;

  @FXML
  Text reminder; // text field for a "remember to fill out all fields before submitting form" thingy

  @Setter NodeEntity selectedNode = null;

  // Lists of Nodes and Node Data
  private GraphicsContext gc;

  // scaling constant
  private double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulations

  /** Starting method called when screen is opened: Draws nodes and edges for floor L1 */
  public void initialize() {

    createNodeButton.setVisible(false);
    saveButton.setVisible(false);
    gc = mainCanvas.getGraphicsContext2D();

    mainTextPane.setVisible(false);
    initializeFloorMap("L1");

    // Makes gesture pane connect to correct parts
    this.mainGesturePane.setContent(mainStackPane);
    mainGesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    // Action Listener for toggle switch
    toggleSwitch
        .selectedProperty()
        .addListener(
            Observable -> {
              changeLocations();
            });
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
    initializeFloorMap(floor);
  }

  /**
   * Adds the image of the floor, nodes, edges, and location names
   *
   * @param floor is the String of the floor's name in tableview either "L1", "L2", "1", "2", "3"
   */
  private void initializeFloorMap(String floor) {
    NodeDraw2.setSelectedPane(null);
    List<NodeEntity> allNodes = FacadeRepository.getInstance().getNodesOnFloor(floor);
    List<EdgeEntity> allEdges = FacadeRepository.getInstance().getEdgesOnFloor(floor);
    Image image = ImageLoader.getImage(floor);

    mainImageView.setImage(image);
    NodeDraw2.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, this);
    NodeDraw2.drawEdges(allEdges, SCALE_FACTOR, edgeAnchorPane);
    NodeDraw2.drawLocations(allNodes, SCALE_FACTOR, mainTextPane, this);
  }

  /**
   * Method to delete the node that is selected by the user Deletes from database and from the nodes
   * on the map
   *
   * @param event
   * @throws IOException
   */
  public void deleteSelectedNode(ActionEvent event) throws IOException {
    NodeEntity currentNode = NodeDraw2.getSelected();
    Pane currentNodePane = NodeDraw2.getSelectedPane();
    String id = currentNode.getNodeid();
    String currentFloor = currentNode.getFloor();
    // Database //
    FacadeRepository.getInstance().collapseNode(currentNode); // edge repair and deletes node
    // FacadeRepository.getInstance().deleteNode(id); // delete from database

    // Redraw map using database //
    // initializeFloorMap(currentFloor); // may need to use Floor.something to get tableview

    // Redraw Map not using database //
    currentNodePane.setVisible(false); // delete node from map view
    List<EdgeEntity> allEdges = FacadeRepository.getInstance().getEdgesOnFloor(currentFloor);
    if (Floor.indexFromTableString(currentFloor) != -1) {
      NodeDraw2.drawEdges(
          allEdges, SCALE_FACTOR, edgeAnchorPane); // delete then redraw edges for this floor
    }
  }

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

  public void addEdge(ActionEvent event) {}

  /**
   * Method that creates a new node on click "Create" with CreateNodeButton Adds into database and
   * draws on map
   *
   * @param event
   */
  public void createNode(ActionEvent event) {

    // Create a new node entity
    NodeEntity newNode = new NodeEntity();
    fieldBox.setStyle("-fx-background-color: '013A75'; ");
    newNode.setXcoord(Integer.parseInt(XCord.getText()));
    newNode.setYcoord(Integer.parseInt(YCord.getText()));
    String tableString = Floor.tableStringFromExtendedString(FloorBox.getText());
    newNode.setFloor(tableString);
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(makeNewNodeID(newNode.getFloor(), newNode.getXcoord(), newNode.getYcoord()));

    // Add new Node to database //
    FacadeRepository.getInstance().addNode(newNode);

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

    // draw node on map using database //
    // initializeFloorMap(tableString);

    // draw node onto the map (nonDatabase) //
    ArrayList<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(newNode);
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this); // draw node
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
    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);
  }

  public String makeNewNodeID(String floor, int x, int y) {
    String xCoord = String.format("%04d", x);
    String yCoord = String.format("%04d", y);

    return (floor + "X" + xCoord + "Y" + yCoord);
  }

  //  @FXML
  //  public void addLocationName(ActionEvent event) {
  //    NodeEntity currentNode = NodeDraw.getSelected();
  //    MoveEntity newLocation =
  //        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
  //    moveimpl.add(newLocation);
  //    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
  //    locationIDBox.setText(currentNode.getNodeid());
  //    createLocation.setVisible(false);
  //
  //    System.out.println("LongName");
  //    System.out.println(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
  //    System.out.println();
  //
  //    // added to redraw
  //    Pane currentNodePane = NodeDraw.getSelectedPane();
  //    currentNodePane.setVisible(false);
  //    List<NodeEntity> oneNode = new ArrayList<>();
  //    oneNode.add(currentNode);
  //    String tableString = currentNode.getFloor();
  //    NodeDraw2.drawNodes(oneNode, SCALE_FACTOR, mainAnchorPane, this);
  //
  //    // initializeFloorMap("L1", stackL1, gestureL1);
  //  }
  //
  //  @FXML
  //  public void editLocationName(ActionEvent event) {
  //    NodeEntity currentNode = NodeDraw.getSelected();
  //    MoveEntity newLocation =
  //        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
  //    List<String> data = new ArrayList<>();
  //    data.add(currentNode.getNodeid());
  //    data.add(longNameBox.getText());
  //    data.add(LocalDate.now().toString());
  //    moveimpl.update(data, newLocation);
  //    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
  //    locationIDBox.setText(currentNode.getNodeid());
  //  }
  //
  //  @FXML
  //  public void delLocationName(ActionEvent event) {
  //    NodeEntity currentNode = NodeDraw.getSelected();
  //    MoveEntity newLocation =
  //        new MoveEntity(currentNode, locNameImp.get(longNameBox.getText()), LocalDate.now());
  //    List<String> data = new ArrayList<>();
  //    data.add(currentNode.getNodeid());
  //    data.add(longNameBox.getText());
  //    data.add(LocalDate.now().toString());
  //    moveimpl.delete(data);
  //    longNameBox.setText(moveimpl.mostRecentLoc(currentNode.getNodeid()).getLongname());
  //    locationIDBox.setText(currentNode.getNodeid());
  //  }
  //
  //  @FXML
  //  public void showLocations(ActionEvent event) {
  //    // TODO
  //    System.out.println("show locations");
  //  }
  //
  //  @FXML
  //  public void hideLocations(ActionEvent event) {
  //
  //    // TODO
  //    System.out.println("show locations");
  //  }
  //
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
  //
  //  @FXML
  //  public void editEdge(ActionEvent event) {}
  //
  //  @FXML
  //  public void deleteEdge(ActionEvent event) {}
  //
  public void setLocationIDBox(String idString) {
    locationIDBox.setText(idString);
  }

  public void setLongNameBox(String loc) {
    longNameBox.setValue(loc);
  }

  public void setLocButtonVisibility(boolean eye) {
    createLocation.setVisible(eye);
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

  // TODO by Sarah
  public void changeLocations() {
    mainTextPane.setVisible(!mainTextPane.isVisible());
  }
}
