package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MessageBoardEntity;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class NewMessageController {

  @FXML MFXComboBox<String> recipientBox;
  @FXML MFXTextField titleBox;
  @FXML TextArea messageBox;

  static EmployeeEntity employee;

  @FXML
  public void initialize() {

    // TODO: HARDCODED IN, REMOVE LATER
    employee = FacadeRepository.getInstance().getEmployeeByUser("admin");
    /*
    FacadeRepository.getInstance()
        .addMessage(
            new MessageBoardEntity(
                employee, employee, "AAA", "aaa", new Timestamp(System.currentTimeMillis())));

     */

    ObservableList<String> employees = FXCollections.observableArrayList();

    for (EmployeeEntity ee :
        FXCollections.observableArrayList(FacadeRepository.getInstance().getAllEmployee())) {
      employees.add(ee.getUsername());
    }

    recipientBox.setItems(employees);
  }

  @FXML
  void hidePopup(ActionEvent event) {

    MessagingController.hidePopup();
  }

  @FXML
  void sendMessage(ActionEvent event) {

    System.out.println(
        employee.getUsername()
            + ", "
            + FacadeRepository.getInstance()
                .getEmployeeByUser(recipientBox.getSelectedItem())
                .getUsername()
            + ", "
            + titleBox.getText()
            + ", "
            + messageBox.getText()
            + ", "
            + new Timestamp(System.currentTimeMillis()));

    FacadeRepository.getInstance()
        .addMessage(
            new MessageBoardEntity(
                employee,
                // FacadeRepository.getInstance().getEmployeeByUser(recipientBox.getSelectedItem()),
                employee, // hardcoded, remove later
                titleBox.getText(),
                messageBox.getText(),
                new Timestamp(System.currentTimeMillis())));
  }
}
