package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.HomeDatabaseController.iecsv;

import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
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

  @FXML
  public void initialize() {
    reminder.setVisible(false);
    reminderPane.setVisible(false);
  }

  @FXML
  public void openFileExplorer(ActionEvent event) {
    FileChooser fc = new FileChooser();
    File selectedFile = fc.showOpenDialog(null);
    if (selectedFile != null) {
      fileNameField.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  public void cancel(ActionEvent event) {
    if (iecsv.getTableType().equals("employee")) {
      Navigation.navigate(Screen.EMPLOYEE);
    } else if (iecsv.getTableType().equals("node")) {
      Navigation.navigate(Screen.NODE);
    } else if (iecsv.getTableType().equals("move")) {
      Navigation.navigate(Screen.MOVE);
    }
  }

  @FXML
  void clearForm(ActionEvent event) {
    fileNameField.clear();
  }

  @FXML
  public void importCSV(ActionEvent event) throws FileNotFoundException {
    if (fileNameField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      if (iecsv.getTableType().equals("employee")) {
        employee.importFromCSV(fileNameField.getText());
        System.out.println("Import employee works");
        Navigation.navigate(Screen.EMPLOYEE);
      } else if (iecsv.getTableType().equals("node")) {
        node.importFromCSV(fileNameField.getText());
        System.out.println("Import node works");
        Navigation.navigate(Screen.NODE);
      } else if (iecsv.getTableType().equals("move")) {
        // locationName.importFromCSV("locationname");
        node.importFromCSV("nodes");
        move.importFromCSV(fileNameField.getText());
        System.out.println("Import move works");
        Navigation.navigate(Screen.MOVE);
      }
    }
  }
}
