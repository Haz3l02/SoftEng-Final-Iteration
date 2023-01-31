package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class HomeServiceRequestController {
  @FXML
  public void initialize() {}

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HELP);
  }

  @FXML
  public void switchToHome(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToSanitation(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToSecurity(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToComputer(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

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
}
