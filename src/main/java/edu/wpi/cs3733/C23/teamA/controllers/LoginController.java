package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
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

  @FXML MFXButton loginButton;
  EmployeeEntity employee = new EmployeeEntity();

  @FXML
  public void initialize() {
    incorrectNotification.setVisible(false);
  }

  @FXML
  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("Are you sure you want to leave the app?");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close();
    }
  }

  @FXML
  public void login(ActionEvent event) throws SQLException, InterruptedException {
    EmployeeImpl employee1 = new EmployeeImpl();
    // Transaction tx = session.beginTransaction();

    ArrayList<String> info =
        employee1.checkPass(usernameTextField.getText(), passwordTextField.getText());
    // employee1.closeSession();
    if (info.get(0).equals("")) {
      incorrectNotification.setVisible(true);
      usernameTextField.clear();
      passwordTextField.clear();
    } else {
      IdNumberHolder holder = IdNumberHolder.getInstance();
      holder.setId(info.get(0));
      holder.setUsername(usernameTextField.getText());
      holder.setPassword(passwordTextField.getText());
      holder.setJob(info.get(1));
      holder.setName(info.get(2));
      if (holder.getJob().equalsIgnoreCase("Maintenance")) {
        Navigation.navigateHome(Screen.HOME_MAINTENANCE);
      } else if (holder.getJob().equalsIgnoreCase("Admin")) {
        Navigation.navigateHome(Screen.HOME_ADMIN);
      } else {
        Navigation.navigateHome(Screen.HOME_EMPLOYEE);
      }
    }
  }
}
