package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

// import org.controlsfx.control.PopOver;

public abstract class ServiceRequestController {
  // protected HashMap<String, SanitationRequest> requests = new HashMap<String,
  // SanitationRequest>();

  @FXML protected MFXTextField nameBox;
  @FXML protected MFXTextField IDNum;
  @FXML protected MFXTextField locBox;
  @FXML protected MFXTextField descBox;
  @FXML protected MFXComboBox<String> urgencyBox;
  @FXML protected MFXFilterComboBox<String> locationBox;
  @FXML protected Text reminder;
  @FXML protected StackPane reminderPane;

  // for the timer
  public volatile boolean stop = false;

  // for the timer
  public volatile boolean stop = false;

  @FXML MFXButton backButton;
  private PopOver popup;

  @FXML
  public void initialize() throws SQLException, InterruptedException {
    // This statement blocks Pathfinding from being opened... is it important?
    // backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    popup = new PopOver(loader.load());
    popup.show(((Node) event.getSource()).getScene().getWindow());
    popup.detach();
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  void clearForm() {
    nameBox.clear();
    IDNum.clear();
    locBox.clear();
    descBox.clear();
    urgencyBox.clear();
  }

  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");
    stop = true;
    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.navigate(Screen.LOGIN);
      // Navigation.close(); // MAY NOT FUCKING WORK
    }
  }
}
