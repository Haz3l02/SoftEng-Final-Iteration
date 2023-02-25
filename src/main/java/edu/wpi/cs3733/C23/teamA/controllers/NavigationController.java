package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ImportExportCSV;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

public abstract class NavigationController {
  public volatile boolean stop = false;
  private static PopOver popup;

  public static ImportExportCSV iecsv = new ImportExportCSV("");

  public void switchToImport(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigate(Screen.IMPORT_CSV);
  }

  @FXML
  public void switchToExport(javafx.event.ActionEvent event) throws IOException {
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
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  @FXML
  public void switchToServiceRequestStatus() {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }

  @FXML
  public void switchToConfirmationScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.CONFIRMATION);
  }

  @FXML
  public void switchToHomeDatabaseScene(javafx.event.ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_ACTUAL);
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
  public void switchToSanitation() {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToSecurity() {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToAudioVisual() {
    Navigation.navigate(Screen.AUDIOVISUAL);
  }

  @FXML
  public void switchToAccessibility() {
    Navigation.navigate(Screen.ACCESSIBILITY);
  }

  @FXML
  public void switchToComputer() {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToPatientTransport() {
    Navigation.navigate(Screen.PATIENT_TRANSPORT);
  }

  @FXML
  public void switchToMapScene() {
    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void switchToEdgeScene(javafx.event.ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene() {
    iecsv = new ImportExportCSV("node");
    Navigation.navigate(Screen.NODE);
  }

  public void switchToMoveScene() {
    iecsv = new ImportExportCSV("move");
    Navigation.navigate(Screen.MOVE);
  }

  public void switchToEmployeeScene() {
    iecsv = new ImportExportCSV("employee");
    Navigation.navigate(Screen.EMPLOYEE);
  }

  public void switchToKioskSetupScene() {
    stop = true;
    Navigation.navigateHome(Screen.KIOSK_SETUP);
  }

  @FXML
  public void switchToHomeScene(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  @FXML
  public void switchToHomeDatabase(javafx.event.ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  @FXML
  public void switchToPathfinding() {
    stop = true;
    Navigation.navigate(Screen.PATHFINDING);
  }
}
