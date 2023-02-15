package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ComputerRequestImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import io.github.palexdev.materialfx.controls.MFXButton;
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
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;
  private ComputerRequestImpl compI = new ComputerRequestImpl();
  DevicesCategory device;

  @FXML
  public void initialize() throws SQLException {
    super.initialize();

    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(DevicesCategory.deviceList());
      devicesBox.setItems(devices);
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.
    ComputerRequestEntity editComputerRequest = null;
    if (newEdit.needEdits && newEdit.getRequestType().equals("Computer")) {
      // ComputerRequestImpl compI = new ComputerRequestImpl();
      editComputerRequest = compI.get(newEdit.getRequestID());
      nameBox.setText(editComputerRequest.getName());
      IDNum.setText(editComputerRequest.getEmployee().getEmployeeid());
      devicesBox.setText(editComputerRequest.getDevice().toString());
      deviceIDNum.setText(editComputerRequest.getDeviceid());
      locationBox.setText(editComputerRequest.getLocation().getLongname());
      urgencyBox.setText(editComputerRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editComputerRequest.getDescription());
      // compI.closeSession();
    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Computer")) {
      ComputerRequestEntity editRequest = compI.get(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      devicesBox.setText(editRequest.getDevice().toString());
      deviceIDNum.setText(editRequest.getDeviceid());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      // sanI.closeSession();
      accept.setDisable(false);
      accept.setVisible(true);
      clear.setDisable(true);
      clear.setVisible(false);
      submit.setDisable(true);
      submit.setVisible(false);
      reject.setDisable(false);
      reject.setVisible(true);
    }
    // Otherwise Initialize service requests as normal
  }

  @FXML
  void submitRequest(ActionEvent event) {
    // ComputerRequestImpl compI = new ComputerRequestImpl();
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
                Status.NEW,
                "Unassigned",
                deviceIDNum.getText(),
                device);
        compI.add(submission);
      }
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
