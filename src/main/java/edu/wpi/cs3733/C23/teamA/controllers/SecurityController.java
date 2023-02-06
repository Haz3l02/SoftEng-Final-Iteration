package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.enums.RequestCategory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.SecurityRequest;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

public class SecurityController extends ServiceRequestController {

  @FXML private MFXTextField phone;
  @FXML private MFXComboBox<String> requestsBox;
  ServicerequestEntity.Urgency urgent;
  SecurityrequestEntity.Assistance assistance;

  @FXML
  public void initialize() throws SQLException {
    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList(
              RequestCategory.HARASSMENT.getRequest(),
              RequestCategory.SECURITY_ESCORT.getRequest(),
              RequestCategory.POTENTIAL_THREAT.getRequest());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(
              UrgencyLevel.LOW.getUrgency(),
              UrgencyLevel.MEDIUM.getUrgency(),
              UrgencyLevel.HIGH.getUrgency(),
              UrgencyLevel.EXTREMELY_URGENT.getUrgency());

      ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (Move move : moves) {
        locations.add(move.getLongName());
      }

      requestsBox.setItems(requests);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void switchToSecurityScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException, SQLException {
    if (nameBox.getText().equals("")
        || phone.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getText().equals("")
        || descBox.getText().equals("")
        || requestsBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
        Session session = ADBSingletonClass.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        EmployeeEntity person = session.get(EmployeeEntity.class, "123");
        // IDNum.getText()
        LocationnameEntity location = session.get(LocationnameEntity.class, locationBox.getText());
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServicerequestEntity.Urgency.LOW;
          case "Medium":
            urgent = ServicerequestEntity.Urgency.MEDIUM;
          case "High":
            urgent = ServicerequestEntity.Urgency.HIGH;
          case "Extremely Urgent":
            urgent = ServicerequestEntity.Urgency.EXTREMELY_URGENT;
        }
        switch (requestsBox.getValue()) {
          case "Harassment":
            assistance = SecurityrequestEntity.Assistance.HARASSMENT;
          case "Security Escort":
            assistance = SecurityrequestEntity.Assistance.SECURITY_ESCORT;
          case "Potential Threat":
            assistance = SecurityrequestEntity.Assistance.POTENTIAL_THREAT;
        }
      SecurityrequestEntity submission =
                new SecurityrequestEntity(
                        nameBox.getText(),
                        person,
                        location,
                        descBox.getText(),
                        urgent,
                        ServicerequestEntity.RequestType.SECURITY,
                        ServicerequestEntity.Status.BLANK,
                        "Unassigned",
                        assistance,phone.getText());
        session.persist(submission);
        tx.commit();
        session.close();
        // submission.insert(); // *some db thing for getting the request in there*
      }

      switchToConfirmationScene(event);
    }


  @FXML
  void clearForm() {
    super.clearForm();
    phone.clear();
    requestsBox.clear();
  }
}
