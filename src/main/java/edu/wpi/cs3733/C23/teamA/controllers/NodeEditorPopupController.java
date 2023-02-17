package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NodeEditorPopupController {

  @FXML MFXTextField XCord;
  @FXML MFXTextField YCord;
  @FXML MFXComboBox FloorBox;
  @FXML MFXComboBox BuildingBox;
  @FXML MFXButton createNodeButton;
  @FXML MFXButton saveButton;



  @FXML
  public void createNode(ActionEvent actionEvent) {}

  @FXML
  public void saveNodeEdit(ActionEvent actionEvent) {}

  @FXML
  public void editNode(ActionEvent actionEvent) {}

  @FXML
  public void deleteSelectedNode(ActionEvent actionEvent) {}

  @FXML
  public void switchToNodeScene(ActionEvent actionEvent){
    Navigation.navigate(Screen.HOME_DATABASE);
  }
}
