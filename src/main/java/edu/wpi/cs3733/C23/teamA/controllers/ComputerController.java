package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComputerController extends MenuController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;
  UrgencyLevel urgent;
  DevicesCategory device;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String name = holder.getName();
    String id = holder.getId();

    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }
    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(DevicesCategory.deviceList());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(UrgencyLevel.urgencyList());

      Session session = getSessionFactory().openSession();
      List<LocationNameEntity> temp = getAllRecords(LocationNameEntity.class, session);

      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }

      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);

      nameBox.setText(name);
      IDNum.setText(id);

      devicesBox.setItems(devices);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.

    if (newEdit.needEdits && newEdit.getRequestType().equals("Computer")) {
      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      ComputerRequestEntity editComputerRequest =
          session.get(ComputerRequestEntity.class, newEdit.getRequestID());
      nameBox.setText(editComputerRequest.getName());
      IDNum.setText(editComputerRequest.getEmployee().getEmployeeid());
      devicesBox.setText(editComputerRequest.getDevice().toString());
      deviceIDNum.setText(editComputerRequest.getDeviceid());
      locationBox.setText(editComputerRequest.getLocation().getLongname());
      urgencyBox.setText(editComputerRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editComputerRequest.getDescription());
      // session.persist(submission);
      tx.commit();
      session.close();
    }
    // }
    // Otherwise Initialize service requests as normal
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) {
    Navigation.navigate(Screen.COMPUTER_CONFIRMATION);
  }

  @FXML
  public void switchToComputerScene(ActionEvent event) {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  void submitRequest(ActionEvent event) {
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      if (newEdit.needEdits) {

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        device = DevicesCategory.valueOf(devicesBox.getValue().toUpperCase());

        ComputerRequestEntity submission =
            session.get(ComputerRequestEntity.class, newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = session.get(LocationNameEntity.class, locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setDevice(device);
        submission.setDeviceid(deviceIDNum.getText());
      } else {

        EmployeeEntity person = session.get(EmployeeEntity.class, IDNum.getText());
        LocationNameEntity location = session.get(LocationNameEntity.class, locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        device = DevicesCategory.valueOf(devicesBox.getValue().toUpperCase());

        ComputerRequestEntity submission =
            new ComputerRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.COMPUTER,
                Status.BLANK,
                "Unassigned",
                deviceIDNum.getText(),
                device);
        session.persist(submission);
      }
      tx.commit();
      session.close();

      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    deviceIDNum.clear();
    devicesBox.clear();
  }
}
