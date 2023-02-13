package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.SecurityRequestImpl;
import edu.wpi.cs3733.C23.teamA.enums.RequestCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SecurityController extends ServiceRequestController {

  @FXML private MFXTextField phone;
  @FXML private MFXComboBox<String> requestsBox;
  private RequestCategory assistance;
  SecurityRequestImpl securityImpl = new SecurityRequestImpl();
  SecurityRequestEntity submission = new SecurityRequestEntity();
  EmployeeImpl employee = new EmployeeImpl();
  LocationNameImpl location = new LocationNameImpl();

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList(RequestCategory.categoryList());
      requestsBox.setItems(requests);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Security")) {

      SecurityRequestEntity editRequest = securityImpl.get(newEdit.getRequestID());

      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      requestsBox.setText(editRequest.getRequestType().requestType);
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      phone.setText(editRequest.getSecphone());
      submission.setEmployeeAssigned(editRequest.getEmployeeAssigned());
      submission.setStatus(editRequest.getStatus());
      submission.setDate(editRequest.getDate());
      submission.setRequestType(
          ServiceRequestEntity.RequestType.valueOf(newEdit.getRequestType().toUpperCase()));
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) {
    Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  void submitRequest(ActionEvent event) {
    if (nameBox.getText().equals("")
        || phone.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || requestsBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        LocationNameEntity loc = location.get(locationBox.getValue());
        EmployeeEntity person = employee.get(IDNum.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue().toUpperCase());

        submission.setEmployee(person);
        submission.setName(nameBox.getText());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setAssistance(assistance);
        submission.setSecphone(phone.getText());
        securityImpl.update(newEdit.getRequestID(), submission);
      } else {

        EmployeeEntity person = employee.get(IDNum.getText());
        LocationNameEntity loc = location.get(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue().toUpperCase());

        SecurityRequestEntity submission =
            new SecurityRequestEntity(
                nameBox.getText(),
                person,
                loc,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.SECURITY,
                Status.BLANK,
                "Unassigned",
                assistance,
                phone.getText());
        securityImpl.add(submission);
      }
      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    phone.clear();
    requestsBox.clear();
  }
}
