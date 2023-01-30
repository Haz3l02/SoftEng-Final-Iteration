package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.SanitationRequest;
import edu.wpi.cs3733.C23.teamA.ServiceRequestTableRow;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
  private String id;

  private CheckBox aCheckbox = new CheckBox("");

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  private ObservableList<ServiceRequestTableRow> dbTableRowsModel =
      FXCollections.observableArrayList(
          new ServiceRequestTableRow(aCheckbox, "aaa", "aaaaaaaa", "aaa", "aaaaaaa", "aaaa"));

  //  @FXML
  //  private void receiveData(ActionEvent event) {
  //    IdNumberHolder holder = IdNumberHolder.getInstance();
  //    id = holder.getId();
  //  }

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    id = holder.getId();

    checkbox.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    SanitationRequest sr = new SanitationRequest();

    ArrayList<SanitationRequest> specificRequests = new ArrayList<>();
    specificRequests = sr.getSanitationRequestByUser(id); // MAKE SURE TO FIX THIS;
    System.out.println(specificRequests.get(0).getName());

    for (SanitationRequest bla : specificRequests) {
      CheckBox ch = new CheckBox("");
      ServiceRequestTableRow serviceReqNewRow =
          new ServiceRequestTableRow(
              ch, bla.getRequestType(), "date", "status", bla.getUl(), "assignedEmployee");
      dbTableRowsModel.add(serviceReqNewRow);
    }

    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void editData(ActionEvent event) throws IOException {

    // Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException {}
}
