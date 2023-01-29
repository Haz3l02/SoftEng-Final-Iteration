package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.SanitationEntity;
import edu.wpi.cs3733.C23.teamA.ServiceRequestEntities;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

public class SanitationController {
  private HashMap<String, ServiceRequestEntities> requests =
      new HashMap<String, ServiceRequestEntities>();

  @FXML private MFXTextField nameBox;
  @FXML private MFXTextField idBox;
  @FXML private MFXTextField locBox;
  @FXML private MFXTextField descBox;
  @FXML private MFXComboBox<String> categoryBox;
  @FXML private MFXComboBox<String> urgencyBox;
  @FXML private Text reminder;
  // private PopOver popup;

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
  public void switchToHelpScene(ActionEvent event) throws IOException {
    //    FXMLLoader loader =
    //            new FXMLLoader(Main.class.getResource("views/ServiceReqOneHelpScreenFXML.fxml"));
    //    popup = new PopOver(loader.load());
    //    popup.show(((Node)
    // event.getSource()).getScene().getWindow());Navigation.navigate(Screen.HELP);
  }

  @FXML
  public void switchToSanitationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION);
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
  void submitRequest(ActionEvent event) throws IOException {

    if (nameBox.getText().equals("")
        || idBox.getText().equals("")
        || locBox.getText().equals("")
        || descBox.getText().equals("")
        || categoryBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      requests.put(
          idBox.getText(),
          new SanitationEntity(
              nameBox.getText(),
              idBox.getText(),
              locBox.getText(),
              descBox.getText(),
              categoryBox.getValue(),
              urgencyBox.getValue()));

      // *some db thing for getting the request in there*
      System.out.println("this submits data"); // to be removed later

      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    nameBox.clear();
    idBox.clear();
    locBox.clear();
    descBox.clear();
    categoryBox.clear();
    urgencyBox.clear();
  }

  @FXML
  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close();
    }
  }
}
