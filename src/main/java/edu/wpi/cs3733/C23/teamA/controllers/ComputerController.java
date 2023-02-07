package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ComputerRequest;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ComputerController extends ServiceRequestController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;

  @FXML
  public void initialize() {
    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(
              DevicesCategory.DESKTOP.getDevices(),
              DevicesCategory.TABLET.getDevices(),
              DevicesCategory.LAPTOP.getDevices(),
              DevicesCategory.MONITOR.getDevices(),
              DevicesCategory.PERIPHERALS.getDevices(),
              DevicesCategory.KIOSK.getDevices(),
              DevicesCategory.PRINTER.getDevices());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(
              UrgencyLevel.LOW.getUrgency(),
              UrgencyLevel.MEDIUM.getUrgency(),
              UrgencyLevel.HIGH.getUrgency(),
              UrgencyLevel.EXTREMELY_URGENT.getUrgency());

      devicesBox.setItems(devices);
      urgencyBox.setItems(urgencies);
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER_CONFIRMATION);
  }

  @FXML
  public void switchToComputerScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locBox.getText().equals("")
        || descBox.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      ComputerRequest submission =
          new ComputerRequest(
              nameBox.getText(),
              IDNum.getText(),
              locBox.getText(),
              descBox.getText(),
              urgencyBox.getValue(),
              "Computer Request",
              "Blank",
              "Unassigned",
              deviceIDNum.getText(),
              devicesBox.getValue());

      submission.insert(); // *some db thing for getting the request in there*

      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    deviceIDNum.clear();
    devicesBox.clear();
  }
}
