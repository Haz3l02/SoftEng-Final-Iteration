package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.Main;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

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
  @FXML private MFXButton viewForm;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton cancel;
  @FXML private MFXTextField fileNameField;
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXButton exportCSVButton;
  @FXML private MFXButton importCSVButton;

  public static EditTheForm newEdit = new EditTheForm(0, "", false);
  public static AcceptTheForm acceptTheForm = new AcceptTheForm(0, "", false);
  public static ImportExportCSV iecsv = new ImportExportCSV("");

  private String hospitalID;
  private int employeeID;

  private String job;
  private String name;
  private ObservableList<ServiceRequestEntity> dbTableRowsModel =
      FXCollections.observableArrayList();
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
  public void initialize() throws SQLException, IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/HelpFXML.fxml"));
    PopOver popup = new PopOver(loader.load());
    IdNumberHolder holder = IdNumberHolder.getInstance();
    hospitalID = holder.getId();
    name = holder.getName();
    job = holder.getJob();
    employeeID = holder.getEmployeeID();

    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }

    // Assign permissions to differentiate between medical and non-medical staff
    if (statusBox != null) {
      if (job.equalsIgnoreCase("medical")) {
        statusBox.setDisable(true);
        employeeBox.setDisable(true);
        formTypeBox.setDisable(true);
        dateBox.setDisable(true);
        urgencyBox.setDisable(false);
        viewForm.setVisible(false);
        deleteButton.setVisible(true);
        exportCSVButton.setDisable(true);
        exportCSVButton.setVisible(false);

      } else if (job.equalsIgnoreCase("Maintenance")) {
        statusBox.setDisable(false);
        employeeBox.setDisable(true);
        formTypeBox.setDisable(true);
        dateBox.setDisable(true);
        urgencyBox.setDisable(true);
        editForm.setVisible(false);
        deleteButton.setDisable(true);
        deleteButton.setVisible(false);
        exportCSVButton.setDisable(true);
        exportCSVButton.setVisible(false);
      } else if (job.equalsIgnoreCase(("Admin"))) {
        statusBox.setDisable(true);
        employeeBox.setDisable(false);
        formTypeBox.setDisable(true);
        dateBox.setDisable(true);
        urgencyBox.setDisable(true);
        editForm.setVisible(false);
        deleteButton.setDisable(true);
        deleteButton.setVisible(false);
        exportCSVButton.setDisable(false);
        exportCSVButton.setVisible(true);
      }

      IDCol.setCellValueFactory(new PropertyValueFactory<>("requestid"));
      formTypeCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getRequestType().requestType));
      dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
      statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
      urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
      // try {
      employeeAssignedCol.setCellValueFactory(
          param -> new SimpleStringProperty(param.getValue().getEmployeeAssigned().getUsername()));
      //      } catch (NullPointerException e) {
      //        employeeAssignedCol.setCellValueFactory(param -> new
      // SimpleStringProperty("Unassigned"));
      //      }
      if (job.equalsIgnoreCase("medical")) {
        serviceRequestData = FacadeRepository.getInstance().getAllServByEmployee(hospitalID);
      } else if (job.equalsIgnoreCase("Maintenance")) {
        serviceRequestData =
            FacadeRepository.getInstance().getServiceRequestByAssigned(holder.getEmployeeID());
      } else if (job.equalsIgnoreCase("Admin")) {
        serviceRequestData = FacadeRepository.getInstance().getServiceRequestByUnassigned();
      }
      dbTableRowsModel.addAll(serviceRequestData);

      serviceReqsTable.setItems(dbTableRowsModel);
    }
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
      employeeBox.setValue(
          String.valueOf(clickedServiceReqTableRow.getEmployeeAssigned().getUsername()));
      if (job.equalsIgnoreCase("medical")) {
        editForm.setVisible(true);
        editForm.setDisable(false);
        deleteButton.setDisable(false);
      } else {
        editForm.setVisible(false);
        viewForm.setDisable(false);
        deleteButton.setDisable(true);
      }
    }

    ObservableList<String> statuses = FXCollections.observableArrayList(Status.statusList());

    ObservableList<String> urgencies =
        FXCollections.observableArrayList(UrgencyLevel.urgencyList());

    ObservableList<String> formTypes = FXCollections.observableArrayList(FormType.typeList());

    ObservableList<String> maintenance =
        FXCollections.observableArrayList(
            FacadeRepository.getInstance().getListEmployeeOfByJob("Maintenance"));

    statusBox.setItems(statuses);
    urgencyBox.setItems(urgencies);
    formTypeBox.setItems(formTypes);
    employeeBox.setItems(maintenance);
  }

  @FXML
  public void delete(ActionEvent event) {
    int currentRowId = Integer.parseInt(IDBoxSaver.getText());
    FacadeRepository.getInstance().deleteServiceRequest(currentRowId);
    reloadData();
  }

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      if (job.equalsIgnoreCase("medical")) {
        serviceRequestData = FacadeRepository.getInstance().getAllServByEmployee(hospitalID);
      } else if (job.equalsIgnoreCase("Maintenance")) {
        serviceRequestData = FacadeRepository.getInstance().getServiceRequestByAssigned(employeeID);
      } else if (job.equalsIgnoreCase("Admin")) {
        serviceRequestData = FacadeRepository.getInstance().getServiceRequestByUnassigned();
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
          SRTable.setStatus(Status.valueOf(statusBox.getText().toUpperCase()));
          SRTable.setUrgency(UrgencyLevel.valueOf(urgencyBox.getText().toUpperCase()));

          if (employeeBox.getText().equalsIgnoreCase("unassigned"))
            SRTable.setEmployeeAssigned(null); // MAKE INT
          else
            SRTable.setEmployeeAssigned(
                FacadeRepository.getInstance().getEmployeeByUser(employeeBox.getText()));

          FacadeRepository.getInstance().updateServiceRequest(currentRowId, SRTable);

          reloadData();
          break;
        }
      }
    }
  }

  public void clearEdits() {
    IDBoxSaver.setText("");
    formTypeBox.clear();
    dateBox.clear();
    statusBox.clear();
    urgencyBox.clear();
    employeeBox.clear();
  }

  public void acceptForm(ActionEvent event) {
    ServiceRequestEntity clickedRow = serviceReqsTable.getSelectionModel().getSelectedItem();
    acceptTheForm =
        new AcceptTheForm(clickedRow.getRequestid(), clickedRow.getRequestType().requestType, true);
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
      case "Patient Transport":
        Navigation.navigate(Screen.PATIENT_TRANSPORT);
        break;
      default:
        Navigation.navigateHome(Screen.HOME);
        break;
    }
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
      case "Transportation":
        Navigation.navigate(Screen.PATIENT_TRANSPORT);
        break;
      default:
        Navigation.navigateHome(Screen.HOME);
        break;
    }
  }
}
