package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.SanitationRequestImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ServiceRequestImpl;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
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
    reject.setDisable(true);
    reject.setVisible(false);
    accept.setDisable(true);
    accept.setVisible(false);
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
      // sanI.closeSession();
      accept.setDisable(true);
      accept.setVisible(false);
      clear.setDisable(false);
      clear.setVisible(true);
      submit.setDisable(false);
      submit.setVisible(true);
      reject.setDisable(true);
      reject.setVisible(false);

    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Sanitation")) {
      SanitationRequestImpl sanI = new SanitationRequestImpl();
      SanitationRequestEntity editRequest = sanI.get(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
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
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION_CONFIRMATION);
  }

  @FXML
  void acceptRequest(ActionEvent event) {
    ServiceRequestImpl sri = new ServiceRequestImpl();
    sri.updateStatus(Status.PROCESSING, acceptTheForm.getRequestID());
  }

  @FXML
  public void rejectRequest(ActionEvent event) {
    ServiceRequestImpl sri = new ServiceRequestImpl();
    sri.updateEmployee("Unassigned", acceptTheForm.getRequestID());
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {
    SanitationRequestImpl sanI = new SanitationRequestImpl();
    LocationNameImpl locationI = new LocationNameImpl();
    EmployeeImpl employeeI = new EmployeeImpl();
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
      // sanI.closeSession();
      // employeeI.closeSession();
      // locationI.closeSession();
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
