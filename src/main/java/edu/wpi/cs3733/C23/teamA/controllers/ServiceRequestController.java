package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
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
  protected int employeeID;
  protected UrgencyLevel urgent;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String name = holder.getName();
    String id = holder.getId();
    employeeID = holder.getEmployeeID();

    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }

    if (urgencyBox != null) {

      ObservableList<String> urgencies =
          FXCollections.observableArrayList(UrgencyLevel.urgencyList());
      List<LocationNameEntity> temp = FacadeRepository.getInstance().getAllLocation();

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
  void acceptRequest(ActionEvent event) throws IOException {
    FacadeRepository.getInstance()
        .updateStatusOfServ(Status.PROCESSING, acceptTheForm.getRequestID());
    switchToServiceRequestStatus(event);
  }

  @FXML
  public void rejectRequest(ActionEvent event) throws IOException {
    FacadeRepository.getInstance().updateServEmployee(null, acceptTheForm.getRequestID());
    FacadeRepository.getInstance().updateStatusOfServ(Status.NEW, acceptTheForm.getRequestID());
    switchToServiceRequestStatus(event);
  }
}
