package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ServicerequestEntity.getServiceByEmployee;

import edu.wpi.cs3733.C23.teamA.enums.FormType;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.ServicerequestEntity;
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
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
  ServicerequestEntity.Urgency urgent;
  ServicerequestEntity.Status status;

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

    if (job.equals("Medical") || job.equals("medical")) {
      statusBox.setDisable(true);
      employeeBox.setDisable(true);
      formTypeBox.setDisable(true);
      dateBox.setDisable(true);
      urgencyBox.setDisable(false);

    } else {
      statusBox.setDisable(false);
      employeeBox.setDisable(false);
      formTypeBox.setDisable(true);
      dateBox.setDisable(true);
      urgencyBox.setDisable(true);
    }

    IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    ServiceRequest sr = new SanitationRequest();

    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // ArrayList<ServiceRequest> specificRequests = new ArrayList<ServiceRequest>();
    List<ServicerequestEntity> requests = new ArrayList<ServicerequestEntity>();

    if (job.equals("medical") || job.equals("Medical")) {
      // specificRequests = sr.getServiceRequestsByID(hospitalID);
      requests = getServiceByEmployee(hospitalID, session);

    } else {
      // specificRequests = sr.getServiceRequests();
      requests = getAllRecords(ServicerequestEntity.class, session);
    }

    for (ServicerequestEntity billy : requests) {
      ServiceRequestTableRow serviceReqNewRow =
          new ServiceRequestTableRow(
              billy.getRequestid(),
              billy.getRequesttype().toString(),
              billy.getDate().toString(),
              billy.getStatus().toString(),
              billy.getUrgency().urgency,
              billy.getEmployeeassigned());
      dbTableRowsModel.add(serviceReqNewRow);
    }

    serviceReqsTable.setItems(dbTableRowsModel);
    tx.commit();
    session.close();
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
      if (job.equals("Medical") || job.equals("medical")) {
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
          Session session = getSessionFactory().openSession();
          Transaction tx = session.beginTransaction();
          ServicerequestEntity billy = session.get(ServicerequestEntity.class, currentRowId);

          if (statusBox != null && !statusBox.isDisabled()) {
            switch (statusBox.getValue()) {
              case "Blank":
                status = ServicerequestEntity.Status.BLANK;
                break;
              case "Processing":
                status = ServicerequestEntity.Status.PROCESSING;
                break;
              case "Done":
                status = ServicerequestEntity.Status.DONE;
                break;
              default:
                status = ServicerequestEntity.Status.BLANK;
                break;
            }

            billy.setStatus(status);
          }
          if (urgencyBox != null && !urgencyBox.isDisabled()) {
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
            billy.setUrgency(urgent);
          }
          billy.setEmployeeassigned(employeeBox.getText());
          // billy.updateStatusEmployee(currentRowId, statusBox.getText(), employeeBox.getText());
          tx.commit();
          session.close();
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
