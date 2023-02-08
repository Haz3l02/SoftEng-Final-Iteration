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
  @FXML protected MFXComboBox locBox;
  @FXML protected MFXTextField descBox;
  @FXML protected MFXComboBox<String> urgencyBox;
  @FXML protected MFXFilterComboBox<String> locationBox;
  @FXML protected Text reminder;
  @FXML protected StackPane reminderPane;

  @FXML MFXButton backButton;
  private static PopOver popup;

  @FXML
  public void initialize() throws SQLException, IOException {
    // This statement blocks Pathfinding from being opened... is it important?
    // backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    popup = new PopOver(loader.load());
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  /**
   * If called from a button without the fxid:backButton then it shows the help popup If called from
   * a button with that fxid, it hides the help popup
   *
   * @param event
   * @throws IOException
   */
  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
<<<<<<< HEAD
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    popup = new PopOver(loader.load());
    popup.show(((Node) event.getSource()).getScene().getWindow());
    popup.detach();
=======

    if (!event.getSource().equals(backButton)) {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(backButton)) {
      popup.hide();
    }
>>>>>>> 961e483c4bc70f6c6c0c7d4e10d00d74abc8a8bc
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) throws IOException {
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

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.navigate(Screen.LOGIN);
      // Navigation.close(); // MAY NOT FUCKING WORK
    }
  }
}
