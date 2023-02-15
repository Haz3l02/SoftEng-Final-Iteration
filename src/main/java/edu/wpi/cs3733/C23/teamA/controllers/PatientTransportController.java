package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.PatientTransportimpl;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PatientTransportController extends ServiceRequestController {
  @FXML private MFXTextField pNameBox;
  @FXML private MFXTextField equipmentBox;
  @FXML private MFXTextField pIDBox;
  @FXML private MFXFilterComboBox<String> moveToBox;
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;

  private PatientTransportimpl patI = new PatientTransportimpl();

  @FXML
  public void initialize() throws SQLException {
    super.initialize();

    if (moveToBox != null) {
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
      LocationNameImpl locationI = new LocationNameImpl();
      List<LocationNameEntity> temp = locationI.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }
      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);
      moveToBox.setItems(locations);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.
    if (newEdit.needEdits && newEdit.getRequestType().equals("PatientTransport")) {
      PatientTransportRequestEntity editPatientRequest = patI.get(newEdit.getRequestID());
      nameBox.setText(editPatientRequest.getName());
      IDNum.setText(editPatientRequest.getEmployee().getEmployeeid());
      urgencyBox.setText(editPatientRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editPatientRequest.getDescription());
      pNameBox.setText(editPatientRequest.getPatientName());
      locationBox.setText(editPatientRequest.getLocation().getLongname());
      moveToBox.setText(editPatientRequest.getMoveTo().getLongname());
      pIDBox.setText(editPatientRequest.getPatientID());
      equipmentBox.setText(editPatientRequest.getEquipment());
    } else if (acceptTheForm.acceptance
        && acceptTheForm.getRequestType().equals("Patient Transport")) {
      PatientTransportRequestEntity editPatientRequest = patI.get(acceptTheForm.getRequestID());
      nameBox.setText(editPatientRequest.getName());
      IDNum.setText(editPatientRequest.getEmployee().getEmployeeid());
      urgencyBox.setText(editPatientRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editPatientRequest.getDescription());
      pNameBox.setText(editPatientRequest.getPatientName());
      locationBox.setText(editPatientRequest.getLocation().getLongname());
      moveToBox.setText(editPatientRequest.getMoveTo().getLongname());
      pIDBox.setText(editPatientRequest.getPatientID());
      equipmentBox.setText(editPatientRequest.getEquipment());
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

  //  @FXML
  //  public void switchToConfirmationScene(ActionEvent event) throws IOException {
  //    Navigation.navigate(Screen.PATIENT_CONFIRMATION);
  //  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {
    PatientTransportimpl patI = new PatientTransportimpl();
    LocationNameImpl locationI = new LocationNameImpl();
    EmployeeImpl employeeI = new EmployeeImpl();
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || urgencyBox.getValue() == null
        || pNameBox.getText().equals("")
        || pIDBox.getText().equals("")
        || moveToBox.getValue() == null
        || equipmentBox.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        // something that submits it
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());

        PatientTransportRequestEntity submission = patI.get(newEdit.getRequestID());
        // supers
        submission.setName(nameBox.getText());
        LocationNameEntity loc1 = locationI.get(locationBox.getValue());
        submission.setLocation(loc1);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        // sub fields
        submission.setPatientID(pIDBox.getText());
        submission.setPatientID(pIDBox.getText());
        LocationNameEntity loc2 = locationI.get(moveToBox.getValue());
        submission.setMoveTo(loc2);
        submission.setEquipment(equipmentBox.getText());
      } else {
        EmployeeEntity person = employeeI.get(IDNum.getText());
        // IDNum.getText()
        LocationNameEntity loc = locationI.get(locationBox.getText());
        LocationNameEntity moveTo = locationI.get(moveToBox.getValue());
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());

        PatientTransportRequestEntity submission =
            new PatientTransportRequestEntity(
                nameBox.getText(),
                person,
                loc,
                descBox.getText(),
                urgent,
                PatientTransportRequestEntity.RequestType.PATIENT_TRANSPORT,
                Status.NEW,
                "Unassigned",
                pNameBox.getText(),
                pIDBox.getText(),
                moveTo,
                equipmentBox.getText());
        patI.add(submission);
        // submission.insert(); // *some db thing for getting the request in there*
      }
      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  //  @FXML
  //  public void switchToHomeScene(ActionEvent event) throws IOException {
  //    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  //  }

  @FXML
  void clearForm() {
    pNameBox.clear();
    pIDBox.clear();
    moveToBox.clear();
    equipmentBox.clear();
  }
}
