package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.*;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.*;
import io.github.palexdev.materialfx.controls.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

  // Canvases
  @FXML Canvas mapEditorCanvasL1;
  @FXML Canvas mapEditorCanvasL2;
  @FXML Canvas mapEditorCanvasF1;
  @FXML Canvas mapEditorCanvasF2;
  @FXML Canvas mapEditorCanvasF3;

  // image views
  @FXML private ImageView floorL2;
  @FXML private ImageView floorL1;
  @FXML private ImageView floorF1;
  @FXML private ImageView floorF2;
  @FXML private ImageView floorF3;

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

  @FXML TabPane editorTabPane;

  @FXML
  Text reminder; // text field for a "remember to fill out all fields before submitting form" thingy

  @Setter NodeEntity selectedNode = null;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;
  private List<EdgeEntity> allEdges;
  private GraphicsContext[] gcs = new GraphicsContext[5];
  private AnchorPane[] aps = new AnchorPane[5];
  private ImageView[] ivs = new ImageView[5];
  private StackPane[] stacks = new StackPane[5];

  // scaling constant
  private double SCALE_FACTOR = 0.15; // constant for map size/coordinate manipulation

  static Pane previousNode = null;
  static Pane selectNodePane = null;
  static NodeEntity selectNode = null;

  /** Starting method called when screen is opened: Draws nodes and edges */
  public void initialize() {

    NodeDraw.setSelectedPane(null);
    createNodeButton.setVisible(false);
    saveButton.setVisible(false);

    // set location name box
    ObservableList<String> locationList =
        FXCollections.observableArrayList(
            FacadeRepository.getInstance().getAllLocation().stream()
                .map(locationNameEntity -> locationNameEntity.getLongname())
                .toList());
    longNameBox.setItems(locationList);

    // sets the arrays for GraphicContexts and AnchorPanes
    setArrays();

    // add nodes and edges per floor
    initializeFloorMap("L1", stackL1, gestureL1);
    initializeFloorMap("L2", stackL2, gestureL2);
    initializeFloorMap("1", stackF1, gestureF1);
    initializeFloorMap("2", stackF2, gestureF2);
    initializeFloorMap("3", stackF3, gestureF3);
  }

  /**
   * Attaches the gesturepane with the stackpane and reads and adds all the nodes on a floor to the
   * correct anchorPane
   */
  private void initializeFloorMap(String floor, StackPane stack, GesturePane gesture) {
    int floorIndex = Floor.indexFromTableString(floor);
    // add image
    addFloorMapImage(floor, ivs[floorIndex]);

    // Get all nodes on floor names floor!
    allNodes = FacadeRepository.getInstance().getNodesOnFloor(floor);
    allEdges = FacadeRepository.getInstance().getEdgesOnFloor(floor);

    LocationNameEntity locNameEnt;
    ArrayList<NodeEntity> nullNodes = new ArrayList<>();

    //    // for loop
    //    for (NodeEntity n : allNodes) {
    //      locNameEnt = location.mostRecentLoc(n.getNodeid());
    //      if (locNameEnt == null) {
    //        nullNodes.add(n);
    //      }
    //    }

    GraphicsContext gc = gcs[floorIndex];

    // Add nodes as circles
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, aps[floorIndex], this);
    NodeDraw.drawEdges(allEdges, SCALE_FACTOR, gc);

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

    Node node = stack;
    gesture.setContent(node);
    gesture.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
  }

  public void loadLocNames(ActionEvent event) {}

  @FXML
  public void switchToNodeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_DATABASE);
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
    FacadeRepository.getInstance().deleteNode(id);
    FacadeRepository.getInstance().collapseNode(currentNode);
    currentNodePane.setVisible(false);
    int index = Floor.indexFromTableString(currentNode.getFloor());
    gcs[index].clearRect(
        0, 0, gcs[index].getCanvas().getWidth(), gcs[index].getCanvas().getHeight());
    // initializeFloorMap(currentNode.getFloor(), stackL1, gestureL1);
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
    String tableString = Floor.fromString(FloorBox.getText());
    newNode.setFloor(tableString);
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(makeNewNodeID(newNode.getFloor(), newNode.getXcoord(), newNode.getYcoord()));

    //    System.out.println("X: " + newNode.getXcoord());
    //    System.out.println("Y: " + newNode.getYcoord());
    //    System.out.println("Floor: " + newNode.getFloor());
    //    System.out.println("Building: " + newNode.getBuilding());
    //    System.out.println("ID: " + newNode.getNodeid());

    // Add new Node to database
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

    // draw node onto the map
    ArrayList<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(newNode);
    NodeDraw.drawNodes(oneNode, SCALE_FACTOR, aps[Floor.indexFromTableString(tableString)], this);
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
    currentNode.setFloor(Floor.fromString(FloorBox.getText()));

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
    FacadeRepository.getInstance().updateNode(id, currentNode);
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
    NodeDraw.drawNodes(oneNode, SCALE_FACTOR, aps[Floor.indexFromTableString(tableString)], this);

    // initialize();
  }

  @FXML
  public void addLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(
            currentNode,
            FacadeRepository.getInstance().getLocation(longNameBox.getText()),
            LocalDate.now());
    FacadeRepository.getInstance().addMove(newLocation);
    longNameBox.setText(
        FacadeRepository.getInstance().moveMostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
    createLocation.setVisible(false);

    System.out.println("LongName");
    System.out.println(
        FacadeRepository.getInstance().moveMostRecentLoc(currentNode.getNodeid()).getLongname());
    System.out.println();

    // added to redraw
    Pane currentNodePane = NodeDraw.getSelectedPane();
    currentNodePane.setVisible(false);
    List<NodeEntity> oneNode = new ArrayList<>();
    oneNode.add(currentNode);
    String tableString = currentNode.getFloor();
    NodeDraw.drawNodes(oneNode, SCALE_FACTOR, aps[Floor.indexFromTableString(tableString)], this);

    // initializeFloorMap("L1", stackL1, gestureL1);
  }

  @FXML
  public void editLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(
            currentNode,
            FacadeRepository.getInstance().getLocation(longNameBox.getText()),
            LocalDate.now());
    List<String> data = new ArrayList<>();
    data.add(currentNode.getNodeid());
    data.add(longNameBox.getText());
    data.add(LocalDate.now().toString());
    FacadeRepository.getInstance().updateMove(data, newLocation);
    longNameBox.setText(
        FacadeRepository.getInstance().moveMostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
  }

  @FXML
  public void delLocationName(ActionEvent event) {
    NodeEntity currentNode = NodeDraw.getSelected();
    MoveEntity newLocation =
        new MoveEntity(
            currentNode,
            FacadeRepository.getInstance().getLocation(longNameBox.getText()),
            LocalDate.now());
    List<String> data = new ArrayList<>();
    data.add(currentNode.getNodeid());
    data.add(longNameBox.getText());
    data.add(LocalDate.now().toString());
    FacadeRepository.getInstance().deleteMove(data);
    longNameBox.setText(
        FacadeRepository.getInstance().moveMostRecentLoc(currentNode.getNodeid()).getLongname());
    locationIDBox.setText(currentNode.getNodeid());
  }

  public void editLocationName() {}

  public void delLocationName() {}

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

  private void setArrays() {
    // set graphicContexts for each floor
    gcs[0] = mapEditorCanvasL1.getGraphicsContext2D();
    gcs[1] = mapEditorCanvasL2.getGraphicsContext2D();
    gcs[2] = mapEditorCanvasF1.getGraphicsContext2D();
    gcs[3] = mapEditorCanvasF2.getGraphicsContext2D();
    gcs[4] = mapEditorCanvasF3.getGraphicsContext2D();

    // set anchorpanes into an array for easy access
    aps[0] = nodeAnchorL1;
    aps[1] = nodeAnchorL2;
    aps[2] = nodeAnchorF1;
    aps[3] = nodeAnchorF2;
    aps[4] = nodeAnchorF3;

    ivs[0] = floorL1;
    ivs[1] = floorL2;
    ivs[2] = floorF1;
    ivs[3] = floorF2;
    ivs[4] = floorF3;

    stacks[0] = stackL1;
    stacks[1] = stackL2;
    stacks[2] = stackF1;
    stacks[3] = stackF2;
    stacks[4] = stackF3;
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

  //  public void drawNodes(List<NodeEntity> allNodes, double scaleFactor, AnchorPane nodeAnchor) {
  //    // gc.setFill(Color.web("0x224870"));
  //
  //    // draw circle for each node
  //    for (NodeEntity n : allNodes) {
  //      int[] updatedCoords = NodeDraw.scaleCoordinates(n.getXcoord(), n.getYcoord(),
  // scaleFactor);
  //      Pane nodeGraphic = new Pane();
  //
  //      /* Set the style of the node */
  //      nodeGraphic.setPrefSize(5, 5);
  //      nodeGraphic.setLayoutX(updatedCoords[0] - 2.5);
  //      nodeGraphic.setLayoutY(updatedCoords[1] - 2.5);
  //      nodeGraphic.setStyle(
  //          "-fx-background-color: '#224870'; "
  //              + "-fx-background-radius: 12.5; "
  //              + "-fx-border-color: '#224870'; "
  //              + "-fx-border-width: 1;"
  //              + "-fx-border-radius: 12.5");
  //      //      Text locName = new Text();
  //      //      locName.setVisible(false);
  //      //      if (!(locations.mostRecentLoc(n.getNodeid()) == null)) {
  //      //        locName.setVisible(true);
  //      //        locName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 5));
  //      //        locName.setText(locations.mostRecentLoc(n.getNodeid()).getShortname());
  //      //        locName.setLayoutX(updatedCoords[0] - 2.5);
  //      //        locName.setLayoutY(updatedCoords[1] - 2.5);
  //      //        NodeMapController nmcToggle = new NodeMapController();
  //      //        //      if (nmcToggle.toggleLocations()) {
  //      //        //        locName.setVisible(false);
  //      //        //      }
  //      //      }
  //
  //      EventHandler<MouseEvent> eventHandler =
  //          new EventHandler<MouseEvent>() {
  //            @Override
  //            public void handle(MouseEvent event) {
  //
  //              selectNodePane = nodeGraphic;
  //
  //              if ((previousNode != null)) {
  //
  //                if (!previousNode.equals(nodeGraphic)) {
  //
  //                  previousNode.setStyle(
  //                      "-fx-background-color: '#224870'; "
  //                          + "-fx-background-radius: 12.5; "
  //                          + "-fx-border-color: '#224870'; "
  //                          + "-fx-border-width: 1;"
  //                          + "-fx-border-radius: 13.5");
  //                  previousNode.setPrefSize(5, 5);
  //                  //                  previousNode.setLayoutX(updatedCoords[0] - 2.5);
  //                  //                  previousNode.setLayoutY(updatedCoords[1] - 2.5);
  //                }
  //              }
  //
  //              nodeGraphic.setStyle(
  //                  "-fx-background-color: '#D3E9F6'; "
  //                      + "-fx-background-radius: 12.5; "
  //                      + "-fx-border-color: '#224870'; "
  //                      + "-fx-border-width: 1;"
  //                      + "-fx-border-radius: 13.5");
  //              nodeGraphic.setPrefSize(7, 7);
  //              //              nodeGraphic.setLayoutX(updatedCoords[0] - 3.5);
  //              //              nodeGraphic.setLayoutY(updatedCoords[1] - 3.5);
  //
  //              previousNode = nodeGraphic;
  //              selectNode = n;
  //
  //              setXCord(n.getXcoord().toString());
  //              setYCord(n.getYcoord().toString());
  //              setFloorBox(Floor.extendedStringFromTableString(n.getFloor()));
  //              // nmc.setFloorBox(n.getFloor());
  //              setBuildingBox(n.getBuilding());
  //              makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord());
  //
  //              System.out.println(moveimpl.mostRecentLoc(n.getNodeid()).getLongname()); // added
  //
  //              if (!(moveimpl.mostRecentLoc(n.getNodeid()) == null)) {
  //                setLongNameBox(moveimpl.mostRecentLoc(n.getNodeid()).getLongname());
  //                setLocationIDBox(makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
  //                setLocButtonVisibility(false);
  //              } else {
  //                setLongNameBox(null);
  //                setLocationIDBox(makeNewNodeID(n.getFloor(), n.getXcoord(), n.getYcoord()));
  //                setLocButtonVisibility(true);
  //              }
  //            }
  //          };
  //      nodeGraphic.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
  //
  //      nodeAnchor.getChildren().add(nodeGraphic);
  //      // nodeAnchor.getChildren().add(locName);
  //    }
  //  }

}
