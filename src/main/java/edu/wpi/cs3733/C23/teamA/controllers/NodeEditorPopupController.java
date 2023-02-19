package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.makeNewNodeID;
import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.*;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class NodeEditorPopupController extends NavigationController {

  @FXML MFXTextField xCoord;
  @FXML MFXTextField yCoord;
  private @FXML MFXComboBox<String> FloorBox;
  private @FXML MFXComboBox<String> BuildingBox;
  @FXML MFXButton createNodeButton;

  public void initialize() {
    populateStuff();
  }

  public void populateStuff() {

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
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    mapEditor.closePopup("node");
  }

  @FXML
  public void createNode(ActionEvent actionEvent) {

    // Create a new node entity
    NodeEntity newNode = new NodeEntity();

    newNode.setXcoord(Integer.parseInt(xCoord.getText()));
    newNode.setYcoord(Integer.parseInt(yCoord.getText()));
    String tableString = Floor.tableStringFromExtendedString(FloorBox.getText());
    newNode.setFloor(tableString);
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(makeNewNodeID(newNode.getFloor(), newNode.getXcoord(), newNode.getYcoord()));
    // Add new Node to database //
    FacadeRepository.getInstance().addNode(newNode);

    // switch box screen
    // createNodeButton.setVisible(false);
    // fieldBox.setStyle("-fx-background-color: '#bad1ea'; ");

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

    MapEditorController.mapEditor.closePopup("node");

    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void saveNodeEdit(ActionEvent actionEvent) {}

  @FXML
  public void editNode(ActionEvent actionEvent) {}

  @FXML
  public void deleteSelectedNode(ActionEvent actionEvent) {}
}
