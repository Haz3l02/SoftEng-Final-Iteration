package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.iecsv;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class ExportCSVController {
  @FXML private Text reminder;
  @FXML private StackPane reminderPane;
  @FXML private MFXTextField fileNameField;
  @FXML private MFXButton cancel;
  @FXML private MFXCheckbox checkBox;

  @FXML
  public void initialize() {
    reminder.setVisible(false);
    reminderPane.setVisible(false);
  }

  @FXML
  public void openFileExplorer(ActionEvent event) {
    DirectoryChooser dc = new DirectoryChooser();
    File selectedDirectory = dc.showDialog(null);
    if (selectedDirectory != null) {
      fileNameField.setText(selectedDirectory.getAbsolutePath());
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
    } else if (iecsv.getTableType().equals("status")) {
      Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
    }
  }

  @FXML
  public void clearForm(ActionEvent event) {
    fileNameField.clear();
  }

  @FXML
  public void exportCSV(ActionEvent event) throws IOException {

    if (fileNameField.getText().equals("")) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
      if (checkBox.isSelected()) {
        FacadeRepository.getInstance().exportEmployee(fileNameField.getText());
        FacadeRepository.getInstance().exportNode(fileNameField.getText());
        FacadeRepository.getInstance().exportMove(fileNameField.getText());
        FacadeRepository.getInstance().exportService(fileNameField.getText());
      } else {
        if (iecsv.getTableType().equals("employee")) {
          FacadeRepository.getInstance().exportEmployee(fileNameField.getText());
          Navigation.navigate(Screen.EMPLOYEE);
        } else if (iecsv.getTableType().equals("node")) {
          FacadeRepository.getInstance().exportNode(fileNameField.getText());
          Navigation.navigate(Screen.NODE);
        } else if (iecsv.getTableType().equals("move")) {
          FacadeRepository.getInstance().exportMove(fileNameField.getText());
          Navigation.navigate(Screen.MOVE);
        } else {
          FacadeRepository.getInstance().exportEmployee(fileNameField.getText());
          Navigation.navigate(Screen.SERVICE_REQUEST_STATUS);
        }
      }
    }
  }
}
