package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

// import org.controlsfx.control.PopOver;

public class ComputerController extends ServiceRequestController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;

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
  public void switchToComputerScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException {
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locBox.getText().equals("")
        || descBox.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      //      requests.put(
      //          IDNum.getText(),
      //          new ComputerEntity(
      //              nameBox.getText(),
      //              IDNum.getText(),
      //              locBox.getText(),
      //              descBox.getText(),
      //              urgencyBox.getValue(),
      //              devicesBox.getValue(),
      //              deviceIDNum.getText(),
      //              "Computer Request"));

      // *some db thing for getting the request in there*
      //      System.out.println(
      //          "NAME: "
      //              + (requests.get(IDNum.getText())).getName()
      //              + " ID Number: "
      //              + (requests.get(IDNum.getText())).getIDNum());

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
