package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ComputerRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ComputerRequestImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ComputerController extends ServiceRequestController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;
  DevicesCategory device;

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(DevicesCategory.deviceList());
      devicesBox.setItems(devices);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.
    if (newEdit.needEdits && newEdit.getRequestType().equals("Computer")) {
      ComputerRequestImpl compI = new ComputerRequestImpl();
      ComputerRequestEntity editComputerRequest = compI.get(newEdit.getRequestID());
      nameBox.setText(editComputerRequest.getName());
      IDNum.setText(editComputerRequest.getEmployee().getEmployeeid());
      devicesBox.setText(editComputerRequest.getDevice().toString());
      deviceIDNum.setText(editComputerRequest.getDeviceid());
      locationBox.setText(editComputerRequest.getLocation().getLongname());
      urgencyBox.setText(editComputerRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editComputerRequest.getDescription());
      // compI.closeSession();
    }
    // Otherwise Initialize service requests as normal
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) {
    Navigation.navigate(Screen.COMPUTER_CONFIRMATION);
  }

  @FXML
  void submitRequest(ActionEvent event) {
    ComputerRequestImpl compI = new ComputerRequestImpl();
    LocationNameImpl locationI = new LocationNameImpl();
    EmployeeImpl employeeI = new EmployeeImpl();
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        ComputerRequestEntity submission = compI.get(newEdit.getRequestID());
        LocationNameEntity loc = locationI.get(locationBox.getValue());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        device = DevicesCategory.valueOf(devicesBox.getValue().toUpperCase());

        submission.setName(nameBox.getText());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setDevice(device);
        submission.setDeviceid(deviceIDNum.getText());
      } else {
        EmployeeEntity person = employeeI.get(IDNum.getText());
        LocationNameEntity location = locationI.get(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        device = DevicesCategory.valueOf(devicesBox.getValue().toUpperCase());

        ComputerRequestEntity submission =
            new ComputerRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.COMPUTER,
                Status.BLANK,
                "Unassigned",
                deviceIDNum.getText(),
                device);
        compI.add(submission);
      }
      // compI.closeSession();
      // employeeI.closeSession();
      // locationI.closeSession();

      newEdit.setNeedEdits(false);
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
