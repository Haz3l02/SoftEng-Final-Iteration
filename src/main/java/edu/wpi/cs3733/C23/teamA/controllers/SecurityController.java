package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
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

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList(RequestCategory.categoryList());
      requestsBox.setItems(requests);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Security")) {
      SecurityRequestImpl secI = new SecurityRequestImpl();
      SecurityRequestEntity editRequest = secI.get(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      requestsBox.setText(editRequest.getRequestType().requestType);
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      phone.setText(editRequest.getSecphone());
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
    SecurityRequestImpl secI = new SecurityRequestImpl();
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
                Status.BLANK,
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
