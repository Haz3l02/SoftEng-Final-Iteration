package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.ComputerEntity;
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

// import org.controlsfx.control.PopOver;

public class ComputerController {
  private HashMap<String, ServiceRequestEntities> requests =
      new HashMap<String, ServiceRequestEntities>();

  @FXML private MFXTextField name;
  @FXML private MFXTextField IDnum;
  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXTextField locBox;
  @FXML private MFXTextField issue;
  @FXML private MFXComboBox<String> devicesBox;
  @FXML private MFXComboBox<String> urgencyBox;
  @FXML private Text reminder;
  // private PopOver popup;

  @FXML
  public void initialize() {
    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(
              "Desktop", "Tablet", "Laptop", "Monitor", "Peripherals", "Kiosks", "Printers");
      ObservableList<String> urgencies =
          FXCollections.observableArrayList("Low", "Medium", "High", "Extremely Urgent");

      devicesBox.setItems(devices);
      urgencyBox.setItems(urgencies);
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER_CONFIRMATION);
  }

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HELP);
  }

  @FXML
  public void switchToComputerScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeServiceRequest(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException {
    if (name.getText().equals("")
        || IDnum.getText().equals("")
        || locBox.getText().equals("")
        || issue.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      requests.put(
          IDnum.getText(),
          new ComputerEntity(
              name.getText(),
              IDnum.getText(),
              locBox.getText(),
              issue.getText(),
              urgencyBox.getValue(),
              devicesBox.getValue(),
              deviceIDNum.getText()));

      // *some db thing for getting the request in there*
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
    IDnum.clear();
    deviceIDNum.clear();
    locBox.clear();
    issue.clear();
    devicesBox.clear();
    urgencyBox.clear();
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
