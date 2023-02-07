package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.controlsfx.control.PopOver;

public class HomeServiceRequestController extends ServiceRequestController {

  @FXML MFXButton pastSubmissions;

  @FXML
  public void initialize() throws SQLException {
    //    IdNumberHolder holder = IdNumberHolder.getInstance();
    //    String hospitalID = holder.getId();
    //    String job = holder.getJob();
    //
    //    SanitationRequest sr = new SanitationRequest();
    //
    //    ArrayList<ServiceRequest> specificRequests = new ArrayList<>();
    //    specificRequests = sr.getServiceRequestsByID(hospitalID);
    //
    //    if (specificRequests.size() == 0 && job.equals("medical")) {
    //      pastSubmissions.setDisable(true);
    //    } else {
    //      pastSubmissions.setDisable(false);
    //    }
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

  @FXML
  public void switchToCredits(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/CreditsFXML.fxml"));
    PopOver popup = new PopOver(loader.load());
    popup.show(((Node) event.getSource()).getScene().getWindow());
    popup.detach();
  }
}
