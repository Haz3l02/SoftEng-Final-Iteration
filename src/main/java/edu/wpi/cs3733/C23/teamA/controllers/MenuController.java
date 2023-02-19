package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

// import org.controlsfx.control.PopOver;

public class MenuController extends NavigationController {

  @FXML protected MFXTextField nameBox;
  @FXML protected MFXTextField IDNum;
  @FXML protected MFXComboBox locBox;
  @FXML protected MFXTextField descBox;
  @FXML protected MFXComboBox<String> urgencyBox;
  @FXML protected MFXComboBox locationBox;
  @FXML protected Text reminder;
  @FXML protected StackPane reminderPane;
  @FXML protected ImageView home;
  @FXML protected ImageView home2;
  @FXML protected ImageView help;
  @FXML protected ImageView logout;
  @FXML ComboBox<String> serviceRequest = new ComboBox<>();
  @FXML ComboBox<String> admin = new ComboBox<>();

  // for the timer
  //  public volatile boolean stop = false;

  @FXML MFXButton backButton;
  private static PopOver popup;

  @FXML
  public void initialize() throws SQLException, IOException, InterruptedException {
    //    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    //    popup = new PopOver(loader.load());

    // testButton.setGraphic(home);
    home.setOnMouseClicked(
        (MouseEvent e) -> {
          switchToHomeScene();
        });

    home2.setOnMouseClicked(
        (MouseEvent e) -> {
          switchToHomeScene();
        });
    help.setOnMouseClicked(
        (MouseEvent e) -> {
          try {
            switchToHelpScene(e);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
    logout.setOnMouseClicked(
        (MouseEvent e) -> {
          logout();
        });
    admin
        .getItems()
        .addAll("Map Editor", "Access Employee Records", "Department Moves", "Create Nodes");
    serviceRequest
        .getItems()
        .addAll(
            "Sanitation Request",
            "Security Request",
            "Computer Request",
            "Patient Transportation",
            "Audio/Visual Request",
            "Accessibility Request",
            "View Previous Submissions");

    serviceRequest
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              switch (newValue) {
                case "Computer Request":
                  switchToComputer();
                  break;
                case "Security Request":
                  switchToSecurity();
                  break;
                case "Sanitation Request":
                  switchToSanitation();
                  break;
                case "Accessibility Request":
                  // switchToAccessibility();
                  System.out.println("Accessibility");
                  break;
                case "Patient Transportation":
                  switchToPatientTransport();
                  break;
                case "View Past Submissions":
                  switchToServiceRequestStatus();
                  break;
                case "Audio/Visual Request":
                  // switchToAudioVisual();
                  System.out.println("Audio/Visual");
                  break;
                default:
                  break;
              }
              System.out.println(newValue);
            });

    System.out.println("HELOPOOOOOOS");
    admin
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              switch (newValue) {
                case "Map Editor":
                  System.out.println("HELOPOOOOOOSKJDHBvafcjhadsvbfhjwf");
                  switchToMapScene();
                  break;
                case "Access Employee Records":
                  switchToEmployeeScene();
                  break;
                case "Sanitation Request":
                  switchToSanitation();
                case "Department Moves":
                  switchToMoveScene();
                  break;
                case "Create Nodes":
                  switchToNodeScene();
                  break;
                default:
                  break;
              }
            });

    //    if (admin.getValue() != null) {
    //      System.out.println("HELO");
    //
    //      switch (admin.getValue()) {
    //        case "Map Editor":
    //          System.out.println("HELOPOOOOOOSKJDHBvafcjhadsvbfhjwf");
    //          switchToMapScene();
    //          break;
    //        case "Access Employee Records":
    //          switchToEmployeeScene();
    //          break;
    //        case "Sanitation Request":
    //          switchToSanitation();
    //        case "Department Moves":
    //          switchToMoveScene();
    //          break;
    //        case "Create Nodes":
    //          switchToNodeScene();
    //          break;
    //        default:
    //          break;
    //      }
    //    }
  }

  @FXML
  public void switchToHomeScene() {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    if (holder.getJob().equalsIgnoreCase("Maintenance")) {
      Navigation.navigateHome(Screen.HOME_MAINTENANCE);
    } else if (holder.getJob().equalsIgnoreCase("Admin")) {
      Navigation.navigateHome(Screen.HOME_ADMIN);
    } else {
      Navigation.navigateHome(Screen.HOME_EMPLOYEE);
    }
  }

  /**
   * If called from a button without the fxid:backButton then it shows the help popup If called from
   * a button with that fxid, it hides the help popup
   *
   * @param event
   * @throws IOException
   */
  @FXML
  public void switchToHelpScene(MouseEvent event) throws IOException {
    if (!event.getSource().equals(backButton)) {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(backButton)) {
      popup.hide();
    }
  }

  public void logout() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to log out!");
    alert.setContentText("Unsubmitted information in the form will be lost!");
    stop = true;
    if (alert.showAndWait().get() == ButtonType.OK) {
      System.out.println("You have successfully logged out!");
      Navigation.navigateHome(Screen.LOGIN);
    }
  }
}
