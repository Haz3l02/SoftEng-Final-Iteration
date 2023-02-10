package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.AApp;
import edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass;
import edu.wpi.cs3733.C23.teamA.hibernateDB.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.hibernate.Session;

public class LoginController {

  @FXML MFXTextField usernameTextField;
  @FXML MFXPasswordField passwordTextField;
  @FXML Text incorrectNotification;
  @FXML private GesturePane pane;
  EmployeeEntity employee = new EmployeeEntity();

  //  File file = new File(pathName);
  //  Image image = new Image(file.toURI().toString());
  //    nodeMapImage.setImage(image);
  @FXML
  public void initialize() {
    Node node = new ImageView(AApp.class.getResource("BackDrop.jpg").toExternalForm());
    System.out.println(node);
    this.pane.setContent(node);

    this.pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    //    incorrectNotification.setVisible(false);
  }

  @FXML
  public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("Are you sure you want to leave the app?");

    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.close(); // MAY NOT FUCKING WORK
    }
  }

  @FXML
  public void login(ActionEvent event) throws SQLException {

    Session session = ADBSingletonClass.getSessionFactory().openSession();
    // Transaction tx = session.beginTransaction();

    ArrayList<String> info =
        EmployeeEntity.checkPass(usernameTextField.getText(), passwordTextField.getText(), session);

    if (info.get(0).equals("")) {
      incorrectNotification.setVisible(true);
    } else {
      IdNumberHolder holder = IdNumberHolder.getInstance();
      holder.setId(info.get(0));
      holder.setUsername(usernameTextField.getText());
      holder.setPassword(passwordTextField.getText());
      holder.setJob(info.get(1));
      holder.setName(info.get(2));
      // tx.commit();
      session.close();
      Navigation.navigate(Screen.HOME);
    }
    // tx.commit();
    session.close();
  }
}
