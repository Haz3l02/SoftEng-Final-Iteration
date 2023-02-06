package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SanitationController extends ServiceRequestController {

  ServicerequestEntity.Urgency urgent;
  SanitationrequestEntity.Category category;

  @FXML private MFXComboBox<String> categoryBox;

  @FXML
  public void initialize() throws SQLException {
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

      ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (Move move : moves) {
        locations.add(move.getLongName());
      }

      categoryBox.setItems(categories);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
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
        || locationBox.getValue().equals("")
        || descBox.getText().equals("")
        || categoryBox.getValue() == null
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
          urgent = ServicerequestEntity.Urgency.LOW;
        case "High":
          urgent = ServicerequestEntity.Urgency.LOW;
        case "Extremely Urgent":
          urgent = ServicerequestEntity.Urgency.LOW;
      }
      switch (categoryBox.getValue()) {
        case "Standard":
          category = SanitationrequestEntity.Category.STANDARD;
        case "Bio-Hazard":
          category = SanitationrequestEntity.Category.BIOHAZARD;
        case "Wong":
          category = SanitationrequestEntity.Category.WONG;
      }
      SanitationrequestEntity submission =
          new SanitationrequestEntity(
              nameBox.getText(),
              person,
              location,
              descBox.getText(),
              urgent,
              ServicerequestEntity.RequestType.SANITATION,
              ServicerequestEntity.Status.BLANK,
              "Unassigned",
              category);
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
    categoryBox.clear();
  }
}
