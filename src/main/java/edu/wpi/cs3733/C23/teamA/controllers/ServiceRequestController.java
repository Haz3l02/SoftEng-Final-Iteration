package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class ServiceRequestController extends MenuController {

  @FXML protected MFXTextField nameBox;
  @FXML protected MFXTextField IDNum;
  @FXML protected MFXTextField descBox;
  @FXML protected MFXComboBox<String> urgencyBox;
  @FXML protected MFXFilterComboBox<String> locationBox;
  @FXML protected Text reminder;
  @FXML protected StackPane reminderPane;
  protected UrgencyLevel urgent;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String name = holder.getName();
    String id = holder.getId();

    if (urgencyBox != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(UrgencyLevel.urgencyList());
      LocationNameImpl locationI = new LocationNameImpl();
      List<LocationNameEntity> temp = locationI.getAll();

      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }

      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);

      nameBox.setText(name);
      IDNum.setText(id);

      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
  }

  @FXML
  void clearForm() {
    descBox.clear();
    urgencyBox.clear();
    locationBox.clear();
  }

  @FXML
  public void switchToSecurityScene(ActionEvent event) {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToSanitationScene(ActionEvent event) {
    Navigation.navigate(Screen.SANITATION);
  }

  @FXML
  public void switchToComputerScene(ActionEvent event) {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  public void switchToPatientTransportScene(ActionEvent event) {
    Navigation.navigate(Screen.PATIENT_TRANSPORT);
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  }
}
