package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeController extends ServiceRequestController {

  //  @FXML MFXButton navigateButton;
  //  @FXML Text textNotification;

  @FXML
  public void initialize() {}

  /*
  @FXML
  public void switchToIDInput(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.ID_INPUT);
  }

   */

  @FXML
  public void switchToDatabase(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.DATABASE);
  }

  @FXML
  public void switchToPathfinding(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.PATHFINDING);
  }
}
