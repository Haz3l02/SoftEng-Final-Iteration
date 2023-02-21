package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.acceptTheForm;
import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.enums.*;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AudioVisualController extends ServiceRequestController {
  private AVDevice category;
  private Subject subject;

  @FXML private MFXComboBox<String> devicesBox;
  @FXML private MFXComboBox<String> subjectBox;
  @FXML private MFXDatePicker returnDate;
  @FXML private MFXToggleButton installation;

  @FXML private MFXTextField numDevices;
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;
  private IdNumberHolder holder = IdNumberHolder.getInstance();

  @FXML
  public void initialize() throws SQLException {
    super.initialize();
    ObservableList<String> subjects = FXCollections.observableArrayList(Subject.subjectList());

    returnDate.setValue(LocalDate.now().plusWeeks(1L));
    subjectBox.setItems(subjects);

    if (devicesBox
        != null) { // this is here because SubmissionConfirmation page reuses this controller
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
      ObservableList<String> categories = FXCollections.observableArrayList(AVDevice.deviceList());
      devicesBox.setItems(categories);
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("Audio\\Visual")) {
      AudioVisualRequestEntity editRequest =
          FacadeRepository.getInstance().getAVRequest(newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(String.valueOf(editRequest.getEmployee().getEmployeeid()));
      devicesBox.setText(editRequest.getAvdevice().getDevice());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getAdditionalequipment());
      returnDate.setValue(editRequest.getReturndate());
      subjectBox.setText(editRequest.getSubject().getSubject());
      numDevices.setText(String.valueOf(editRequest.getNumdevices()));
      installation.setSelected(editRequest.isInstallationrequired());
      // set buttons enabled/disabled and visible/invisible
      accept.setDisable(true);
      accept.setVisible(false);
      clear.setDisable(false);
      clear.setVisible(true);
      submit.setDisable(false);
      submit.setVisible(true);
      reject.setDisable(true);
      reject.setVisible(false);

    } else if (acceptTheForm.acceptance && acceptTheForm.getRequestType().equals("Audio\\Visual")) {
      AudioVisualRequestEntity editRequest =
          FacadeRepository.getInstance().getAVRequest(acceptTheForm.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(String.valueOf(editRequest.getEmployee().getEmployeeid()));
      devicesBox.setText(editRequest.getAvdevice().getDevice());
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getAdditionalequipment());
      returnDate.setValue(editRequest.getReturndate());
      subjectBox.setText(editRequest.getSubject().getSubject());
      numDevices.setText(String.valueOf(editRequest.getNumdevices()));
      installation.setSelected(editRequest.isInstallationrequired());

      if (holder.getJob().equalsIgnoreCase("admin")) {
        accept.setDisable(true);
        reject.setDisable(true);
      } else {
        accept.setDisable(false);
        reject.setDisable(false);
        reject.setVisible(true);
      }
      accept.setVisible(true);
      clear.setDisable(true);
      clear.setVisible(false);
      submit.setDisable(true);
      submit.setVisible(false);
    }
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {

    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || devicesBox.getValue() == null
        || urgencyBox.getValue() == null
        || subjectBox.getText().equals("")
        || numDevices.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = AVDevice.valueOf(devicesBox.getValue().toUpperCase());

        AudioVisualRequestEntity submission =
            FacadeRepository.getInstance().getAVRequest(newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setAvdevice(category);
        submission.setNumdevices(Integer.parseInt(numDevices.getText()));
        submission.setAdditionalequipment(descBox.getText());
      } else {
        EmployeeEntity person =
            FacadeRepository.getInstance().getEmployee(Integer.parseInt(IDNum.getText()));
        LocationNameEntity location =
            FacadeRepository.getInstance().getLocation(locationBox.getText());

        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        category = AVDevice.valueOf(devicesBox.getValue().toUpperCase());
        subject = Subject.valueOf(subjectBox.getValue().toUpperCase());

        AudioVisualRequestEntity submission =
            new AudioVisualRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.AV,
                Status.NEW,
                "Unassigned",
                installation.isSelected(),
                Integer.parseInt(numDevices.getText()),
                category,
                subject,
                descBox.getText(),
                returnDate.getValue());
        FacadeRepository.getInstance().addAudioVisualRequest(submission);
      }
      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    devicesBox.clear();
  }
}
