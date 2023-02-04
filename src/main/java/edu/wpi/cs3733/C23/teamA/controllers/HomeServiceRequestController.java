package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.SanitationRequest;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeServiceRequestController extends ServiceRequestController {

  @FXML MFXButton pastSubmissions;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String hospitalID = holder.getId();

    SanitationRequest sr = new SanitationRequest();

    ArrayList<ServiceRequest> specificRequests = new ArrayList<>();
    specificRequests = sr.getServiceRequestsByID(hospitalID);

    if (specificRequests.size() == 0) {
      pastSubmissions.setDisable(true);
    } else {
      pastSubmissions.setDisable(false);
    }
  }

  @FXML
  public void switchToSanitation(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToSecurity(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToComputer(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToIDInput(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.ID_INPUT);
  }

  @FXML
  public void switchToServiceRequestStatus(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }
}
