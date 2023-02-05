package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.databases.Employee;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

public class LoginController {

  @FXML MFXTextField usernameTextField;
  @FXML MFXPasswordField passwordTextField;
  @FXML Text incorrectNotification;
  Employee employee = new Employee();

  @FXML
  public void initialize() {
    incorrectNotification.setVisible(false);
  }

  @FXML
  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close(); // MAY NOT FUCKING WORK
    }
  }

  @FXML
  public void login(ActionEvent event) throws SQLException {

    ArrayList<String> info =
        employee.checkPass(usernameTextField.getText(), passwordTextField.getText());

    if (info.get(0).equals("")) {
      incorrectNotification.setVisible(true);
    } else {
      IdNumberHolder holder = IdNumberHolder.getInstance();
      holder.setId(info.get(0));
      holder.setUsername(usernameTextField.getText());
      holder.setPassword(passwordTextField.getText());
      holder.setJob(info.get(1));
      Navigation.navigate(Screen.HOME);
    }
  }
}
