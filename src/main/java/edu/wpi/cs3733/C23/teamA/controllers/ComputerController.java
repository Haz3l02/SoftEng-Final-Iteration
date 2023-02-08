package edu.wpi.cs3733.C23.teamA.controllers;

<<<<<<< HEAD
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.enums.DevicesCatagory;
=======
import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
>>>>>>> 961e483c4bc70f6c6c0c7d4e10d00d74abc8a8bc
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComputerController extends ServiceRequestController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;
  ServicerequestEntity.Urgency urgent;
  ComputerrequestEntity.Device device;

  @FXML
  public void initialize() throws SQLException {
    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }
    if (devicesBox != null) {
      ObservableList<String> devices =
          FXCollections.observableArrayList(
              DevicesCategory.DESKTOP.getDevices(),
              DevicesCategory.TABLET.getDevices(),
              DevicesCategory.LAPTOP.getDevices(),
              DevicesCategory.MONITOR.getDevices(),
              DevicesCategory.PERIPHERALS.getDevices(),
              DevicesCategory.KIOSK.getDevices(),
              DevicesCategory.PRINTER.getDevices());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(
              UrgencyLevel.LOW.getUrgency(),
              UrgencyLevel.MEDIUM.getUrgency(),
              UrgencyLevel.HIGH.getUrgency(),
              UrgencyLevel.EXTREMELY_URGENT.getUrgency());

      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();

      List<LocationnameEntity> temp = new ArrayList<LocationnameEntity>();
      temp = getAllRecords(LocationnameEntity.class, session);

      // ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationnameEntity move : temp) {
        locations.add(move.getLongname());
      }

      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);

      devicesBox.setItems(devices);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.

    if (newEdit.needEdits && newEdit.getRequestType().equals("COMPUTER")) {
      //      String requestType =
      //          (newEdit.getRequestType())
      //              .substring(0, (newEdit.getRequestType().indexOf("Request")) - 1); //
      // "Computer"
      //      if (requestType.equals("Computer")) {
      System.out.println("here");
      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      ComputerrequestEntity editComputerRequest =
          session.get(ComputerrequestEntity.class, newEdit.getRequestID());
      System.out.println("before switch here");
      // editComputerRequest.getRequestID()
      // editComputerRequest = editComputerRequest.getComputerRequest(newEdit.getRequestID());
      System.out.println("after switch here");
      nameBox.setText(editComputerRequest.getName());
      IDNum.setText(editComputerRequest.getEmployee().getEmployeeid());
      devicesBox.setText(editComputerRequest.getDevice().toString());
      deviceIDNum.setText(editComputerRequest.getDeviceid());
      locationBox.setText(editComputerRequest.getLocation().toString());
      urgencyBox.setText(editComputerRequest.getUrgency().urgency);
      descBox.setText(editComputerRequest.getDescription());
      // session.persist(submission);
      tx.commit();
      session.close();
    }
    // }
    // Otherwise Initialize service requests as normal
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER_CONFIRMATION);
  }

  @FXML
  public void switchToComputerScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.COMPUTER);
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {
    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getText().equals("")
        || descBox.getText().equals("")
        || deviceIDNum.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        // something that submits it
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServicerequestEntity.Urgency.LOW;
            break;
          case "Medium":
            urgent = ServicerequestEntity.Urgency.MEDIUM;
            break;
          case "High":
            urgent = ServicerequestEntity.Urgency.HIGH;
            break;
          case "Extremely Urgent":
            urgent = ServicerequestEntity.Urgency.EXTREMELY_URGENT;
            break;
        }
        switch (devicesBox.getValue()) {
          case "Desktop":
            device = ComputerrequestEntity.Device.DESKTOP;
            break;
          case "Tablet":
            device = ComputerrequestEntity.Device.TABLET;
            break;
          case "Laptop":
            device = ComputerrequestEntity.Device.LAPTOP;
            break;
          case "Monitor":
            device = ComputerrequestEntity.Device.MONITOR;
            break;
          case "Peripherals":
            device = ComputerrequestEntity.Device.PERIPHERALS;
            break;
          case "Kiosk":
            device = ComputerrequestEntity.Device.KIOSK;
            break;
          case "Printer":
            device = ComputerrequestEntity.Device.PRINTER;
            break;
        }

        ComputerrequestEntity submission =
            session.get(ComputerrequestEntity.class, newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationnameEntity loc = session.get(LocationnameEntity.class, locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setDevice(device);
        submission.setDeviceid(deviceIDNum.getText());
        System.out.println("made the new change");
        // session.persist(submission);
        // session.merge(submission);
        tx.commit();
        session.close();
      } else {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        EmployeeEntity person = session.get(EmployeeEntity.class, "123");
        // IDNum.getText()
        LocationnameEntity location = session.get(LocationnameEntity.class, locationBox.getText());
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServicerequestEntity.Urgency.LOW;
            break;
          case "Medium":
            urgent = ServicerequestEntity.Urgency.MEDIUM;
            break;
          case "High":
            urgent = ServicerequestEntity.Urgency.HIGH;
            break;
          case "Extremely Urgent":
            urgent = ServicerequestEntity.Urgency.EXTREMELY_URGENT;
            break;
        }
        switch (devicesBox.getValue()) {
          case "Desktop":
            device = ComputerrequestEntity.Device.DESKTOP;
            break;
          case "Tablet":
            device = ComputerrequestEntity.Device.TABLET;
            break;
          case "Laptop":
            device = ComputerrequestEntity.Device.LAPTOP;
            break;
          case "Monitor":
            device = ComputerrequestEntity.Device.MONITOR;
            break;
          case "Peripherals":
            device = ComputerrequestEntity.Device.PERIPHERALS;
            break;
          case "Kiosk":
            device = ComputerrequestEntity.Device.KIOSK;
            break;
          case "Printer":
            device = ComputerrequestEntity.Device.PRINTER;
            break;
        }
        ComputerrequestEntity submission =
            new ComputerrequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServicerequestEntity.RequestType.COMPUTER,
                ServicerequestEntity.Status.BLANK,
                "Unassigned",
                deviceIDNum.getText(),
                device);
        session.persist(submission);
        tx.commit();
        session.close();
        // submission.insert(); // *some db thing for getting the request in there*
      }
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
