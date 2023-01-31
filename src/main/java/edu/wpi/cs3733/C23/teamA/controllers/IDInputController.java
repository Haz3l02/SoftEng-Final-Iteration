package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.ServiceRequest;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class IDInputController {
  @FXML public MFXTextField idNumTextField;
  @FXML public Text textNotification;
  private String idNum;

  @FXML
  public void initialize() {
    textNotification.setVisible(false);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void submitButtonPressed(ActionEvent event)
      throws IOException, SQLException, InterruptedException {

    ServiceRequest sr = new ServiceRequest();

    ArrayList<ServiceRequest> specificRequests = new ArrayList<>();
    specificRequests = sr.getServiceRequestsByID(idNumTextField.getText());
    if (specificRequests.size() == 0) {
      textNotification.setVisible(true);
    } else {
      textNotification.setVisible(false);
      String id = idNumTextField.getText();
      IdNumberHolder holder = IdNumberHolder.getInstance();
      holder.setId(id);
      Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
    }
  }
}
