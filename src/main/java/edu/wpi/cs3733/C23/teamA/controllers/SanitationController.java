package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.SanitationRequest;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SanitationController extends ServiceRequestController {

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

      SanitationRequest submission =
          new SanitationRequest(
              nameBox.getText(),
              IDNum.getText(),
              locationBox.getValue(),
              descBox.getText(),
              categoryBox.getValue(),
              urgencyBox.getValue(),
              "Sanitation Request",
              "Blank",
              "Unassigned");

      submission.insert(); // Adding to the database
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    categoryBox.clear();
  }
}
