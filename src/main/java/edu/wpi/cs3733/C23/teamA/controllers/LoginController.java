package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass;
import edu.wpi.cs3733.C23.teamA.hibernateDB.EmployeeEntity;
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
import org.hibernate.Session;

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

    Session session = ADBSingletonClass.getSessionFactory().openSession();

    ArrayList<String> info =
        EmployeeEntity.checkPass(usernameTextField.getText(), passwordTextField.getText(), session);

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
      session.close();
      Navigation.navigate(Screen.HOME);
    }
    session.close();
  }
}
