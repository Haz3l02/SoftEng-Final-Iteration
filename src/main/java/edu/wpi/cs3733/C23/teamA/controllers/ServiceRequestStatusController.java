package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.enums.FormType;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.*;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.SanitationRequest;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ServiceRequest;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ServiceRequestTableRow;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ServiceRequestStatusController extends ServiceRequestController {

  @FXML private TableView<ServiceRequestTableRow> serviceReqsTable;
  @FXML public TableColumn<ServiceRequestTableRow, Integer> IDCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> formTypeCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> dateCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> statusCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> urgencyCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> employeeAssignedCol;

  // text boxes for editing
  @FXML public MFXComboBox<String> formTypeBox;
  @FXML public MFXTextField dateBox; // make a MFXDatePicker ?????? or not??
  @FXML public MFXComboBox<String> employeeBox;
  @FXML public MFXComboBox<String> statusBox;
  @FXML public Text IDBoxSaver;
  @FXML private MFXButton editForm;

  public static EditTheForm newEdit = new EditTheForm(0, "", false);

  private String hospitalID;
  private String job;

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  private ObservableList<ServiceRequestTableRow> dbTableRowsModel =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    hospitalID = holder.getId();
    job = holder.getJob();

    if (job.equals("medical")) {
      statusBox.setDisable(true);
      employeeBox.setDisable(true);
    } else if (holder.getJob().equals("maintenance")) {
      statusBox.setDisable(false);
      employeeBox.setDisable(false);
    }

    IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    SanitationRequest sr = new SanitationRequest();

    ArrayList<ServiceRequest> specificRequests = new ArrayList<ServiceRequest>();
    if (job.equals("medical")) {
      specificRequests = sr.getServiceRequestsByID(hospitalID);
    } else {
      specificRequests = sr.getServiceRequests();
    }

    for (ServiceRequest bla : specificRequests) {
      ServiceRequestTableRow serviceReqNewRow =
          new ServiceRequestTableRow(
              bla.getRequestID(),
              bla.getRequestType(),
              "date",
              bla.getStatus(),
              bla.getUl(),
              bla.getEmployeeAssigned());
      dbTableRowsModel.add(serviceReqNewRow);
    }

    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

    ServiceRequestTableRow clickedServiceReqTableRow =
        serviceReqsTable.getSelectionModel().getSelectedItem();

    if (clickedServiceReqTableRow != null) {

      IDBoxSaver.setText(String.valueOf(clickedServiceReqTableRow.getID()));
      formTypeBox.setText(String.valueOf(clickedServiceReqTableRow.getFormType()));
      dateBox.setText(String.valueOf(clickedServiceReqTableRow.getDate()));
      statusBox.setText(String.valueOf(clickedServiceReqTableRow.getStatus()));
      urgencyBox.setText(String.valueOf(clickedServiceReqTableRow.getUrgency()));
      employeeBox.setText(String.valueOf(clickedServiceReqTableRow.getEmployeeAssigned()));
      if (hospitalID.equals("69420")) {
        editForm.setVisible(true);
      }
    }

    ObservableList<String> statuses =
        FXCollections.observableArrayList(
            Status.BLANK.getStatus(), Status.PROCESSING.getStatus(), Status.DONE.getStatus());

    ObservableList<String> urgencies =
        FXCollections.observableArrayList(
            UrgencyLevel.LOW.getUrgency(),
            UrgencyLevel.MEDIUM.getUrgency(),
            UrgencyLevel.HIGH.getUrgency(),
            UrgencyLevel.EXTREMELY_URGENT.getUrgency());

    ObservableList<String> formTypes =
        FXCollections.observableArrayList(
            FormType.SANITATION.getFormType(),
            FormType.COMPUTER.getFormType(),
            FormType.SECURITY.getFormType());

    ObservableList<String> employees =
        FXCollections.observableArrayList(
            "Izzy",
            "Isabella",
            "Andrei",
            "Harrison",
            "John",
            "Chris",
            "Steve",
            "Hazel",
            "Audrey",
            "Sarah");

    statusBox.setItems(statuses);
    urgencyBox.setItems(urgencies);
    formTypeBox.setItems(formTypes);
    employeeBox.setItems(employees);
  }

  @FXML
  public void submitEdit(ActionEvent event) throws IOException, SQLException {

    if (!statusBox.getText().trim().isEmpty()
        && !dateBox.getText().trim().isEmpty()
        && !formTypeBox.getText().trim().isEmpty()
        && !urgencyBox.getText().trim().isEmpty()
        && !employeeBox.getText().trim().isEmpty()) {

      ObservableList<ServiceRequestTableRow> currentTableData = serviceReqsTable.getItems();
      int currentRowId = Integer.parseInt(IDBoxSaver.getText());

      for (ServiceRequestTableRow SRTable : currentTableData) {
        if (SRTable.getID() == currentRowId) {
          SRTable.setID(Integer.parseInt(IDBoxSaver.getText()));
          SRTable.setFormType(formTypeBox.getText());
          SRTable.setDate(dateBox.getText());
          SRTable.setStatus(statusBox.getText());
          SRTable.setUrgency(urgencyBox.getText());
          SRTable.setEmployeeAssigned(employeeBox.getText());

          serviceReqsTable.setItems(currentTableData);
          serviceReqsTable.refresh();

          SanitationRequest bob = new SanitationRequest();
          bob.updateStatusEmployee(currentRowId, statusBox.getText(), employeeBox.getText());

          break;
        }
      }
    }
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException {}

  public void clearEdits(ActionEvent event) {

    IDBoxSaver.setText("");
    formTypeBox.clear();
    dateBox.clear();
    statusBox.clear();
    urgencyBox.clear();
    employeeBox.clear();
  }

  public void editForm(ActionEvent event) {
    ServiceRequestTableRow clickedRow = serviceReqsTable.getSelectionModel().getSelectedItem();
    clickedRow.getFormType();
    clickedRow.getID();
    newEdit = new EditTheForm(clickedRow.getID(), clickedRow.getFormType(), true);
    Navigation.navigate(Screen.COMPUTER); // Change to specifci type
  }
}
