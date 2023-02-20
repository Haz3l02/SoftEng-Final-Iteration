package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.AccessibilityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AccessibilityController extends ServiceRequestController {
  // buttons
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;

  // combo boxes
  @FXML
  private MFXComboBox<String> subjectBox; // harrison is using this in audio/visual too: shared?

  // text fields
  @FXML private MFXTextField disabilityDescBox;
  @FXML private MFXTextField accommodationBox;

  // enums?
  Subject subject;

  public void initialize() throws SQLException {
    super.initialize();
    if (subjectBox != null) {
      ObservableList<String> subjects = FXCollections.observableArrayList(Subject.subjectList());
      subjectBox.setItems(subjects);
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
    }

    if (newEdit.needEdits && newEdit.getRequestType().equals("Accessibility")) {
      AccessibilityRequestEntity editRequest =
          FacadeRepository.getInstance().getAccessabilityRequest(newEdit.getRequestID());

      nameBox.setText(editRequest.getName());
      IDNum.setText(String.valueOf(editRequest.getEmployee().getEmployeeid()));
      subjectBox.setValue(editRequest.getSubject());
      locationBox.setValue(editRequest.getLocation().getLongname());
      urgencyBox.setValue(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      disabilityDescBox.setText(editRequest.getDisability());
      accommodationBox.setText(editRequest.getAccommodation());

      // set buttons enabled/disabled and visible/invisible
      accept.setDisable(true);
      accept.setVisible(false);
      clear.setDisable(false);
      clear.setVisible(true);
      submit.setDisable(false);
      submit.setVisible(true);
      reject.setDisable(true);
      reject.setVisible(false);

    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Accessibility")) {
      AccessibilityRequestEntity editRequest =
          FacadeRepository.getInstance().getAccessabilityRequest(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(String.valueOf(editRequest.getEmployee().getEmployeeid()));
      subjectBox.setValue(editRequest.getSubject());
      locationBox.setValue(editRequest.getLocation().getLongname());
      urgencyBox.setValue(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      disabilityDescBox.setText(editRequest.getDisability());
      accommodationBox.setText(editRequest.getAccommodation());

      // set buttons enabled/disabled and visible/invisible
      accept.setDisable(false);
      accept.setVisible(true);
      clear.setDisable(true);
      clear.setVisible(false);
      submit.setDisable(true);
      submit.setVisible(false);
      reject.setDisable(false);
      reject.setVisible(true);
    }
  }

  public void submitRequest(ActionEvent event) {
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || disabilityDescBox.getText().equals("")
        || accommodationBox.getText().equals("")
        || subjectBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        subject = Subject.valueOf(subjectBox.getValue().toUpperCase());

        AccessibilityRequestEntity submission =
            FacadeRepository.getInstance().getAccessabilityRequest(newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setDisability(disabilityDescBox.getText());
        submission.setAccommodation(accommodationBox.getText());
        submission.setUrgency(urgent);
        submission.setSubject(subject.getSubject()); // currently a string - enum instead?
      } else {
        EmployeeEntity person =
            FacadeRepository.getInstance().getEmployee(Integer.parseInt(IDNum.getText()));
        LocationNameEntity location =
            FacadeRepository.getInstance().getLocation(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        subject = Subject.valueOf(subjectBox.getValue().toUpperCase());

        AccessibilityRequestEntity submission =
            new AccessibilityRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.ACCESSIBILITY,
                Status.NEW,
                "Unassigned",
                subject.getSubject(),
                disabilityDescBox.getText(),
                accommodationBox.getText());
        FacadeRepository.getInstance().addAccessability(submission);
      }

      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    subjectBox.clear();
    disabilityDescBox.clear();
    accommodationBox.clear();
  }
}
