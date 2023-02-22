package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
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
  private IdNumberHolder holder = IdNumberHolder.getInstance();

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
      editComputerRequest =
          FacadeRepository.getInstance().getComputerRequest(newEdit.getRequestID());
      nameBox.setText(editComputerRequest.getName());
      IDNum.setText(editComputerRequest.getEmployee().getHospitalid());
      devicesBox.setText(editComputerRequest.getDevice().toString());
      deviceIDNum.setText(editComputerRequest.getDeviceid());
      locationBox.setText(editComputerRequest.getLocation().getLongname());
      urgencyBox.setText(editComputerRequest.getUrgency().getUrgency());
      locationBox.setValue(editComputerRequest.getLocation().getLongname());
      urgencyBox.setValue(editComputerRequest.getUrgency().getUrgency());
      descBox.setText(editComputerRequest.getDescription());
      // compI.closeSession();
    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Computer")) {
      ComputerRequestEntity editRequest =
          FacadeRepository.getInstance().getComputerRequest(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getHospitalid());
      devicesBox.setText(editRequest.getDevice().toString());
      deviceIDNum.setText(editRequest.getDeviceid());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      locationBox.setValue(editRequest.getLocation().getLongname());
      urgencyBox.setValue(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      // sanI.closeSession();
      if (holder.getJob().equalsIgnoreCase("admin")) {
        accept.setDisable(true);
        reject.setDisable(true);
      } else {
        accept.setDisable(false);
        reject.setDisable(false);
        reject.setVisible(true);
      }
      accept.setVisible(true);
      clear.setDisable(true);
      clear.setVisible(false);
      submit.setDisable(true);
      submit.setVisible(false);
    }
    // Otherwise Initialize service requests as normal
  }

  @FXML
  void submitRequest(ActionEvent event) {
    // ComputerRequestImpl compI = new ComputerRequestImpl();
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
        ComputerRequestEntity submission =
            FacadeRepository.getInstance().getComputerRequest(newEdit.getRequestID());
        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        device = DevicesCategory.valueOf(devicesBox.getValue().toUpperCase());

        submission.setName(nameBox.getText());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setDevice(device);
        submission.setDeviceid(deviceIDNum.getText());
      } else {
        EmployeeEntity person = FacadeRepository.getInstance().getEmployee(employeeID);
        EmployeeEntity unassigned = FacadeRepository.getInstance().getEmployee(0);

        LocationNameEntity location =
            FacadeRepository.getInstance().getLocation(locationBox.getText());

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
                null,
                deviceIDNum.getText(),
                device);
        FacadeRepository.getInstance().addComputerRequest(submission);
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
