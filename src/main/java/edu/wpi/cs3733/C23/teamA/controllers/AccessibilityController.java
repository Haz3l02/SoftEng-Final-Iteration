package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Subject;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import edu.wpi.cs3733.C23.teamA.Database.Entities.AccessibilityRequestEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.SQLException;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

public class AccessibilityController extends ServiceRequestController {
  // buttons
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;

  // combo boxes
  @FXML private MFXComboBox<String> subjectBox;

  // text fields
  @FXML private MFXTextField disabilityDescBox;
  @FXML private MFXTextField accommodationBox;

  // enums?
  Subject subject;

  public void initialize() throws SQLException {
    /*
      super.initialize();
      if (subjectBox != null) {
          ObservableList<String> subjects =
                  FXCollections.observableArrayList(Subject.subjectList());
          subjectBox.setItems(subjects);
          reject.setDisable(true);
          reject.setVisible(false);
          accept.setDisable(true);
          accept.setVisible(false);
      }

      if (newEdit.needEdits && newEdit.getRequestType().equals("Accessibility")) { // todo
          AccessibilityRequestEntity editRequest =
                  FacadeRepository.getInstance().getAccessibilityRequest(newEdit.getRequestID());
          nameBox.setText(editRequest.getName());
          IDNum.setText(editRequest.getEmployee().getEmployeeid());
          subjectBox.setValue(editRequest.getSubject().getSubject());
          locationBox.setValue(editRequest.getLocation().getLongname());
          urgencyBox.setValue(editRequest.getUrgency().getUrgency());
          descBox.setText(editRequest.getDescription());
          disabilityDescBox.setText(editRequest.getDisabilityDesc());
          accommodationBox.setText(editRequest.getAccomodationDesc());
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
                  FacadeRepository.getInstance().getAccessibilityRequest(acceptTheForm.getRequestID());
          nameBox.setText(editRequest.getName());
          IDNum.setText(editRequest.getEmployee().getEmployeeid());
          subjectBox.setValue(editRequest.getSubject().getSubject());
          locationBox.setValue(editRequest.getLocation().getLongname());
          urgencyBox.setValue(editRequest.getUrgency().getUrgency());
          descBox.setText(editRequest.getDescription());
          disabilityDescBox.setText(editRequest.getDisabilityDesc());
          accommodationBox.setText(editRequest.getAccomodationDesc());
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
     */
  }

  public void submitRequest(ActionEvent event) {
    /*
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
                      FacadeRepository.getInstance().getAccessibilityRequest(newEdit.getRequestID());
              submission.setName(nameBox.getText());
              LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
              submission.setLocation(loc);
              submission.setDescription(descBox.getText());
              submission.setUrgency(urgent);
              submission.setSubject(subject);
              // todo FINISH!!!!
          } else {
              EmployeeEntity person = FacadeRepository.getInstance().getEmployee(IDNum.getText());
              LocationNameEntity location =
                      FacadeRepository.getInstance().getLocation(locationBox.getText());

              urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
              category = IssueCategory.valueOf(categoryBox.getValue().toUpperCase());

              SanitationRequestEntity submission =
                      new SanitationRequestEntity(
                              nameBox.getText(),
                              person,
                              location,
                              descBox.getText(),
                              urgent,
                              ServiceRequestEntity.RequestType.SANITATION,
                              Status.NEW,
                              "Unassigned",
                              category);
              FacadeRepository.getInstance().addSanitationRequest(submission);
          }

          newEdit.setNeedEdits(false);
          switchToConfirmationScene(event);
      }
     */
  }

  @FXML
  void clearForm() {
    /*
      super.clearForm();
      subjectBox.clear();
      disabilityDescBox.clear();
      accommodationBox.clear();
     */
  }
}
