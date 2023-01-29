package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.SecurityEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SecurityController extends ServiceRequestController {

  @FXML private MFXTextField phone;
  @FXML private MFXComboBox<String> requestsBox;

  @FXML
  public void initialize() {
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList("Harassment", "Security Escort", "Potential Threat");
      ObservableList<String> urgencies =
          FXCollections.observableArrayList("Low", "Medium", "High", "Extremely Urgent");

      requestsBox.setItems(requests);
      urgencyBox.setItems(urgencies);
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HELP);
  }

  @FXML
  public void switchToSecurityScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException {
    if (nameBox.getText().equals("")
        || phone.getText().equals("")
        || IDNum.getText().equals("")
        || locBox.getText().equals("")
        || descBox.getText().equals("")
        || requestsBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      requests.put(
          IDNum.getText(),
          new SecurityEntity(
              nameBox.getText(),
              phone.getText(),
              IDNum.getText(),
              locBox.getText(),
              descBox.getText(),
              urgencyBox.getValue(),
              requestsBox.getValue(),
              "Security Request"));

      // *some db thing for getting the request in there*
      System.out.println("this submits data");
      System.out.println(
          "NAME: "
              + (requests.get(IDNum.getText())).getName()
              + " ID Number: "
              + (requests.get(IDNum.getText())).getIDNum());
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    phone.clear();
    requestsBox.clear();
  }
}
