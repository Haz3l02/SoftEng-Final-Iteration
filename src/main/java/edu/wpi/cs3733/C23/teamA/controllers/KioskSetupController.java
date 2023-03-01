package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import edu.wpi.cs3733.C23.teamA.serviceRequests.Kiosk;
import io.github.palexdev.materialfx.controls.MFXButton;
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

public class KioskSetupController extends MenuController {
  @FXML private MFXFilterComboBox<String> locationBox;
  @FXML private MFXToggleButton leftRightToggle, directionOnOff;
  @FXML private MFXFilterComboBox<String> moveLocation;
  @FXML private MFXDatePicker moveDate;
  @FXML private TextArea moveDescription;
  @FXML private Label left, right;
  @FXML private StackPane reminderPane;
  @FXML private Text reminder, moveReminder;
  @FXML private MFXButton generateButton;

  public static Kiosk kiosk;

  @FXML
  public void initialize() {
    generateButton.setDisable(true);
    moveDate.setDisable(true);
    reminder.setVisible(false);
    reminderPane.setVisible(false);
    moveReminder.setVisible(false);
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
    moveLocation
        .selectedItemProperty()
        .addListener(
            (Observable -> {
              moveDate.setDisable(false);
            }));
    moveDate
        .valueProperty()
        .addListener(
            Observable -> {
              List<MoveEntity> moves =
                  FacadeRepository.getInstance()
                      .newAndOldMove(moveLocation.getText(), moveDate.getValue());
              if (moves.size() == 1
                  || moves.get(0).getNode() == null
                  || moves.get(1).getNode() == null) {
                reminderPane.setVisible(true);
                moveReminder.setVisible(true);
              } else {
                reminderPane.setVisible(false);
                moveReminder.setVisible(false);
                if (moves.get(0).getMessage() == null) {
                  moveDescription.setText("No message");
                } else {
                  moveDescription.setText(moves.get(0).getMessage());
                }
                generateButton.setDisable(false);
              }
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
  public void switchToNodeEditorScene() {
    Navigation.navigate(Screen.MOVE);
  }

  @FXML
  public void generateKiosk() {
    if (locationBox.getText() == null
        || moveLocation.getText() == null
        || moveDate.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      List<MoveEntity> moves =
          FacadeRepository.getInstance().newAndOldMove(moveLocation.getText(), moveDate.getValue());
      kiosk =
          new Kiosk(
              moves.get(1).getNode(),
              moves.get(0).getNode(),
              left.getText(),
              right.getText(),
              directionOnOff.isSelected(),
              moveDescription.getText(),
              moveLocation.getText(),
              moves.get(0).getMovedate());
      Navigation.navigateHome(Screen.KIOSK);
      // Code to check if the move entered is valid.

    }
  }
}
