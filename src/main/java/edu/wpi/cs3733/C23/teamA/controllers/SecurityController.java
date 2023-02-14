package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.*;
import edu.wpi.cs3733.C23.teamA.enums.RequestCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
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
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;
  private RequestCategory assistance;
  private SecurityRequestImpl secI;

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    reject.setDisable(true);
    reject.setVisible(false);
    accept.setDisable(true);
    accept.setVisible(false);
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList(RequestCategory.categoryList());
      requestsBox.setItems(requests);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Security")) {
      SecurityRequestEntity editRequest = secI.get(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      requestsBox.setText(editRequest.getRequestType().requestType);
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      phone.setText(editRequest.getSecphone());
      accept.setDisable(true);
      accept.setVisible(false);
      clear.setDisable(false);
      clear.setVisible(true);
      submit.setDisable(false);
      submit.setVisible(true);
      reject.setDisable(true);
      reject.setVisible(false);
    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Security")) {
      SecurityRequestEntity editRequest = secI.get(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      requestsBox.setText(editRequest.getRequestType().requestType);
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
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  void submitRequest(ActionEvent event) {
    LocationNameImpl locationI = new LocationNameImpl();
    EmployeeImpl employeeI = new EmployeeImpl();
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
        // something that submits it
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue().toUpperCase());

        SecurityRequestEntity submission = secI.get(newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = locationI.get(locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setAssistance(assistance);
        submission.setSecphone(phone.getText());

        secI.update(submission.getRequestid(), submission);

      } else {
        EmployeeEntity person = employeeI.get(IDNum.getText());
        LocationNameEntity location = locationI.get(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue());

        SecurityRequestEntity submission =
            new SecurityRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.SECURITY,
                Status.NEW,
                "Unassigned",
                assistance,
                phone.getText());
        secI.add(submission);
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
