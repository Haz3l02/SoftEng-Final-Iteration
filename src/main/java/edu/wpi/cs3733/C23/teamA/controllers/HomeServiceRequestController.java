package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ServiceRequestEntity.getServiceByEmployee;

import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.hibernateDB.ServiceRequestEntity;
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
import org.hibernate.Session;

public class HomeServiceRequestController extends ServiceRequestController {

  @FXML MFXButton pastSubmissions;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String hospitalID = holder.getId();
    String job = holder.getJob();
    Session session = getSessionFactory().openSession();

    ArrayList<ServiceRequestEntity> specificRequests = new ArrayList<ServiceRequestEntity>();
    specificRequests = getServiceByEmployee(hospitalID, session);
    session.close();

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
  public void switchToIDInput(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.ID_INPUT);
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
