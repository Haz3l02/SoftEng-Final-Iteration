package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Building;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Setter;

public class NodeEditorEditPopupController extends NavigationController {

  @FXML MFXComboBox<String> FloorBox;
  @FXML MFXComboBox<String> BuildingBox;
  @FXML MFXTextField xCoord;
  @FXML MFXTextField yCoord;

  @Setter private static int xCord;
  @Setter private static int yCord;
  @Setter private static String floor;
  @Setter private static String building;
  @Setter private static NodeEntity node;

  public void initialize() {

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

    xCoord.setText(String.valueOf(xCord));
    yCoord.setText(String.valueOf(yCord));
    FloorBox.setText(floor);
    BuildingBox.setText(building);
  }

  @FXML
  public void saveNodeEdit(ActionEvent event) {

    System.out.println("submit node edits");
    System.out.println(node.getNodeid());

    node.setXcoord(Integer.parseInt(xCoord.getText()));
    node.setYcoord(Integer.parseInt(yCoord.getText()));
    node.setBuilding(BuildingBox.getText());
    node.setFloor(FloorBox.getText());
    node.setNodeid(node.getNodeid());

    FacadeRepository.getInstance().updateNode(node.getNodeid(), node);

    mapEditor.closePopup("node edit");
    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    mapEditor.closePopup("node edit");
  }
}
