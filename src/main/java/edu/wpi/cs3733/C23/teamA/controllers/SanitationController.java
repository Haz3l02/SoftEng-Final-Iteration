package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SanitationController extends ServiceRequestController {

  ServiceRequestEntity.Urgency urgent;
  SanitationRequestEntity.Category category;

  @FXML private MFXComboBox<String> categoryBox;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String name = holder.getName();
    String id = holder.getId();

    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }
    if (categoryBox
        != null) { // this is here because SubmissionConfirmation page reuses this controller
      ObservableList<String> categories =
          FXCollections.observableArrayList(
              IssueCategory.STANDARD.getIssue(),
              IssueCategory.BIOHAZARD.getIssue(),
              IssueCategory.WONG.getIssue());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(
              UrgencyLevel.LOW.getUrgency(),
              UrgencyLevel.MEDIUM.getUrgency(),
              UrgencyLevel.HIGH.getUrgency(),
              UrgencyLevel.EXTREMELY_URGENT.getUrgency());

      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();

      List<LocationNameEntity> temp = new ArrayList<LocationNameEntity>();
      temp = getAllRecords(LocationNameEntity.class, session);

      // ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }

      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);

      nameBox.setText(name);
      IDNum.setText(id);

      categoryBox.setItems(categories);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("SANITATION")) {

      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      SanitationRequestEntity editRequest =
          session.get(SanitationRequestEntity.class, newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      categoryBox.setText(editRequest.getCategory().category);
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().urgency);
      descBox.setText(editRequest.getDescription());
      tx.commit();
      session.close();
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION_CONFIRMATION);
  }

  @FXML
  public void switchToSanitationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION);
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
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServiceRequestEntity.Urgency.LOW;
            break;
          case "Medium":
            urgent = ServiceRequestEntity.Urgency.MEDIUM;
            break;
          case "High":
            urgent = ServiceRequestEntity.Urgency.HIGH;
            break;
          case "Extremely Urgent":
            urgent = ServiceRequestEntity.Urgency.EXTREMELY_URGENT;
            break;
        }
        switch (categoryBox.getValue()) {
          case "Standard":
            category = SanitationRequestEntity.Category.STANDARD;
            break;
          case "Biohazard":
            category = SanitationRequestEntity.Category.BIOHAZARD;
            break;
          case "Wong":
            category = SanitationRequestEntity.Category.WONG;
            break;
        }

        SanitationRequestEntity submission =
            session.get(SanitationRequestEntity.class, newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = session.get(LocationNameEntity.class, locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setCategory(category);

        tx.commit();
        session.close();
      } else {

        Session session = ADBSingletonClass.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        EmployeeEntity person = session.get(EmployeeEntity.class, IDNum.getText());
        // IDNum.getText()
        LocationNameEntity location = session.get(LocationNameEntity.class, locationBox.getText());
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServiceRequestEntity.Urgency.LOW;
            break;
          case "Medium":
            urgent = ServiceRequestEntity.Urgency.MEDIUM;
            break;
          case "High":
            urgent = ServiceRequestEntity.Urgency.HIGH;
            break;
          case "Extremely Urgent":
            urgent = ServiceRequestEntity.Urgency.EXTREMELY_URGENT;
            break;
        }
        switch (categoryBox.getValue()) {
          case "Standard":
            category = SanitationRequestEntity.Category.STANDARD;
            break;
          case "Biohazard":
            category = SanitationRequestEntity.Category.BIOHAZARD;
            break;
          case "Wong":
            category = SanitationRequestEntity.Category.WONG;
            break;
        }

        SanitationRequestEntity submission =
            new SanitationRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.SANITATION,
                ServiceRequestEntity.Status.BLANK,
                "Unassigned",
                category);
        session.persist(submission);
        tx.commit();
        session.close();
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
