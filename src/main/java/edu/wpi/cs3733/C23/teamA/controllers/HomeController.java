package edu.wpi.cs3733.C23.teamA.controllers;

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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController extends MenuController {

  @FXML private Label time = new Label("hello");
  @FXML private Label message = new Label("hello");
  @FXML private Label welcome = new Label("hello");

  @FXML
  public void initialize() throws IOException, InterruptedException {
    grabQuote();
    dateAndTime();
    // IdNumberHolder userInfo = new IdNumberHolder();
    IdNumberHolder userInfo = IdNumberHolder.getInstance();
    welcome.setText("Welcome " + userInfo.getName() + "!");
  }

  @FXML
  public void switchToDatabase(ActionEvent event) throws IOException {
    stop = true;
    Navigation.navigate(Screen.HOME_DATABASE);
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
