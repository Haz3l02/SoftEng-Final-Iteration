package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.hibernate.Session;

public class IDInputController {
  @FXML public MFXTextField idNumTextField;
  @FXML public Text textNotification;

  @FXML
  public void initialize() {
    textNotification.setVisible(false);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void submitButtonPressed(ActionEvent event) {
    Session session = getSessionFactory().openSession();

    ArrayList<ServiceRequestEntity> specificRequests = new ArrayList<>();
    specificRequests =
        ServiceRequestEntity.getServiceRequestsByID(idNumTextField.getText(), session);
    if (specificRequests.size() == 0) {
      textNotification.setVisible(true);
    } else {
      textNotification.setVisible(false);
      session.close();
      Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
    }
  }
}
