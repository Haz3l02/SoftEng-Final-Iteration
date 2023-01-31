package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.SanitationRequest;
import edu.wpi.cs3733.C23.teamA.ServiceRequest;
import edu.wpi.cs3733.C23.teamA.ServiceRequestTableRow;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
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
  @FXML public MFXTextField formTypeBox; // this should probably be a MFXComboBox
  @FXML public MFXTextField dateBox;
  @FXML public MFXTextField employeeBox;
  @FXML public MFXTextField statusBox;
  @FXML public Text IDBoxSaver;

  private String hospitalID;

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  private ObservableList<ServiceRequestTableRow> dbTableRowsModel =
      FXCollections.observableArrayList();

  /*
  FXCollections.observableArrayList(
  new ServiceRequestTableRow(0, "aa", "aaaaaaaa", "aaa", "aaaaaaa", "aaaa"));

   */

  //  @FXML
  //  private void receiveData(ActionEvent event) {
  //    IdNumberHolder holder = IdNumberHolder.getInstance();
  //    id = holder.getId();
  //  }

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    hospitalID = holder.getId();

    IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    SanitationRequest sr = new SanitationRequest();

    ArrayList<ServiceRequest> specificRequests = new ArrayList<>();
    specificRequests = sr.getServiceRequestsByID(hospitalID); // MAKE SURE TO FIX THIS;
    // System.out.println(specificRequests.get(0).getName());

    for (ServiceRequest bla : specificRequests) {
      ServiceRequestTableRow serviceReqNewRow =
          new ServiceRequestTableRow(
              bla.getRequestID(),
              bla.getRequestType(),
              "date",
              bla.getStatus(),
              bla.getUl(),
              bla.getEmployeeAssigned());
      System.out.println(bla.getRequestID()); // todo: remove later
      dbTableRowsModel.add(serviceReqNewRow);
    }

    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void rowClicked(MouseEvent event) {
    // TODO: make it not throw a bunch of errors when an empty row is clicked (add an if statement
    // that checks for null values I guess?)

    ServiceRequestTableRow clickedServiceReqTableRow =
        serviceReqsTable.getSelectionModel().getSelectedItem();

    IDBoxSaver.setText(String.valueOf(clickedServiceReqTableRow.getID()));
    formTypeBox.setText(String.valueOf(clickedServiceReqTableRow.getFormType()));
    dateBox.setText(String.valueOf(clickedServiceReqTableRow.getDate()));
    statusBox.setText(String.valueOf(clickedServiceReqTableRow.getStatus()));
    urgencyBox.setText(String.valueOf(clickedServiceReqTableRow.getUrgency()));
    employeeBox.setText(String.valueOf(clickedServiceReqTableRow.getEmployeeAssigned()));
  }

  @FXML
  public void submitEdit(ActionEvent event) throws IOException, SQLException {

    if (!IDBoxSaver.equals("")) {

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
  //  @FXML
  //  public void editData(ActionEvent event) throws IOException {
  //  edit.
  //  }

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
}
