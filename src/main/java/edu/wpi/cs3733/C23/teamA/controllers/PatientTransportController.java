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
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PatientTransportController extends ServiceRequestController {
  @FXML private MFXTextField pNameBox;
  @FXML private MFXTextField equipmentBox;
  @FXML private MFXTextField pIDBox;
  @FXML private MFXFilterComboBox<String> moveToBox;
  @FXML private MFXFilterComboBox<String> genderBox;
  @FXML private MFXFilterComboBox<String> modeBox;
  @FXML private MFXFilterComboBox<String> mobilityBox;
  @FXML private MFXToggleButton babyCheckBox;
  @FXML private MFXToggleButton immuneCheckBox;
  @FXML private MFXButton clear;
  @FXML private MFXButton submit;
  @FXML private MFXButton accept;
  @FXML private MFXButton reject;
  private Gender gender;
  private ModeOfTransfer mode;
  private Mobility mobility;
  private IdNumberHolder holder = IdNumberHolder.getInstance();

  @FXML
  public void initialize() throws SQLException {
    super.initialize();

    if (moveToBox != null) {
      reject.setDisable(true);
      reject.setVisible(false);
      accept.setDisable(true);
      accept.setVisible(false);
      // locations
      List<LocationNameEntity> temp = FacadeRepository.getInstance().getAllLocation();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }
      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);
      // genders
      ObservableList<String> genders = FXCollections.observableArrayList(Gender.genderList());
      // modes
      ObservableList<String> modes =
          FXCollections.observableArrayList(ModeOfTransfer.modeOfTransfersList());
      // mobility
      ObservableList<String> mobilities =
          FXCollections.observableArrayList(Mobility.mobilityList());

      // setting
      moveToBox.setItems(locations);
      genderBox.setItems(genders);
      modeBox.setItems(modes);
      mobilityBox.setItems(mobilities);
    }
    // If Edit past submissions is pressed. Open Service request with form fields filled out.
    if (newEdit.needEdits && newEdit.getRequestType().equals("Patient Transport")) {
      PatientTransportRequestEntity editPatientRequest =
          FacadeRepository.getInstance().getPatientTransport(newEdit.getRequestID());
      nameBox.setText(editPatientRequest.getName());
      IDNum.setText(String.valueOf(editPatientRequest.getEmployee().getEmployeeid()));
      urgencyBox.setText(editPatientRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editPatientRequest.getDescription());
      pNameBox.setText(editPatientRequest.getPatientName());
      locationBox.setText(editPatientRequest.getLocation().getLongname());
      moveToBox.setText(editPatientRequest.getMoveTo().getLongname());
      pIDBox.setText(editPatientRequest.getPatientID());
      equipmentBox.setText(editPatientRequest.getEquipment());
      genderBox.setText(editPatientRequest.getGender().getGender());
      modeBox.setText(editPatientRequest.getMode().getMode());
      mobilityBox.setText(editPatientRequest.getMobility().getMobility());
      babyCheckBox.setSelected(editPatientRequest.isBaby());
      immuneCheckBox.setSelected(editPatientRequest.isImmuneComp());
    } else if (acceptTheForm.acceptance
        && acceptTheForm.getRequestType().equals("Patient Transport")) {
      PatientTransportRequestEntity editPatientRequest =
          FacadeRepository.getInstance().getPatientTransport(acceptTheForm.getRequestID());
      nameBox.setText(editPatientRequest.getName());
      IDNum.setText(String.valueOf(editPatientRequest.getEmployee().getEmployeeid()));
      urgencyBox.setText(editPatientRequest.getUrgency().getUrgency()); // Double check
      descBox.setText(editPatientRequest.getDescription());
      pNameBox.setText(editPatientRequest.getPatientName());
      locationBox.setText(editPatientRequest.getLocation().getLongname());
      moveToBox.setText(editPatientRequest.getMoveTo().getLongname());
      pIDBox.setText(editPatientRequest.getPatientID());
      equipmentBox.setText(editPatientRequest.getEquipment());
      genderBox.setText(editPatientRequest.getGender().getGender());
      modeBox.setText(editPatientRequest.getMode().getMode());
      mobilityBox.setText(editPatientRequest.getMobility().getMobility());
      babyCheckBox.setSelected(editPatientRequest.isBaby());
      immuneCheckBox.setSelected(editPatientRequest.isImmuneComp());
      // sanI.closeSession();
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

    // Otherwise Initialize service requests as normal
  }

  @FXML
  void submitRequest(ActionEvent event) throws IOException, SQLException {

    if (nameBox.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || urgencyBox.getValue() == null
        || pNameBox.getText().equals("")
        || pIDBox.getText().equals("")
        || moveToBox.getValue() == null
        || equipmentBox.getText().equals("")
        || genderBox.getValue() == null
        || modeBox.getValue() == null
        || mobilityBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        // something that submits it

        // enums
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        gender = Gender.valueOf(genderBox.getValue().toUpperCase());
        mode = ModeOfTransfer.valueOf(modeBox.getValue().toUpperCase());
        mobility = Mobility.valueOf(mobilityBox.getValue().toUpperCase());
        // bools
        boolean babyBool = babyCheckBox.isSelected();
        boolean immuneBool = immuneCheckBox.isSelected();
        // locations
        LocationNameEntity loc1 =
            FacadeRepository.getInstance().getLocation(locationBox.getValue());
        LocationNameEntity loc2 = FacadeRepository.getInstance().getLocation(moveToBox.getValue());

        // create submission
        PatientTransportRequestEntity submission =
            FacadeRepository.getInstance().getPatientTransport(newEdit.getRequestID());

        // supers
        submission.setName(nameBox.getText());
        submission.setLocation(loc1);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        // sub-fields
        submission.setPatientID(pIDBox.getText());
        submission.setMoveTo(loc2);
        submission.setEquipment(equipmentBox.getText());
        submission.setGender(gender);
        submission.setMode(mode);
        submission.setMobility(mobility);
        submission.setBaby(babyBool);
        submission.setImmuneComp(immuneBool);
      } else {
        EmployeeEntity person =
            FacadeRepository.getInstance().getEmployee(Integer.parseInt(IDNum.getText()));

        // enums
        urgent = UrgencyLevel.valueOf(urgencyBox.getValue().toUpperCase());
        gender = Gender.valueOf(genderBox.getValue().toUpperCase());
        mode = ModeOfTransfer.valueOf(modeBox.getValue().toUpperCase());
        mobility = Mobility.valueOf(mobilityBox.getValue().toUpperCase());
        // bools
        boolean babyBool = babyCheckBox.isSelected();
        boolean immuneBool = immuneCheckBox.isSelected();
        // locations
        LocationNameEntity loc = FacadeRepository.getInstance().getLocation(locationBox.getValue());
        LocationNameEntity moveTo =
            FacadeRepository.getInstance().getLocation(moveToBox.getValue());
        PatientTransportRequestEntity submission =
            new PatientTransportRequestEntity(
                nameBox.getText(),
                person,
                loc,
                descBox.getText(),
                urgent,
                PatientTransportRequestEntity.RequestType.PATIENT_TRANSPORT,
                Status.NEW,
                "Unassigned",
                pNameBox.getText(),
                pIDBox.getText(),
                moveTo,
                equipmentBox.getText(),
                gender,
                mode,
                mobility,
                babyBool,
                immuneBool);
        FacadeRepository.getInstance().addPatientTransport(submission);
        // submission.insert(); // *some db thing for getting the request in there*
      }
      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  //  @FXML
  //  public void switchToHomeScene(ActionEvent event) throws IOException {
  //    Navigation.navigateHome(Screen.HOME_SERVICE_REQUEST);
  //  }

  @FXML
  void clearForm() {
    super.clearForm();
    pNameBox.clear();
    pIDBox.clear();
    moveToBox.clear();
    equipmentBox.clear();
    genderBox.clear();
    modeBox.clear();
    mobilityBox.clear();
    babyCheckBox.setSelected(false);
    immuneCheckBox.setSelected(false);
  }
}
