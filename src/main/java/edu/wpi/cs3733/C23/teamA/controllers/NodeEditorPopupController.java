package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NodeEditorPopupController extends NavigationController {

  @FXML MFXTextField xCoord;
  @FXML MFXTextField yCoord;
  private @FXML MFXComboBox FloorBox;
  private @FXML MFXComboBox BuildingBox;
  @FXML MFXButton createNodeButton;
  @FXML MFXButton saveButton;

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
  public void createNode(ActionEvent actionEvent) throws InterruptedException {
    System.out.println(
        xCoord.getText()
            + " "
            + yCoord.getText()
            + " "
            + BuildingBox.getText()
            + " "
            + FloorBox.getText());
    MapEditorController.mapEditor.createNode(
        xCoord.getText(), yCoord.getText(), BuildingBox.getText(), FloorBox.getText());

    /*
    NodeEntity newNode = new NodeEntity();
    newNode.setXcoord(Integer.parseInt(XCord.getText()));
    newNode.setYcoord(Integer.parseInt(YCord.getText()));
    String tableString = Floor.tableStringFromExtendedString(FloorBox.getText());
    newNode.setFloor(tableString);
    newNode.setBuilding(BuildingBox.getText());
    newNode.setNodeid(
        MapEditorController.makeNewNodeID(
            newNode.getFloor(), newNode.getXcoord(), newNode.getYcoord()));

    // Add new Node to database //
    FacadeRepository.getInstance().addNode(newNode);

    MapEditorController.hideLastSelected("node", newNode);

     */
  }

  @FXML
  public void saveNodeEdit(ActionEvent actionEvent) {}

  @FXML
  public void editNode(ActionEvent actionEvent) {}

  @FXML
  public void deleteSelectedNode(ActionEvent actionEvent) {}
}
