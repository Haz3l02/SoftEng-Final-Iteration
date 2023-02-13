package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.enums.Job;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
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

public class EmployeeController {

  @FXML private TableView<EmployeeEntity> employeeTable;
  @FXML public TableColumn<EmployeeEntity, String> nameCol;
  @FXML public TableColumn<EmployeeEntity, String> employeeCol;
  @FXML public TableColumn<EmployeeEntity, String> usernameCol;
  @FXML public TableColumn<EmployeeEntity, String> passwordCol;
  @FXML public TableColumn<EmployeeEntity, String> jobCol;

  // text boxes for editing
  @FXML public MFXTextField IDNumBox;
  @FXML public MFXTextField nameBox;
  @FXML public MFXTextField usernameBox;
  @FXML public MFXTextField passwordBox;
  @FXML public MFXComboBox<String> jobBox;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton createEmployee;

  private String hospitalID;
  private String job;

  private ObservableList<EmployeeEntity> dbTableRowsModel = FXCollections.observableArrayList();
  EmployeeImpl employee = new EmployeeImpl();
  List<EmployeeEntity> employeeData = new ArrayList<>();

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeDatabase(ActionEvent event) throws IOException {
    Navigation.navigateHome(Screen.HOME_DATABASE);
  }

  @FXML
  public void initialize() throws SQLException {

    IdNumberHolder holder = IdNumberHolder.getInstance();
    hospitalID = holder.getId();
    job = holder.getJob();

    employeeData = employee.getAll();

    // Assign permissions to differentiate between medical and non-medical staff
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeid"));
    usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
    passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
    jobCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getJob()));

    dbTableRowsModel.addAll(employeeData);
    employeeTable.setItems(dbTableRowsModel);
  }

  @FXML
  public void rowClicked(MouseEvent event) {

    EmployeeEntity clickedEmployeeTableRow = employeeTable.getSelectionModel().getSelectedItem();

    if (clickedEmployeeTableRow != null) {
      nameBox.setText(String.valueOf(clickedEmployeeTableRow.getName()));
      IDNumBox.setText(String.valueOf(clickedEmployeeTableRow.getEmployeeid()));
      usernameBox.setText(String.valueOf(clickedEmployeeTableRow.getUsername()));
      passwordBox.setText(String.valueOf(clickedEmployeeTableRow.getPassword()));
      jobBox.setText(String.valueOf(clickedEmployeeTableRow.getJob()));
    }
    editButton.setDisable(false);
    deleteButton.setDisable(false);
    createEmployee.setVisible(false);

    ObservableList<String> jobs = FXCollections.observableArrayList(Job.jobList());
    jobBox.setItems(jobs);
  }

  @FXML
  public void createEmployee(ActionEvent event) {
    EmployeeEntity theEmployee =
        new EmployeeEntity(
            IDNumBox.getText(),
            usernameBox.getText(),
            passwordBox.getText(),
            jobBox.getText(),
            nameBox.getText());

    employee.add(theEmployee);
    reloadData();
  }

  @FXML
  public void delete(ActionEvent event) {
    String currentRowId = IDNumBox.getText();
    employee.delete(currentRowId);
    reloadData();
  }

  public void reloadData() {
    dbTableRowsModel.clear();
    try {
      employeeData = employee.getAll();
      dbTableRowsModel.addAll(employeeData);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void submitEdit(ActionEvent event) throws IOException, SQLException, ParseException {

    if (!IDNumBox.getText().trim().isEmpty()
        && !nameBox.getText().trim().isEmpty()
        && !usernameBox.getText().trim().isEmpty()
        && !passwordBox.getText().trim().isEmpty()) {

      ObservableList<EmployeeEntity> currentTableData = employeeTable.getItems();

      String currentRowId = IDNumBox.getText();

      for (EmployeeEntity employees : currentTableData) {
        if (employees.getEmployeeid().equals(currentRowId)) {

          employees.setName(nameBox.getText());
          employees.setUsername(usernameBox.getText());
          employees.setPassword(passwordBox.getText());
          employees.setJob(Job.value(jobBox.getValue()).getJob());
          employees.setEmployeeid(IDNumBox.getText());
          employee.update(currentRowId, employees);
          employeeTable.setItems(currentTableData);
          reloadData();
          break;
        }
      }
    }
  }

  @FXML
  public void submitRequest(ActionEvent event) {}

  public void clearEdits(ActionEvent event) {
    nameBox.clear();
    IDNumBox.clear();
    usernameBox.clear();
    passwordBox.clear();
    jobBox.clear();

    createEmployee.setVisible(true);
    editButton.setDisable(true);
  }
}
