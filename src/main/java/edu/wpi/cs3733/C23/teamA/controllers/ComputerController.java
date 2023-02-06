package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.enums.DevicesCatagory;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ComputerRequest;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComputerController extends ServiceRequestController {

  @FXML private MFXTextField deviceIDNum;
  @FXML private MFXComboBox<String> devicesBox;
  private ComputerRequest submission = new ComputerRequest();
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
              DevicesCatagory.DESKTOP.getDevices(),
              DevicesCatagory.TABLET.getDevices(),
              DevicesCatagory.LAPTOP.getDevices(),
              DevicesCatagory.MONITOR.getDevices(),
              DevicesCatagory.PERIPHERALS.getDevices(),
              DevicesCatagory.KIOSK.getDevices(),
              DevicesCatagory.PRINTER.getDevices());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(
              UrgencyLevel.LOW.getUrgency(),
              UrgencyLevel.MEDIUM.getUrgency(),
              UrgencyLevel.HIGH.getUrgency(),
              UrgencyLevel.EXTREMELY_URGENT.getUrgency());

      ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (Move move : moves) {
        locations.add(move.getLongName());
      }

      devicesBox.setItems(devices);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.

    if (newEdit.needEdits) {
      String requestType =
          (newEdit.getRequestType())
              .substring(0, (newEdit.getRequestType().indexOf("Request")) - 1); // "Computer"
      if (requestType.equals("Computer")) {
        System.out.println("here");

        ComputerRequest editComputerRequest = new ComputerRequest();
        System.out.println("before switch here");
        // editComputerRequest.getRequestID()
        editComputerRequest = editComputerRequest.getComputerRequest(newEdit.getRequestID());
        System.out.println("after switch here");
        nameBox.setText(editComputerRequest.getName());
        IDNum.setText(editComputerRequest.getIdNum());
        devicesBox.setText(editComputerRequest.getDevice());
        deviceIDNum.setText(editComputerRequest.getDeviceID());
        locationBox.setText(editComputerRequest.getLocation());
        urgencyBox.setText(editComputerRequest.getUl());
        descBox.setText(editComputerRequest.getDescription());
      }
    }
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
      // reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        // something that submits it
        submission.updateComputerRequest(
            newEdit.getRequestID(),
            nameBox.getText(),
            IDNum.getText(),
            locationBox.getText(),
            descBox.getText(),
            urgencyBox.getValue(),
            "Computer Request",
            "Blank",
            "Unassigned",
            deviceIDNum.getText(),
            devicesBox.getValue());
      } else {
        Session session = ADBSingletonClass.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        EmployeeEntity person = session.get(EmployeeEntity.class, "123");
        // IDNum.getText()
        LocationnameEntity location = session.get(LocationnameEntity.class, locationBox.getText());
        switch (urgencyBox.getValue()) {
          case "Low":
            urgent = ServicerequestEntity.Urgency.LOW;
          case "Medium":
            urgent = ServicerequestEntity.Urgency.LOW;
          case "High":
            urgent = ServicerequestEntity.Urgency.LOW;
          case "Extremely Urgent":
            urgent = ServicerequestEntity.Urgency.LOW;
        }
        switch (devicesBox.getValue()) {
          case "Desktop":
            device = ComputerrequestEntity.Device.DESKTOP;
          case "Tablet":
            device = ComputerrequestEntity.Device.TABLET;
          case "Laptop":
            device = ComputerrequestEntity.Device.LAPTOP;
          case "Monitor":
            device = ComputerrequestEntity.Device.MONITOR;
          case "Peripherals":
            device = ComputerrequestEntity.Device.PERIPHERALS;
          case "Kiosk":
            device = ComputerrequestEntity.Device.KIOSK;
          case "Printer":
            device = ComputerrequestEntity.Device.PRINTER;
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
