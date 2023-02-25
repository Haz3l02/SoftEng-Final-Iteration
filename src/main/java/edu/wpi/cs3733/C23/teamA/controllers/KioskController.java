package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KioskController {
  @FXML Label announcement, left, right;

  @FXML
  public void initialize() {
    announcement.setText(kiosk.getMessage());
    left.setText(kiosk.getLeft());
    right.setText(kiosk.getRight());

    // TODO Setup the split pane thing and the map
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }
}
