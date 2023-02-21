package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SanitationController extends ServiceRequestController {
  private IssueCategory category;

  @FXML private MFXComboBox<String> categoryBox;
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;

  @FXML
  public void initialize() throws SQLException {
    super.initialize();

    if (categoryBox
        != null) { // this is here because SubmissionConfirmation page reuses this controller
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
      ObservableList<String> categories =
          FXCollections.observableArrayList(IssueCategory.issueList());
      categoryBox.setItems(categories);
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Sanitation")) {
      SanitationRequestEntity editRequest =
          FacadeRepository.getInstance().getSanitationRequest(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getHospitalid());
      categoryBox.setText(editRequest.getCategory().getIssue());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      accept.setDisable(true);
      accept.setVisible(false);
      clear.setDisable(false);
      clear.setVisible(true);
      submit.setDisable(false);
      submit.setVisible(true);
      reject.setDisable(true);
      reject.setVisible(false);

    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Sanitation")) {
      SanitationRequestEntity editRequest =
          FacadeRepository.getInstance().getSanitationRequest(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getHospitalid());
      categoryBox.setText(editRequest.getCategory().getIssue());
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
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {

    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || categoryBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = IssueCategory.valueOf(categoryBox.getValue().toUpperCase());

        SanitationRequestEntity submission =
            FacadeRepository.getInstance().getSanitationRequest(newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setCategory(category);
      } else {
        System.out.println("FUCKKKK" + employeeID);
        EmployeeEntity person = FacadeRepository.getInstance().getEmployee(employeeID);
        System.out.println("FUCKKKK YOU " + person.getName());

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
                null,
                category);
        FacadeRepository.getInstance().addSanitationRequest(submission);
      }

      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    categoryBox.clear();
  }
}
