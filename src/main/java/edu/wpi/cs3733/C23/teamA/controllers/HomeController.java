package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ServiceRequestImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
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

public class HomeController extends MenuController {

  @FXML private TableView<ServiceRequestEntity> assignmentsTable;
  @FXML public TableColumn<ServiceRequestEntity, Integer> IDCol;
  @FXML public TableColumn<ServiceRequestEntity, String> requestTypeCol;
  @FXML public TableColumn<ServiceRequestEntity, LocationNameEntity> locationCol;
  @FXML public TableColumn<ServiceRequestEntity, String> urgencyCol;
  @FXML public TableColumn<ServiceRequestEntity, String> node1Col;
  @FXML public TableColumn<ServiceRequestEntity, String> node2Col;

  @FXML private Label time = new Label("hello");
  @FXML private Label message = new Label("hello");
  @FXML private Label welcome = new Label("hello");
  @FXML private MFXButton assignmentsButton;

  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();

  private ServiceRequestImpl sri = new ServiceRequestImpl();
  List<ServiceRequestEntity> requests = new ArrayList<ServiceRequestEntity>();

  @FXML
  public void initialize() throws IOException, InterruptedException {
    grabQuote();
    dateAndTime();
    IdNumberHolder userInfo = new IdNumberHolder();
    userInfo = IdNumberHolder.getInstance();
    welcome.setText("Welcome " + userInfo.getName() + "!");
    if (userInfo.getJob().equalsIgnoreCase("Maintenance") && IDCol != null) {

      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      requests = sri.getServiceRequestByAssigned(userInfo.getName());
      if (requests.size() == 0) {
        assignmentsButton.setDisable(true);
      } else {
        assignmentsButton.setDisable(false);
      }
      dbTableRowsModel.addAll(requests);

      assignmentsTable.setItems(dbTableRowsModel);
    } else if (userInfo.getJob().equalsIgnoreCase("Admin") && IDCol != null) {
      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      requestTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

      requests = sri.getServiceRequestByUnassigned();

      dbTableRowsModel.addAll(requests);

      assignmentsTable.setItems(dbTableRowsModel);
    }
    //    else {
    //      locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    //      node1Col.setCellValueFactory(
    //          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
    //      node2Col.setCellValueFactory(new PropertyValueFactory<>("node"));
    //      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    //
    //      MoveImpl move = new MoveImpl();
    //
    //      List<MoveEntity> moveData = new ArrayList<MoveEntity>();
    //
    //      moveData = move.getAll();
    //      moveData.dbTableRowsModel.addAll(requests);
    //
    //      assignmentsTable.setItems(dbTableRowsModel);
    //    }
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
