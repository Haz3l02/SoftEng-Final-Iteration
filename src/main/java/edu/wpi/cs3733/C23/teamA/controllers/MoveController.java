package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.ImageLoader;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInfo;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathfindingSystem;
import edu.wpi.cs3733.C23.teamA.pathfinding.algorithms.AStar;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import jakarta.persistence.PersistenceException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class MoveController extends MenuController {

  @FXML private TableView<MoveEntity> dbTable;

  @FXML public TableColumn<MoveEntity, String> nodeCol;
  @FXML public TableColumn<MoveEntity, String> moveCol;
  @FXML public TableColumn<MoveEntity, String> moveDateCol;
  @FXML public TableColumn<MoveEntity, String> messageCol;

  @FXML public MFXFilterComboBox<String> nodeBox;
  @FXML public MFXFilterComboBox<String> locationBox;
  @FXML public MFXDatePicker dateBox;
  @FXML public MFXButton submit;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton createMove;

  @FXML protected Text warning;
  private static PathfindingSystem pathfindingSystem = new PathfindingSystem(new AStar());
  @FXML private Text locationNotif;
  @FXML private Text currentNode;
  @FXML private Text newNode;
  @FXML private TextArea moveMessage;

  // List of all Node IDs in specific order
  private List<String> allNodeID;
  private List<String> allLongNames; // List of corresponding long names in order
  private AnchorPane[] aps = new AnchorPane[5];
  private AnchorPane[] aps1 = new AnchorPane[5];

  private int currentFloor = 1;

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  List<MoveEntity> moveData = new ArrayList<>();
  List<MoveEntity> currentMoves = new ArrayList<>();

  @FXML private ImageView mainImageView; // imageview to be changed when each floor is selected
  @FXML private ImageView topMainImageView; // imageview to be changed when each floor is selected
  @FXML private AnchorPane anchorF3 = new AnchorPane();
  @FXML private AnchorPane anchorF2 = new AnchorPane();
  @FXML private AnchorPane anchorF1 = new AnchorPane();
  @FXML private AnchorPane anchorL2 = new AnchorPane();
  @FXML private AnchorPane anchorL1 = new AnchorPane();

  @FXML private AnchorPane serviceRequestPane; // displays service requests on currentFloor
  @FXML private AnchorPane textAnchorPane; // displays location names on currentFloor
  @FXML private StackPane mainStackPane; // stack pane with all the anchor panes and image view
  @FXML private StackPane mainStackPane1; // stack pane with all the anchor panes and image view
  @FXML private GesturePane middleGesturePane; // gesture pane to sync with stack pane above
  @FXML private GesturePane topMainGesturePane; // gesture pane to sync with stack pane above

  // private PathfindingController controller = new PathfindingController();
  private String clickedNode;
  private String clickedLocation;
  private LocalDate clickedDate;
  @FXML public SplitPane imagePane;
  @FXML public SplitPane mainPane;
  @FXML public Node allImages;
  @FXML public Node currentNodeImage;
  @FXML public Node newNodeImage;

  /** runs on switching to this scene */
  public void initialize() {
    // Removing the view of the maps
    allImages = mainPane.getItems().get(1);
    mainPane.getItems().remove(allImages);

    currentNodeImage = imagePane.getItems().get(1); // 1st image
    newNodeImage = imagePane.getItems().get(2); // 2nd Image

    moveData = FacadeRepository.getInstance().getAllMove();

    currentMoves = FacadeRepository.getInstance().moveAllMostRecent(LocalDate.now());

    warning.setVisible(false);
    // anchor panes
    aps[0] = anchorL1;
    aps[1] = anchorL2;
    aps[2] = anchorF1;
    aps[3] = anchorF2;
    aps[4] = anchorF3;

    this.topMainGesturePane.setContent(mainStackPane);
    this.middleGesturePane.setContent(mainStackPane1);

    allNodeID =
        FacadeRepository.getInstance().getAllMove().stream()
            .map(moveEntity -> moveEntity.getNode().getNodeid())
            .toList();
    allLongNames =
        FacadeRepository.getInstance().getAllMove().stream()
            .map(moveEntity -> moveEntity.getLocationName().getLongname())
            .toList();

    ObservableList<String> nodes = FXCollections.observableArrayList(allNodeID);
    ObservableList<String> locationNames = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(nodes);
    locationBox.setItems(locationNames);

    nodeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getNode().getNodeid()));
    moveCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    moveDateCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));
    messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

    dbTableRowsModel.addAll(moveData);
    dbTable.setItems(dbTableRowsModel);

    // Location and Node Observer
    locationBox
        .valueProperty()
        .addListener(
            observable -> {
              if (locationBox.getValue() != null) {
                clearEdits();
                GraphNode start =
                    pathfindingSystem.getNode(
                        FacadeRepository.getInstance()
                            .moveLocationRecord(locationBox.getValue(), LocalDate.now())
                            .get(0)
                            .getNode()
                            .getNodeid()); //
                PathInfo pathInfo = pathfindingSystem.runPathfinding(start, start);
                String initialTableString = pathInfo.getFloorPath().get(0);
                currentNode.setText("Current Node Floor " + initialTableString);
                addFloorMapImage(initialTableString, mainImageView);
                locationNotif.setText(
                    "You have selected " + locationBox.getValue().toString() + " to move nodes.");
                createMove.setVisible(true);
                editButton.setVisible(false);
                // createMove.setDisable(true);
                nodeBox.setDisable(false);
                if (mainPane.getItems().size() == 1)
                  mainPane.getItems().add(1, allImages); // If there is nothing add it
                imagePane.getItems().remove(newNodeImage);
                ArrayList<GraphNode> path = pathInfo.getPath();
                ArrayList<String> floorPath = pathInfo.getFloorPath();
                callMapDraw(path, floorPath);

                mainStackPane.getChildren().clear();
                mainStackPane.getChildren().add(mainImageView);
                mainStackPane
                    .getChildren()
                    .add(aps[Floor.indexFromTableString(initialTableString)]);
              }
            });
    nodeBox
        .valueProperty()
        .addListener(
            observable -> {
              if (nodeBox.getValue() != null) {
                System.out.println(nodeBox.getValue());
                GraphNode start = pathfindingSystem.getNode(nodeBox.getValue().toString());
                PathInfo pathInfo = pathfindingSystem.runPathfinding(start, start);
                String finalTableString = pathInfo.getFloorPath().get(0);
                newNode.setText("New Node Floor " + finalTableString);
                addFloorMapImage(finalTableString, topMainImageView);
                locationNotif.setText(
                    "You have selected "
                        + locationBox.getValue().toString()
                        + " to move to node "
                        + nodeBox.getValue().toString());
                editButton.setDisable(false);
                if (imagePane.getItems().size() == 2) imagePane.getItems().add(2, newNodeImage);
                ArrayList<GraphNode> path = pathInfo.getPath();
                ArrayList<String> floorPath = pathInfo.getFloorPath();
                callMapDraw(path, floorPath);

                mainStackPane1.getChildren().clear();
                mainStackPane1.getChildren().add(topMainImageView);
                mainStackPane1.getChildren().add(aps[Floor.indexFromTableString(finalTableString)]);
              }
            });
  }

  private void addFloorMapImage(String floor, ImageView iv) {
    Image image = ImageLoader.getImage(floor);
    iv.setImage(image);
  }

  public void generatePathFromMovePopup(String startID, String endID)
      throws SQLException, RuntimeException {
    // create the graph hashMap where String is nodeId and GraphNode is the node
    pathfindingSystem.prepGraphDB(LocalDate.now()); // don't know if this will work tbh

    // run pathfinding
    GraphNode start = pathfindingSystem.getNode(startID); //
    GraphNode end = pathfindingSystem.getNode(endID); // Value in the moveTable

    // runs pathfinding
    PathInfo pathInfo = pathfindingSystem.runPathfinding(start, end);
    String initialTableString = pathInfo.getFloorPath().get(0);
    currentNode.setText("Current Node Floor " + initialTableString);
    addFloorMapImage(initialTableString, mainImageView);
    currentFloor = Floor.indexFromTableString(initialTableString);

    String finalTableString = initialTableString;

    if (pathInfo.getFloorPath().size() != 1) {

      if (imagePane.getItems().size() == 2) imagePane.getItems().add(2, newNodeImage);
      finalTableString = pathInfo.getFloorPath().get(pathInfo.getFloorPath().size() - 1);
      newNode.setText("New Node Floor " + finalTableString);
      addFloorMapImage(finalTableString, topMainImageView);
    } else imagePane.getItems().remove(newNodeImage); // No second image

    // if pathInfo isn't null, grab the path and draw it
    if (pathInfo != null) {
      // get the paths from pathInfo
      ArrayList<GraphNode> path = pathInfo.getPath();
      ArrayList<String> floorPath = pathInfo.getFloorPath();
      callMapDraw(path, floorPath);

      mainStackPane.getChildren().clear();
      mainStackPane1.getChildren().clear();
      mainStackPane.getChildren().add(mainImageView);
      mainStackPane1.getChildren().add(topMainImageView);
      mainStackPane.getChildren().add(aps[Floor.indexFromTableString(initialTableString)]);
      if (!initialTableString.equals(finalTableString))
        mainStackPane1.getChildren().add(aps[Floor.indexFromTableString(finalTableString)]);
    }
  }
  /**
   * Given a path, draw it on its anchorPane and hide the other anchorPanes
   *
   * @param path the path that you want to be drawn
   */
  private void callMapDraw(ArrayList<GraphNode> path, ArrayList<String> floorPath) {

    // clear the anchorPanes w/ the drawn paths
    for (AnchorPane ap : aps) {
      ap.getChildren().clear();
      ap.setVisible(true);
    }
    aps[currentFloor].setVisible(true);

    //      for (AnchorPane ap : aps1) {
    //        ap.getChildren().clear();
    //        ap.setVisible(false);
    //      }
    //      aps1[currentFloor].setVisible(true);

    // Make this floor's pane viewable
    // aps[currentFloor].setVisible(true);

    pathfindingSystem.drawPath(aps, path);
  }
  /** Clear and retrieve all table rows again With hibernate only use once at start */
  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      moveData = FacadeRepository.getInstance().getAllMove();
      dbTableRowsModel.addAll(moveData);

      clearEdits();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void delete(ActionEvent event) {
    List<String> moveIDs = new ArrayList<>();
    moveIDs.add(nodeBox.getValue());
    moveIDs.add(locationBox.getValue());
    moveIDs.add(dateBox.getValue().toString());
    FacadeRepository.getInstance().deleteMove(moveIDs);
    reloadData();
  }

  @FXML
  public void createMove(ActionEvent event) {
    LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
    MoveEntity theMove =
        new MoveEntity(
            FacadeRepository.getInstance().getNode(nodeBox.getValue()),
            loc,
            dateBox.getValue(),
            moveMessage.getText());
    try {
      warning.setVisible(false);
      FacadeRepository.getInstance().addMove(theMove);
    } catch (PersistenceException p) {
      warning.setVisible(true);
    }
    // moveImpl.add(theMove);
    reloadData();
  }

  public void submitEdit(ActionEvent event) {
    //    if (!nodeBox.getText().trim().isBlank()
    //        || !locationBox.getText().trim().isBlank()
    //        || !dateBox.getValue().toString().isEmpty()) {

    ObservableList<MoveEntity> currentTableData = dbTable.getItems();

    //      LocalDate moveDate = dateBox.getValue();
    //      String nodeID = nodeBox.getValue();
    //      String theLocation = locationBox.getValue();
    //      String submitDate = dateBox.getText().toString();

    for (MoveEntity move : currentTableData) {
      if (move.getMovedate().equals(clickedDate)
          && move.getLocationName().getLongname().equals(clickedLocation)
          && move.getNode().getNodeid().equals(clickedNode)) {
        List<String> moveID = new ArrayList<>();
        moveID.add(clickedNode);
        moveID.add(clickedLocation);
        moveID.add(clickedDate.toString());

        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(clickedLocation);

        move.setNode(FacadeRepository.getInstance().getNode(clickedNode));
        move.setLocationName(loc);
        move.setMovedate(clickedDate);
        move.setMessage(moveMessage.getText());
        System.out.println(moveMessage.getText());
        FacadeRepository.getInstance().moveUpdateMessage(moveMessage.getText(), moveID);
        // FacadeRepository.getInstance().updateMove(moveID, move);
        dbTable.setItems(currentTableData);
        reloadData();
        break;
      }
    }
    //    }
  }

  @FXML
  public void switchToImport(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.IMPORT_CSV);
  }

  public void switchToExport(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.EXPORT_CSV);
  }

  public void clearEdits() {
    nodeBox.clear();
    locationBox.clear();
    dateBox.setValue(dateBox.getCurrentDate());
    moveMessage.clear();
    for (AnchorPane ap : aps) {
      ap.getChildren().clear();
      ap.setVisible(false);
    }
    //        createEmployee.setVisible(true);
    //        editButton.setDisable(true);
  }

  @FXML
  public void rowClicked(MouseEvent event) throws SQLException {
    if (mainPane.getItems().size() == 1) {
      mainPane.getItems().add(1, allImages);
    }
    editButton.setVisible(true);

    MoveEntity clickedMoveTableRow = dbTable.getSelectionModel().getSelectedItem();

    if (clickedMoveTableRow != null) {
      clickedNode = clickedMoveTableRow.getNode().getNodeid();
      clickedLocation = (clickedMoveTableRow.getLocationName().getLongname());
      clickedDate = clickedMoveTableRow.getMovedate();
      moveMessage.setText(clickedMoveTableRow.getMessage());

      editButton.setDisable(false);
      deleteButton.setDisable(false);
      createMove.setVisible(false);

      locationNotif.setText(
          "The location "
              + clickedMoveTableRow.getLocationName().getLongname()
              + " is moving on "
              + clickedMoveTableRow.getMovedate());
      for (MoveEntity m : currentMoves) {
        if (m.getLocationName()
            .getLongname()
            .equalsIgnoreCase(clickedMoveTableRow.getLocationName().getLongname())) {

          generatePathFromMovePopup(
              m.getNode().getNodeid(), clickedMoveTableRow.getNode().getNodeid());
        }
      }
    }

    ObservableList<String> node = FXCollections.observableArrayList(allNodeID);
    ObservableList<String> location = FXCollections.observableArrayList(allLongNames);

    nodeBox.setItems(node);
    locationBox.setItems(location);
  }
}
