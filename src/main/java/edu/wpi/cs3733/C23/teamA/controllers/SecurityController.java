package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.SecurityEntity;
import edu.wpi.cs3733.C23.teamA.ServiceRequestEntities;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

public class SecurityController {
  private HashMap<String, ServiceRequestEntities> requests =
      new HashMap<String, ServiceRequestEntities>();

  @FXML private MFXTextField name;
  @FXML private MFXTextField phone;
  @FXML private MFXTextField IDnum;
  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXTextField locBox;
  @FXML private MFXTextField issue;
  @FXML private MFXComboBox<String> requestsBox;
  @FXML private MFXComboBox<String> urgencyBox;
  @FXML private Text reminder;
  // private PopOver popup;

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
    String num = IDnum.getText();
    if (name.getText().equals("")
        || phone.getText().equals("")
        || IDnum.getText().equals("")
        || locBox.getText().equals("")
        || issue.getText().equals("")
        || requestsBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      requests.put(
          IDnum.getText(),
          new SecurityEntity(
              name.getText(),
              phone.getText(),
              IDnum.getText(),
              locBox.getText(),
              issue.getText(),
              urgencyBox.getValue(),
              requestsBox.getValue()));

      // *some db thing for getting the request in there*
      System.out.println("this submits data");
      System.out.println(
          "NAME: "
              + (requests.get(IDnum.getText())).getName()
              + " ID Number: "
              + (requests.get(IDnum.getText())).getIdNum());
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    name.clear();
    phone.clear();
    IDnum.clear();
    locBox.clear();
    issue.clear();
    requestsBox.clear();
    urgencyBox.clear();
  }

  @FXML
  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close();
    }
  }
}
