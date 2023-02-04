package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeServiceRequestController extends ServiceRequestController {

  @FXML MFXButton pastSubmission;

  @FXML
  public void initialize() {}

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

  @FXML
  public void switchToIDInput(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.ID_INPUT);
  }

  @FXML
  public void switchToServiceRequestStatus(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }
}
