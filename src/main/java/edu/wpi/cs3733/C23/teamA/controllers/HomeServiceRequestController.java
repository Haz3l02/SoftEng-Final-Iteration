package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ServiceRequestImpl;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.controlsfx.control.PopOver;

public class HomeServiceRequestController extends MenuController {

  @FXML MFXButton pastSubmissions;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String hospitalID = holder.getId();
    String job = holder.getJob();

    ServiceRequestImpl serviceI = new ServiceRequestImpl();
    ArrayList<ServiceRequestEntity> specificRequests = serviceI.getAllByEmployee(hospitalID);


    if (specificRequests.size() == 0 && (job.equals("medical") || job.equals("Medical"))) {
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
  public void switchToPatientTransport(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.PATIENT_TRANSPORT);
  }

  @FXML
  public void switchToServiceRequestStatus(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
  }

  public void switchToCredits(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/CreditsFXML.fxml"));
    PopOver creditsPopUp = new PopOver(loader.load());
    creditsPopUp.show(((Node) event.getSource()).getScene().getWindow());
  }
}
