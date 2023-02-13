package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;
import static edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity.getServiceByEmployeeAssigned;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import java.awt.*;
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
  @FXML private Label time = new Label("hello");
  @FXML private Label message = new Label("hello");
  @FXML private Label welcome = new Label("hello");

  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() throws IOException, InterruptedException {
    grabQuote();
    dateAndTime();
    // IdNumberHolder userInfo = new IdNumberHolder();
    IdNumberHolder userInfo = IdNumberHolder.getInstance();
    welcome.setText("Welcome " + userInfo.getName() + "!");
    if (userInfo.getJob().equalsIgnoreCase("Maintenance")) {
      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      Session session = getSessionFactory().openSession();
      List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();

      requests = getServiceByEmployeeAssigned(userInfo.getName(), session);

      dbTableRowsModel.addAll(requests);

      assignmentsTable.setItems(dbTableRowsModel);
      session.close();
    }
  }

  @FXML
  public void switchToStatus(ActionEvent event) throws IOException {
    // stop = true;
    System.out.println("Hereh");
    Navigation.navigateHome(Screen.SERVICE_REQUEST_STATUS);
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
  public void dateAndTime() throws InterruptedException {

    Thread thread =
        new Thread(
            () -> {
              DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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
                      time.setText("Today's Date: " + currentTimeDate);
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
    message.setText("\"" + quote + "\" -" + author);
  }
}
