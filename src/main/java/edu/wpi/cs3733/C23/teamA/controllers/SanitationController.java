package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.SanitationRequest;
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

  @FXML private MFXComboBox<String> categoryBox;

  @FXML
  public void initialize() {
    if (categoryBox
        != null) { // this is here because SubmissionConfirmation page reuses this controller
      ObservableList<String> categories =
          FXCollections.observableArrayList("Standard", "Biohazard", "Wong");
      ObservableList<String> urgencies =
          FXCollections.observableArrayList("Low", "Medium", "High", "Extremely Urgent");

      categoryBox.setItems(categories);
      urgencyBox.setItems(urgencies);
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
        || locBox.getText().equals("")
        || descBox.getText().equals("")
        || categoryBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      //      requests.put(
      //          IDNum.getText(),
      //          new SanitationRequest(
      //              nameBox.getText(),
      //              IDNum.getText(),
      //              locBox.getText(),
      //              descBox.getText(),
      //              categoryBox.getValue(),
      //              urgencyBox.getValue(),
      //              "Sanitation Request"));

      SanitationRequest submission =
          new SanitationRequest(
              nameBox.getText(),
              IDNum.getText(),
              locBox.getText(),
              descBox.getText(),
              categoryBox.getValue(),
              urgencyBox.getValue(),
              "Sanitation Request");

      System.out.println(submission.getIdNum());
      System.out.println(submission.getName());

      submission.insert();

      // *some db thing for getting the request in there*
      System.out.println("this submits data"); // to be removed later
      // System.out.println(requests.get(IDNum.getText()).getName());

      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    categoryBox.clear();
  }
}
