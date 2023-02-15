package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.HomeDatabaseController.iecsv;

import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.ServiceRequestImpl;
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
  @FXML private MFXTextField fileNameField;
  @FXML private MFXButton cancel;
  EmployeeImpl employee = new EmployeeImpl();
  NodeImpl node = new NodeImpl();
  MoveImpl move = new MoveImpl();
  ServiceRequestImpl sri = new ServiceRequestImpl();

  @FXML
  public void initialize() {
    reminder.setVisible(false);
    reminderPane.setVisible(false);
  }

  @FXML
  public void openFileExplorer(ActionEvent event) {
    // we need this to fucking work so save the damn changes
    System.out.println("adfsgbdf");
    FileChooser fc = new FileChooser();
    File selectedDirectory = fc.showOpenDialog(null);
    if (selectedDirectory != null) {
      fileNameField.setText(selectedDirectory.getAbsolutePath());
    }
  }

  public void doStuff() {
    System.out.println("hehe");
  }

  @FXML
  public void cancel(ActionEvent event) {
    if (iecsv.getTableType().equals("employee")) {
      Navigation.navigate(Screen.EMPLOYEE);
    } else if (iecsv.getTableType().equals("node")) {
      Navigation.navigate(Screen.NODE);
    } else if (iecsv.getTableType().equals("move")) {
      Navigation.navigate(Screen.MOVE);
    } else if (iecsv.getTableType().equals("status")) {
      Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
    }
  }

  @FXML
  public void clearForm(ActionEvent event) {
    fileNameField.clear();
  }

  @FXML
  public void importCSV(ActionEvent event) throws IOException {

    if (fileNameField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);

      if (iecsv.getTableType().equals("employee")) {
        employee.importFromCSV(fileNameField.getText());
        Navigation.navigate(Screen.EMPLOYEE);
      } else if (iecsv.getTableType().equals("node")) {
        node.importFromCSV(fileNameField.getText());
        Navigation.navigate(Screen.NODE);
      } else if (iecsv.getTableType().equals("move")) {
        move.importFromCSV(fileNameField.getText());
        Navigation.navigate(Screen.MOVE);
      } else {
        sri.importFromCSV(fileNameField.getText());
        Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
      }
    }
  }
}
