package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.AApp;
import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
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
  @FXML MFXButton myAssignments;
  //  public volatile boolean stop = false;

  @FXML MFXButton backButton;
  @FXML MFXButton navigation;

  private static PopOver popup;

  private final Image homeYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/home_yellow.png"));
  private final Image homeWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/home_blue.png"));
  private final Image helpYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/help_yellow.png"));
  private final Image helpWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/help_blue.png"));
  private final Image logoutYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/exit_yellow.png"));
  private final Image logoutWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/exit_blue.png"));

  @FXML
  public void initialize() throws SQLException, IOException, InterruptedException {
    IdNumberHolder userInfo = new IdNumberHolder();
    userInfo = IdNumberHolder.getInstance();

    //    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    //    popup = new PopOver(loader.load());

    // testButton.setGraphic(home);
    if (userInfo.getJob().equalsIgnoreCase("admin")) admin.setVisible(true);
    if (userInfo.getJob().equalsIgnoreCase("maintenance")) {
      serviceRequest.getItems().add("My Assignments");
      myAssignments.setVisible(true);
      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();

      requests =
          FacadeRepository.getInstance().getServiceRequestByAssigned(userInfo.getEmployeeID());
      if (requests.size() == 0) {
        myAssignments.setDisable(true);

      } else {
        myAssignments.setDisable(false);
      }
    }

    home.setOnMouseClicked(
        (MouseEvent e) -> {
          switchToHomeScene();
        });
    home.setOnMouseEntered(
        event -> {
          home.setImage(this.homeYellow);
        });

    home.setOnMouseExited(
        event -> {
          home.setImage(this.homeWhite);
        });
    help.setOnMouseEntered(
        event -> {
          help.setImage(this.helpYellow);
        });

    help.setOnMouseExited(
        event -> {
          help.setImage(this.helpWhite);
        });
    logout.setOnMouseEntered(
        event -> {
          logout.setImage(this.logoutYellow);
        });

    logout.setOnMouseExited(
        event -> {
          logout.setImage(this.logoutWhite);
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
            "View Submissions",
            "Sanitation Request",
            "Security Request",
            "Computer Request",
            "Patient Transportation",
            "Audio/Visual Request",
            "Accessibility Request");

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
                  switchToAccessibility();
                  break;
                case "Patient Transportation":
                  switchToPatientTransport();
                  break;
                case "View Submissions":
                  switchToServiceRequestStatus();
                  break;
                case "Audio/Visual Request":
                  switchToAudioVisual();
                  break;
                case "My Assignments":
                  switchToServiceRequestStatus();
                  break;
                default:
                  break;
              }
            });

    admin
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              switch (newValue) {
                case "Map Editor":
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
  }

  @FXML
  public void switchToHomeScene() {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    if (holder.getJob().equalsIgnoreCase("Maintenance")) {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
    } else if (holder.getJob().equalsIgnoreCase("Admin")) {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
    } else {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
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
