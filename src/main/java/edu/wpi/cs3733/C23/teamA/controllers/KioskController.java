package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.KioskSetupController.kiosk;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;

public class KioskController {
  @FXML private Label announcement, left, right, leftD, rightD;
  @FXML public SplitPane mainSplitPane;
  @FXML public Node leftPane;
  @FXML public Node mapPane;
  @FXML public Node directionsPane;
  @FXML public Node rightPane;

  @FXML
  public void initialize() {
    leftPane = mainSplitPane.getItems().get(0);
    mapPane = mainSplitPane.getItems().get(1);
    directionsPane = mainSplitPane.getItems().get(2);
    rightPane = mainSplitPane.getItems().get(3);

    announcement.setText(kiosk.getMessage());

    if (kiosk.isDirections()) {
      mainSplitPane.getItems().remove(leftPane);
      mainSplitPane.getItems().remove(rightPane);
      leftD.setText(kiosk.getLeft());
      rightD.setText(kiosk.getRight());
    } else {
      mainSplitPane.getItems().remove(directionsPane);
      left.setText(kiosk.getLeft());
      right.setText(kiosk.getRight());
    }

    // TODO Setup the split pane thing and the map
  }

  @FXML
  public void goBack() {
    Navigation.navigate(Screen.KIOSK_SETUP);
  }
}
