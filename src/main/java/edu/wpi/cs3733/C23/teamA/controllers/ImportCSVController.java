package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ImportCSVController {
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXTextField employeeField, nodeField, moveField;
  @FXML private MFXButton cancel;

  @FXML
  public void initialize() {
    reminder.setVisible(false);
    reminderPane.setVisible(false);
    employeeField.setDisable(true);
    nodeField.setDisable(true);
    moveField.setDisable(true);
  }

  @FXML
  public void openFileExplorerEmployee(ActionEvent event) {
    FileChooser fc = new FileChooser();
    File selectedFile = fc.showOpenDialog(null);
    if (selectedFile != null) {
      employeeField.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  public void openFileExplorerNode(ActionEvent event) {
    FileChooser fc = new FileChooser();
    File selectedFile = fc.showOpenDialog(null);
    if (selectedFile != null) {
      nodeField.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  public void openFileExplorerMove(ActionEvent event) {
    FileChooser fc = new FileChooser();
    File selectedFile = fc.showOpenDialog(null);
    if (selectedFile != null) {
      moveField.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  public void cancel(ActionEvent event) {
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  @FXML
  void clearForm(ActionEvent event) {
    employeeField.clear();
    nodeField.clear();
    moveField.clear();
  }

  @FXML
  public void importCSV(ActionEvent event) throws IOException {
    if (employeeField.getText().equals("")
        || nodeField.getText().equals("")
        || moveField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      // if (iecsv.getTableType().equals("employee")) {
      FacadeRepository.getInstance().importEmployee(employeeField.getText());
      System.out.println("Import employee works");
      // Navigation.navigate(Screen.EMPLOYEE);
      // } else if (iecsv.getTableType().equals("node")) {
      FacadeRepository.getInstance().importNode(nodeField.getText());
      System.out.println("Import node works");
      // Navigation.navigate(Screen.NODE);
      // } else if (iecsv.getTableType().equals("move")) {
      // locationName.importFromCSV("locationname");
      // FacadeRepository.getInstance().importNode("nodes");
      FacadeRepository.getInstance().importMove(moveField.getText());
      System.out.println("Import move works");
      // Navigation.navigate(Screen.MOVE);
    }
  }
}
