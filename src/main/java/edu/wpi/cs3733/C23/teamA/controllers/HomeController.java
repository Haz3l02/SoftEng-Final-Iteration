package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.MaintenanceAssignedAccepted;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;

public class HomeController extends MenuController {

  @FXML private TableView<ServiceRequestEntity> assignmentsTable;
  @FXML public TableColumn<ServiceRequestEntity, Integer> IDCol;
  @FXML public TableColumn<ServiceRequestEntity, String> requestTypeCol;
  @FXML public TableColumn<ServiceRequestEntity, String> locationCol;
  @FXML public TableColumn<ServiceRequestEntity, String> urgencyCol;
  @FXML public TableView<MaintenanceAssignedAccepted> employeeTable;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> nameCol;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> assignedCol;
  @FXML public TableColumn<MaintenanceAssignedAccepted, String> acceptedCol;
  @FXML private Label date = new Label("hello");
  @FXML private Label time = new Label("hello");
  @FXML private Label message = new Label("hello");
  @FXML private Label welcome = new Label("hello");
  @FXML private MFXButton assignmentsButton;

  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();

  private ObservableList<MaintenanceAssignedAccepted> dbTableRowsModel2 =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() throws IOException, InterruptedException {
    grabQuote();
    date();
    time();
    IdNumberHolder userInfo = new IdNumberHolder();
    userInfo = IdNumberHolder.getInstance();
    welcome.setText("Welcome " + userInfo.getName() + "!");
    if (userInfo.getJob().equalsIgnoreCase("Maintenance") && IDCol != null) {

      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getLocation().getLongname()));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      Session session = getSessionFactory().openSession();
      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();

      requests =
          FacadeRepository.getInstance().getServiceRequestByAssigned(userInfo.getEmployeeID());
      if (requests.size() == 0) {
        assignmentsButton.setDisable(true);
      } else {
        assignmentsButton.setDisable(false);
      }
      dbTableRowsModel.addAll(requests);

      assignmentsTable.setItems(dbTableRowsModel);
      session.close();
    } else if (userInfo.getJob().equalsIgnoreCase("Admin") && IDCol != null) {
      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getLocation().getLongname()));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();
      requests = FacadeRepository.getInstance().getServiceRequestByUnassigned();
      dbTableRowsModel.addAll(requests);
      assignmentsTable.setItems(dbTableRowsModel);

      nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
      assignedCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getNumAssigned()));
      acceptedCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getNumAccepted()));

      List<EmployeeEntity> maintenanceEmployees =
          FacadeRepository.getInstance().getEmployeeByJob("maintenance");
      List<MaintenanceAssignedAccepted> maa = new ArrayList<MaintenanceAssignedAccepted>();
      for (EmployeeEntity employee : maintenanceEmployees) {
        maa.add(new MaintenanceAssignedAccepted(employee.getName(), employee.getEmployeeid()));
      }
      System.out.println(maa.size());
      dbTableRowsModel2.addAll(maa);
      for (int i = 0; i < maa.size(); i++) {
        System.out.println(maa.get(i).getName());
      }
      System.out.println(dbTableRowsModel2.size());
      employeeTable.setItems(dbTableRowsModel2);

    } else {
      // Code for medical homepage
    }
  }

  @FXML
  public void switchToStatus(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
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
