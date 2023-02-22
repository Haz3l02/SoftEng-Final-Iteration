package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DisplayServiceRequestsPopupController {
  @FXML private Text mainText;
  @FXML private Text req1;
  @FXML private Text req2;
  @FXML private Text req3;
  @FXML private Text req4;
  @FXML private MFXButton requestsButton;
  private Text[] labels = new Text[4];

  @FXML
  public void initialize() {
    labels[0] = req1;
    labels[1] = req2;
    labels[2] = req3;
    labels[3] = req4;
  }

  public void addText(String[] serviceRequests) {
    int size = serviceRequests.length;
    for (int i = 0; i < Math.min(size, 4); i++) {
      labels[i].setText(serviceRequests[i]);
    }
  }

  public void setMainText(String text) {
    mainText.setText(text);
  }

  @FXML
  public void viewRequests(ActionEvent event) {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }
}
