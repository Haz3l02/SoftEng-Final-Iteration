package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class HomeController {

  @FXML MFXButton navigateButton;

  @FXML
  public void initialize() {

    // navigateButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
  }

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HELP);
  }

  @FXML
  public void switchToHomeServiceRequest(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }
  // TO DO
  //  @FXML
  //  public void switchToDatabase(ActionEvent event) throws IOException {
  //    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  //  }
  //  @FXML
  //  public void switchToPathfinding(ActionEvent event) throws IOException {
  //    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  //  }

  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close(); // MAY NOT FUCKING WORK
    }
  }
  // Check HomeController OLD for prototype

}
