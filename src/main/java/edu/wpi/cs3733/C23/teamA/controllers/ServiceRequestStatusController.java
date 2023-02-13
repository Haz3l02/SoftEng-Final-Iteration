package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ServiceRequestImpl;
import edu.wpi.cs3733.C23.teamA.enums.FormType;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.*;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ServiceRequestStatusController extends MenuController {

  @FXML private TableView<ServiceRequestEntity> serviceReqsTable;
  @FXML public TableColumn<ServiceRequestEntity, Integer> IDCol;
  @FXML public TableColumn<ServiceRequestEntity, String> formTypeCol;
  @FXML public TableColumn<ServiceRequestEntity, String> dateCol;
  @FXML public TableColumn<ServiceRequestEntity, String> statusCol;
  @FXML public TableColumn<ServiceRequestEntity, String> urgencyCol;
  @FXML public TableColumn<ServiceRequestEntity, String> employeeAssignedCol;

  // text boxes for editing
  @FXML public MFXComboBox<String> formTypeBox;
  @FXML public MFXTextField dateBox; // make a MFXDatePicker ?????? or not??
  @FXML public MFXComboBox<String> employeeBox;
  @FXML public MFXComboBox<String> statusBox;
  @FXML public Text IDBoxSaver;
  @FXML private MFXButton editForm;
  @FXML private MFXButton deleteButton;

  UrgencyLevel urgent;
  Status status;

  public static EditTheForm newEdit = new EditTheForm(0, "", false);

  private String hospitalID;
  private String job;
  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();
  ServiceRequestImpl serviceRequestImpl = new ServiceRequestImpl();
  List<ServiceRequestEntity> serviceRequestData = new ArrayList<>();

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    if (holder.getJob().equalsIgnoreCase("Maintenance")) {
      Navigation.navigateHome(Screen.HOME_MAINTENANCE);
    } else if (holder.getJob().equalsIgnoreCase("Admin")) {
      Navigation.navigateHome(Screen.HOME_ADMIN);
    } else {
      Navigation.navigateHome(Screen.HOME_EMPLOYEE);
    }
  }

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    hospitalID = holder.getId();
    job = holder.getJob();

    // Assign permissions to differentiate between medical and non-medical staff
    if (job.equalsIgnoreCase("medical")) {
      statusBox.setDisable(true);
      employeeBox.setDisable(true);
      formTypeBox.setDisable(true);
      dateBox.setDisable(true);
      urgencyBox.setDisable(false);
    } else if (job.equalsIgnoreCase("maintenance")) {
      statusBox.setDisable(false);
      employeeBox.setDisable(true);
      formTypeBox.setDisable(true);
      dateBox.setDisable(true);
      urgencyBox.setDisable(true);
    } else { // admin
      statusBox.setDisable(true);
      employeeBox.setDisable(false);
      formTypeBox.setDisable(true);
      dateBox.setDisable(true);
      urgencyBox.setDisable(true);
    }

    IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
    formTypeCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    if (job.equalsIgnoreCase("medical"))
      serviceRequestData = serviceRequestImpl.getAllByEmployee(hospitalID);
    else serviceRequestData = serviceRequestImpl.getAll();

    dbTableRowsModel.addAll(serviceRequestData);
    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

    ServiceRequestEntity clickedServiceReqTableRow =
        serviceReqsTable.getSelectionModel().getSelectedItem();

    if (clickedServiceReqTableRow != null) {

      IDBoxSaver.setText(String.valueOf(clickedServiceReqTableRow.getRequestid()));
      formTypeBox.setText(String.valueOf(clickedServiceReqTableRow.getRequestType()));
      dateBox.setText(String.valueOf(clickedServiceReqTableRow.getDate()));
      statusBox.setText(String.valueOf(clickedServiceReqTableRow.getStatus()));
      urgencyBox.setText(String.valueOf(clickedServiceReqTableRow.getUrgency()));
      employeeBox.setText(String.valueOf(clickedServiceReqTableRow.getEmployeeAssigned()));
      if (job.equalsIgnoreCase(
          "medical")) { // if they have any submsisions its should be editable for their submissions
        editForm.setDisable(false);
      }
      deleteButton.setDisable(false);
    }

    ObservableList<String> statuses = FXCollections.observableArrayList(Status.statusList());

    ObservableList<String> urgencies =
        FXCollections.observableArrayList(UrgencyLevel.urgencyList());

    ObservableList<String> formTypes = FXCollections.observableArrayList(FormType.typeList());
    //
    //    ObservableList<String> employees =
    //        FXCollections.observableArrayList(
    //            "Izzy",
    //            "Isabella",
    //            "Andrei",
    //            "Harrison",
    //            "John",
    //            "Chris",
    //            "Steve",
    //            "Hazel",
    //            "Audrey",
    //            "Sarah");
    EmployeeImpl theEmployee = new EmployeeImpl();

    ObservableList<String> maintenance =
        FXCollections.observableArrayList(theEmployee.getListOf("Maintenance"));

    statusBox.setItems(statuses);
    urgencyBox.setItems(urgencies);
    formTypeBox.setItems(formTypes);
    employeeBox.setItems(maintenance);
  }

  @FXML
  public void delete(ActionEvent event) {
    int currentRowId = Integer.parseInt(IDBoxSaver.getText());
    serviceRequestImpl.delete(currentRowId);
    reloadData();
  }

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      if (job.equalsIgnoreCase("medical")) {
        serviceRequestData = serviceRequestImpl.getAllByEmployee(hospitalID);
      } else {
        serviceRequestData = serviceRequestImpl.getAll();
      }
      dbTableRowsModel.addAll(serviceRequestData);
      clearEdits();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void submitEdit(ActionEvent event) throws IOException, SQLException, ParseException {

    if (!statusBox.getText().trim().isEmpty()
        && !dateBox.getText().trim().isEmpty()
        && !formTypeBox.getText().trim().isEmpty()
        && !urgencyBox.getText().trim().isEmpty()
        && !employeeBox.getText().trim().isEmpty()) {

      ObservableList<ServiceRequestEntity> currentTableData = serviceReqsTable.getItems();
      int currentRowId = Integer.parseInt(IDBoxSaver.getText());

      for (ServiceRequestEntity SRTable : currentTableData) {
        if (SRTable.getRequestid() == currentRowId) {
          SRTable.setRequestid(Integer.parseInt(IDBoxSaver.getText()));
          SRTable.setRequestType(ServiceRequestEntity.RequestType.valueOf(formTypeBox.getText()));
          SRTable.setDate(Timestamp.valueOf(dateBox.getText()));
          SRTable.setStatus(Status.valueOf(statusBox.getText()));
          SRTable.setUrgency(UrgencyLevel.valueOf(urgencyBox.getText().toUpperCase()));
          SRTable.setEmployeeAssigned(employeeBox.getText());

          serviceRequestImpl.update(currentRowId, SRTable);
          reloadData();
          break;
        }
      }
    }
  }

  @FXML
  public void submitRequest(ActionEvent event) {}

  public void clearEdits() {
    IDBoxSaver.setText("");
    formTypeBox.clear();
    dateBox.clear();
    statusBox.clear();
    urgencyBox.clear();
    employeeBox.clear();
  }

  public void editForm(ActionEvent event) {
    ServiceRequestEntity clickedRow = serviceReqsTable.getSelectionModel().getSelectedItem();
    newEdit =
        new EditTheForm(clickedRow.getRequestid(), clickedRow.getRequestType().requestType, true);
    switch (clickedRow.getRequestType().requestType) {
      case "Computer":
        Navigation.navigate(Screen.COMPUTER);
        break;
      case "Sanitation":
        Navigation.navigate(Screen.SANITATION);
        break;
      case "Security":
        Navigation.navigate(Screen.SECURITY);
        break;
      default:
        Navigation.navigateHome(Screen.HOME);
        break;
    }
  }
}
