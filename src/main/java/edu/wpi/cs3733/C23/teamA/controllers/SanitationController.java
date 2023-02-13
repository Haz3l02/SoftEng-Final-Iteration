package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.SanitationRequestImpl;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SanitationController extends ServiceRequestController {
  IssueCategory category;

  @FXML private MFXComboBox<String> categoryBox;
  SanitationRequestImpl sanitationImpl = new SanitationRequestImpl();
  SanitationRequestEntity submission = new SanitationRequestEntity();
  EmployeeImpl employee = new EmployeeImpl();
  LocationNameImpl location = new LocationNameImpl();

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    if (categoryBox
        != null) { // this is here because SubmissionConfirmation page reuses this controller
      ObservableList<String> categories =
          FXCollections.observableArrayList(IssueCategory.issueList());
      categoryBox.setItems(categories);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Sanitation")) {

      SanitationRequestEntity editRequest = sanitationImpl.get(newEdit.getRequestID());

      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      categoryBox.setText(editRequest.getCategory().getIssue());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      submission.setEmployeeAssigned(editRequest.getEmployeeAssigned());
      submission.setStatus(editRequest.getStatus());
      submission.setDate(editRequest.getDate());
      submission.setRequestType(
          ServiceRequestEntity.RequestType.valueOf(newEdit.getRequestType().toUpperCase()));
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION_CONFIRMATION);
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
        LocationNameEntity loc = location.get(locationBox.getValue());
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = IssueCategory.valueOf(categoryBox.getValue().toUpperCase());
        submission.setName(nameBox.getText());
        submission.setName(nameBox.getText());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setCategory(category);
        sanitationImpl.update(newEdit.getRequestID(), submission);
      } else {
        EmployeeEntity person = employee.get(IDNum.getText());
        LocationNameEntity loc = location.get(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = IssueCategory.valueOf(categoryBox.getValue().toUpperCase());

        SanitationRequestEntity submission =
            new SanitationRequestEntity(
                nameBox.getText(),
                person,
                loc,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.SANITATION,
                Status.BLANK,
                "Unassigned",
                category);
        sanitationImpl.add(submission);
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
