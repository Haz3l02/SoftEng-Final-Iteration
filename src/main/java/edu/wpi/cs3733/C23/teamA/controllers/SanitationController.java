package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.SanitationRequestImpl;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SanitationController extends ServiceRequestController {
  private IssueCategory category;
  SanitationRequestImpl sanI = new SanitationRequestImpl();
  LocationNameImpl locationI = new LocationNameImpl();
  EmployeeImpl employeeI = new EmployeeImpl();

  @FXML private MFXComboBox<String> categoryBox;

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
      SanitationRequestImpl sanI = new SanitationRequestImpl();
      SanitationRequestEntity editRequest = sanI.get(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      categoryBox.setText(editRequest.getCategory().getIssue());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
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
        // something that submits it

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = IssueCategory.valueOf(categoryBox.getValue().toUpperCase());

        SanitationRequestEntity submission = sanI.get(newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = locationI.get(locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setCategory(category);
      } else {
        EmployeeEntity person = employeeI.get(IDNum.getText());
        // IDNum.getText()
        LocationNameEntity location = locationI.get(locationBox.getText());

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
                Status.BLANK,
                "Unassigned",
                category);
        sanI.add(submission);
        // submission.insert(); // *some db thing for getting the request in there*
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
