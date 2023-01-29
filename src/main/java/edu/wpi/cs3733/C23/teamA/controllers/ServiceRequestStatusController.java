package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.ServiceRequestTableRow;
import java.awt.*;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ServiceRequestStatusController extends ServiceRequestController {

  @FXML private TableView<ServiceRequestTableRow> serviceReqsTable;
  @FXML public TableColumn<ServiceRequestTableRow, Checkbox> checkbox;
  @FXML public TableColumn<ServiceRequestTableRow, String> formTypeCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> dateCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> statusCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> urgencyCol;
  @FXML public TableColumn<ServiceRequestTableRow, String> employeeAssignedCol;

  private CheckBox aCheckbox = new CheckBox("");

  private ObservableList<ServiceRequestTableRow> dbTableRowsModel =
      FXCollections.observableArrayList(
          new ServiceRequestTableRow(aCheckbox, "aaa", "aaaaaaaa", "aaa", "aaaaaaa", "aaaa"));

  @FXML
  public void initialize() {

    checkbox.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    System.out.println(requests.get("123").getName());

    //for (sanitationRequest.getSanitationRequestByUser(IDNum) :  request)
    requests.forEach(
        (key, value) -> {
          System.out.println(value.getRequestType());

          CheckBox ch = new CheckBox("");
          ServiceRequestTableRow serviceReqNewRow =
              new ServiceRequestTableRow(
                  ch,
                  value.getRequestType(),
                  "value.date()",
                  "value.status()",
                  value.getUl().toString(),
                  "value.getEmployeeAssigned()");
          dbTableRowsModel.add(serviceReqNewRow);
        });

    //    for (int i = 1; i <= 50; i++) {
    //      CheckBox ch = new CheckBox("");
    //      ServiceRequestTableRow serviceReqRow =
    //          new ServiceRequestTableRow(ch, "a", "aa", "aaa", "aaaa", "aaaaa");
    //      dbTableRowsModel.add(serviceReqRow);
    //    }
    //    requests.forEach((key,value) -> {
    //
    //      ServiceRequestTableRow serviceReqNewRow = new ServiceRequestTableRow(ch,
    // value.getRequestType(), "value.date()", "value.status()",value.getUl(),
    // "value.getEmployeeAssigned()");
    //    });

    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void editData(ActionEvent event) throws IOException {

    // Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException {}
}
