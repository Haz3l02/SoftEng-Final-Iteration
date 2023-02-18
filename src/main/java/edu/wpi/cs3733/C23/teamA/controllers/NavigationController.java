package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ImportExportCSV;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;

public abstract class NavigationController {
  public volatile boolean stop = false;

  public static ImportExportCSV iecsv = new ImportExportCSV("");

  public void switchToImport(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.IMPORT_CSV);
  }

  @FXML
  public void switchToExport(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.EXPORT_CSV);
  }
  /*
  @FXML
  public void switchToNodeScene(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME_DATABASE);
  }
   */
  @FXML
  public void switchToDBHomeScene(ActionEvent event) {
    Navigation.navigate(Screen.HOME_DATABASE);
  }

  @FXML
  public void switchToHomeServiceRequestScene(javafx.event.ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  public void switchToServiceRequestStatus(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }

  @FXML
  public void switchToConfirmationScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.CONFIRMATION);
  }

  @FXML
  public void switchToHomeDatabaseScene(javafx.event.ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }

  @FXML
  public void switchToSecurityScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToSanitationScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToComputerScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToPatientTransportScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.PATIENT_TRANSPORT);
  }

  @FXML
  public void switchToSanitation(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToSecurity(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToComputer(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToPatientTransport(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.PATIENT_TRANSPORT);
  }

  @FXML
  public void switchToMapScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void switchToEdgeScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(javafx.event.ActionEvent event) {
    iecsv = new ImportExportCSV("node");
    Navigation.navigate(Screen.NODE);
  }

  public void switchToMoveScene(javafx.event.ActionEvent event) {
    iecsv = new ImportExportCSV("move");
    Navigation.navigate(Screen.MOVE);
  }

  public void switchToEmployeeScene(javafx.event.ActionEvent event) {
    iecsv = new ImportExportCSV("employee");
    Navigation.navigate(Screen.EMPLOYEE);
  }

  @FXML
  public void switchToHomeScene(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeDatabase(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }
}
