package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.AApp;
import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.MaintenanceAssignedAccepted;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

public class HomeController extends MenuController {

  @FXML private TableView<ServiceRequestEntity> adminTable;
  @FXML private TableView<ServiceRequestEntity> employeeTable;
  @FXML private TableView<ServiceRequestEntity> maintenanceTable;
  @FXML public TableColumn<ServiceRequestEntity, Integer> IDCol;
  @FXML public TableColumn<ServiceRequestEntity, String> requestTypeCol;
  @FXML public TableColumn<ServiceRequestEntity, String> locationCol;
  @FXML public TableColumn<ServiceRequestEntity, String> urgencyCol;
  @FXML public TableColumn<ServiceRequestEntity, Integer> IDCol1;
  @FXML public TableColumn<ServiceRequestEntity, String> requestTypeCol1;
  @FXML public TableColumn<ServiceRequestEntity, String> locationCol1;
  @FXML public TableColumn<ServiceRequestEntity, String> urgencyCol1;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> nameCol;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> assignedCol;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> acceptedCol;
  @FXML public StackPane mapImage;
  @FXML public Text findAPath;
  @FXML public ImageView about;
  @FXML public ImageView credits;
  @FXML public ImageView exit;
  @FXML public Label label1, label2, label3;
  private final Image creditsYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/credits_yellow.png"));
  private final Image creditsWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/credits.png"));
  private final Image aboutYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/about_yellow.png"));
  private final Image aboutWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/about.png"));
  private final Image logoutYellow =
      new Image(AApp.class.getResourceAsStream("assets/icons/exit_yellow.png"));
  private final Image logoutWhite =
      new Image(AApp.class.getResourceAsStream("assets/icons/exit_darkBlue.png"));
  @FXML public MFXTextField adminAnnouncementField;
  @FXML public MFXButton adminAnnouncementButton;
  @FXML public Text announcementText;

  @FXML private Label date = new Label("hello");
  @FXML private Label time = new Label("hello");
  @FXML private Label message = new Label("hello");
  @FXML private Label welcome = new Label("hello");
  @FXML private MFXButton myAssignments;
  private static PopOver popup;

  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();

