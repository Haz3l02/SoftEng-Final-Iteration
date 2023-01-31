package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.SanitationRequest;
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

  private String id;

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  private ObservableList<ServiceRequestTableRow> dbTableRowsModel =
      FXCollections.observableArrayList(
          new ServiceRequestTableRow(0, "aa", "aaaaaaaa", "aaa", "aaaaaaa", "aaaa"));

  //  @FXML
  //  private void receiveData(ActionEvent event) {
  //    IdNumberHolder holder = IdNumberHolder.getInstance();
  //    id = holder.getId();
  //  }

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    id = holder.getId();

    IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    formTypeCol.setCellValueFactory(new PropertyValueFactory<>("formType"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
    employeeAssignedCol.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    SanitationRequest sr = new SanitationRequest();

    ArrayList<SanitationRequest> specificRequests = new ArrayList<>();
    specificRequests = sr.getSanitationRequestByUser(id); // MAKE SURE TO FIX THIS;
    // System.out.println(specificRequests.get(0).getName());

    for (SanitationRequest bla : specificRequests) {
      ServiceRequestTableRow serviceReqNewRow =
          new ServiceRequestTableRow(
              bla.getRequestID(),
              bla.getRequestType(),
              "date",
              "status",
              bla.getUl(),
              "assignedEmployee");
      dbTableRowsModel.add(serviceReqNewRow);
    }

    serviceReqsTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

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
  public void submitEdit(ActionEvent event) throws IOException {

    ObservableList<ServiceRequestTableRow> currentTableData = serviceReqsTable.getItems();
    int currentAnimalId = Integer.parseInt(IDBoxSaver.getText());

    for (ServiceRequestTableRow SRTable : currentTableData) {
      if (SRTable.getID() == currentAnimalId) {
        SRTable.setID(Integer.parseInt(IDBoxSaver.getText()));
        SRTable.setFormType(formTypeBox.getText());
        SRTable.setDate(dateBox.getText());
        SRTable.setStatus(statusBox.getText());
        SRTable.setUrgency(urgencyBox.getText());
        SRTable.setEmployeeAssigned(employeeBox.getText());

        serviceReqsTable.setItems(currentTableData);
        serviceReqsTable.refresh();
        break;
      }
    }
  }

  @FXML
  public void submitRequest(ActionEvent event) throws IOException {}
}
