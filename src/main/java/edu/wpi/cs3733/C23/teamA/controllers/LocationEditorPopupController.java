package edu.wpi.cs3733.C23.teamA.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LocationEditorPopupController {

  @FXML MFXFilterComboBox<String> longNameBox;
  @FXML MFXTextField locationIDBox;

  @FXML
  public void addLocation(ActionEvent actionEvent) {}

  @FXML
  public void editLocation(ActionEvent actionEvent) {}

  @FXML
  public void deleteLocation(ActionEvent actionEvent) {}
}
