package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.Kiosk;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class KioskSetupController {
  @FXML private MFXFilterComboBox<String> locationBox;
  @FXML private MFXToggleButton leftRightToggle, directionOnOff;
  @FXML private MFXFilterComboBox<String> moveLocation;
  @FXML private MFXDatePicker moveDate;
  @FXML private TextArea moveDescription;
  @FXML private Label left, right, leftD, rightD;
  @FXML private StackPane reminderPane;
  @FXML private Text reminder;

  public static Kiosk kiosk = new Kiosk(null, null, "", "", false, "");

  @FXML
  public void initialize() {
    System.out.println("Gets here");
    reminder.setVisible(false);
    reminderPane.setVisible(false);
    List<LocationNameEntity> temp = FacadeRepository.getInstance().getAllLocation();
    ObservableList<String> locations = FXCollections.observableArrayList();
    for (LocationNameEntity move : temp) {
      locations.add(move.getLongname());
    }

    Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);
    locationBox.setItems(locations);
    moveLocation.setItems(locations);

    // Listener for left-right toggle
    leftRightToggle
        .selectedProperty()
        .addListener(
            Observable -> {
              switchLeftRight();
            });
    locationBox
        .selectedItemProperty()
        .addListener(
            Observable -> {
              setLeftRight();
            });
  }

  @FXML
  public void clear() {
    locationBox.clear();
    leftRightToggle.setSelected(false);
    directionOnOff.setSelected(false);
    moveLocation.clear();
    moveDate.clear();
    moveDescription.clear();
    left.setText("Left");
    right.setText("right");
  }

  @FXML
  public void setLeftRight() {
    // Code to get left and right nodes (adjacent nodes).
    ArrayList<String> temp = new ArrayList<String>();
    if (locationBox.getText() == null) {
      temp.add("");
      temp.add("");
    } else {
      temp = FacadeRepository.getInstance().getAdjacentLocations(locationBox.getText());
    }

    left.setText(temp.get(0));
    right.setText(temp.get(temp.size() - 1));
  }

  @FXML
  public void switchLeftRight() {
    String temp = left.getText();
    left.setText(right.getText());
    right.setText(temp);
  }

  @FXML
  public void switchToHomeScene() {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    if (holder.getJob().equalsIgnoreCase("Maintenance")) {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
    } else if (holder.getJob().equalsIgnoreCase("Admin")) {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
    } else {
      Navigation.navigateHome(Screen.HOME_ACTUAL);
    }
    Navigation.navigateHome(Screen.HOME_ACTUAL);
  }

  @FXML
  public void generateKiosk() {
    if (locationBox.getText() == null
        || moveLocation.getText() == null
        || moveDate.getValue() == null
        || moveDescription.getText() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      // Code to check if the move entered is valid.
      kiosk =
          new Kiosk(
              null,
              null,
              left.getText(),
              right.getText(),
              directionOnOff.isSelected(),
              moveDescription.getText());
      Navigation.navigateHome(Screen.KIOSK);
    }
  }
}