  private ObservableList<MaintenanceAssignedAccepted> dbTableRowsModel2 =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() throws IOException, InterruptedException, SQLException {

    grabQuote();
    date();
    time();
    IdNumberHolder userInfo = new IdNumberHolder();
    userInfo = IdNumberHolder.getInstance();
    welcome.setText(userInfo.getName() + "!");
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
                case "Kiosk Creator":
                  switchToKioskSetupScene();
                  break;
                default:
                  break;
              }
            });
    mapImage.setOnMouseClicked(
        (MouseEvent e) -> {
          switchToPathfinding();
        });
    mapImage.setOnMouseEntered(
        (MouseEvent e) -> {
          findAPath.setVisible(true);
        });
    mapImage.setOnMouseExited(
        (MouseEvent e) -> {
          findAPath.setVisible(false);
        });
    about.setOnMouseEntered(
        (MouseEvent e) -> {
          about.setImage(this.aboutYellow);
        });
    about.setOnMouseExited(
        (MouseEvent e) -> {
          about.setImage(this.aboutWhite);
        });
    credits.setOnMouseEntered(
        (MouseEvent e) -> {
          credits.setImage(this.creditsYellow);
        });
    credits.setOnMouseExited(
        (MouseEvent e) -> {
          credits.setImage(this.creditsWhite);
        });
    exit.setOnMouseEntered(
        (MouseEvent e) -> {
          exit.setImage(this.logoutYellow);
        });
    exit.setOnMouseExited(
        (MouseEvent e) -> {
          exit.setImage(this.logoutWhite);
        });
    about.setOnMouseClicked(
        (MouseEvent e) -> {
          try {
            switchToAbout(e);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
    credits.setOnMouseClicked(
        (MouseEvent e) -> {
          try {
            switchToCredits(e);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
    exit.setOnMouseClicked(
        (MouseEvent e) -> {
          logout();
        });

    if (userInfo.getJob().equalsIgnoreCase("maintenance") && IDCol != null) {
      adminTable.setVisible(false);
      adminTable.setDisable(true);
      employeeTable.setVisible(false);
      employeeTable.setDisable(true);
      maintenanceTable.setVisible(true);
      maintenanceTable.setDisable(false);
      adminAnnouncementField.setDisable(true);
      adminAnnouncementField.setVisible(false);
      adminAnnouncementButton.setVisible(false);
      myAssignments.setVisible(true);

      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getLocation().getLongname()));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();

      requests =
          FacadeRepository.getInstance().getServiceRequestByAssigned(userInfo.getEmployeeID());

      int numDone = 0;
      int numAssigned = 0;
      int numAccepted = 0;

      for (ServiceRequestEntity request : requests) {
        if (request.getStatus().equals(Status.DONE)) {
          numDone++;
        } else if (request.getStatus().equals(Status.ASSIGNED)) {
          numAssigned++;
        } else if (request.getStatus().equals(Status.PROCESSING)) {
          numAccepted++;
        }
      }
      label1.setText("# of completed assignments: " + numDone);
      label2.setText("# of assigned assignments: " + numAssigned);
      label3.setText("# of accepted assignments: " + numAccepted);

      dbTableRowsModel.addAll(requests);
      maintenanceTable.setItems(dbTableRowsModel);
      System.out.println(requests.size());
      if (requests.size() == 0) {
        myAssignments.setDisable(true);

      } else {
        myAssignments.setDisable(false);
      }

    } else if (userInfo.getJob().equalsIgnoreCase("Admin") && adminTable != null) {
      adminTable.setVisible(true);
      adminTable.setDisable(false);
      employeeTable.setVisible(false);
      employeeTable.setDisable(true);
      maintenanceTable.setVisible(false);
      maintenanceTable.setDisable(true);

      IDCol1.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol1.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol1.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getLocation().getLongname()));
      urgencyCol1.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();
      requests = FacadeRepository.getInstance().getServiceRequestByUnassigned();

      label1.setText("# of unassigned requests: " + requests.size());
      label2.setVisible(false);
      label3.setVisible(false);

      dbTableRowsModel.addAll(requests);
      adminTable.setItems(dbTableRowsModel);

      myAssignments.setDisable(true); // its already hidden doesnt need to be disabled
      myAssignments.setVisible(false); // It is automatically not visible redundent
      admin.setDisable(false); // Redundent its never going to be disabled
      admin.setVisible(true);
      // NOT IN MAIN FOR SOME REASON NOT SURE IF WE WANT TO KEEP
      //      List<EmployeeEntity> maintenanceEmployees =
      //          FacadeRepository.getInstance().getEmployeeByJob("maintenance");
      //      List<MaintenanceAssignedAccepted> maa = new ArrayList<MaintenanceAssignedAccepted>();
      //      for (EmployeeEntity employee : maintenanceEmployees) {
      //        maa.add(new MaintenanceAssignedAccepted(employee.getName(),
      // employee.getEmployeeid()));
      //      }
      //      System.out.println(maa.size());
      //      dbTableRowsModel2.addAll(maa);
      //      for (int i = 0; i < maa.size(); i++) {
      //        System.out.println(maa.get(i).getName());
      //      }
      //      System.out.println(dbTableRowsModel2.size());
      //      employeeTable.setItems(dbTableRowsModel2);
      admin
          .getItems()
          .addAll(
              "Map Editor",
              "Access Employee Records",
              "Department Moves",
              "Create Nodes",
              "Kiosk Creator");

    } else {
      adminTable.setVisible(false);
      adminTable.setDisable(true);
      employeeTable.setVisible(true);
      employeeTable.setDisable(false);
      maintenanceTable.setVisible(false);
      maintenanceTable.setDisable(true);
      adminAnnouncementField.setDisable(true);
      adminAnnouncementField.setVisible(false);
      adminAnnouncementButton.setVisible(false);
    }
  }

  @FXML
  public void submitAnnouncement(ActionEvent event) {
    announcementText.setText(adminAnnouncementField.getText());
    adminAnnouncementField.clear();
  }

  @FXML
  public void switchToStatus(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }

  @FXML
  public void switchToCredits(MouseEvent event) throws IOException {
    if (!event.getSource().equals(backButton)) {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/CreditsFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(backButton)) {
      popup.hide();
    }
  }

  @FXML
  public void switchToAbout(MouseEvent event) throws IOException {
    if (!event.getSource().equals(backButton)) {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/AboutFXML.fxml"));
      popup = new PopOver(loader.load());
      popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    if (event.getSource().equals(backButton)) {
      popup.hide();
    }
  }

  @FXML
  public void switchToDatabase(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }

  @FXML
  public void switchToPathfinding(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigate(Screen.PATHFINDING);
  }

  @FXML
  public void date() throws InterruptedException {
    Thread thread =
        new Thread(
            () -> {
              // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
              DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, MMM dd, yyyy");
              while (!stop) {
                LocalDateTime now = LocalDateTime.now();
                try {
                  Thread.sleep(1000);
                } catch (Exception e) {
                  System.out.print(e);
                }
                String currentTimeDate = dtf.format(now);
                Platform.runLater(
                    () -> {
                      date.setText(currentTimeDate);
                    });
              }
            });
    thread.start();
  }

  @FXML
  public void time() throws InterruptedException {
    Thread thread =
        new Thread(
            () -> {
              DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

              while (!stop) {
                LocalDateTime now = LocalDateTime.now();
                try {
                  Thread.sleep(1000);
                } catch (Exception e) {
                  System.out.print(e);
                }
                String currentTimeDate = dtf.format(now);
                Platform.runLater(
                    () -> {
                      time.setText(currentTimeDate);
                    });
              }
            });
    thread.start();
  }

  public void grabQuote() throws IOException, InterruptedException {
    HttpRequest request2 =
        HttpRequest.newBuilder()
            .uri(URI.create("https://quotes15.p.rapidapi.com/quotes/random/"))
            .header("X-RapidAPI-Key", "d4914e733dmshdd1ec11f2fb2c05p1452d7jsn0a66c1b7ff90")
            .header("X-RapidAPI-Host", "quotes15.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
    HttpResponse<String> response =
        HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());

    String quote =
        response
            .body()
            .substring(
                response.body().indexOf("\"content\":\"", 0) + 11,
                response.body().indexOf("\"", response.body().indexOf("\"content\":\"") + 15));
    String author =
        response
            .body()
            .substring(
                response.body().indexOf("\"name\":\"", 0) + 8,
                response.body().indexOf("\"", response.body().indexOf("\"name\":\"") + 9));
    message.setText(
        "The greatest glory in living lies not in never falling, but in rising every time we fall. -Nelson Mandela");
  }
}
