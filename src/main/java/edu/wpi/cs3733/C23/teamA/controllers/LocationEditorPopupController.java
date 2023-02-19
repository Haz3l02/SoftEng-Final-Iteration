package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.LocationType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class LocationEditorPopupController {

  @FXML MFXTextField longNameBox;
  @FXML MFXTextField shortNameBox;
  @FXML MFXComboBox<String> locTypeBox;

  @FXML
  public void initialize() {

    ObservableList<String> locTypes =
        FXCollections.observableArrayList(
            LocationType.CONF.getExtendedString(),
            LocationType.DEPT.getExtendedString(),
            LocationType.ELEV.getExtendedString(),
            LocationType.EXIT.getExtendedString(),
            LocationType.HALL.getExtendedString(),
            LocationType.INFO.getExtendedString(),
            LocationType.REST.getExtendedString(),
            LocationType.LABS.getExtendedString(),
            LocationType.RETL.getExtendedString(),
            LocationType.STAI.getExtendedString(),
            LocationType.SERV.getExtendedString(),
            LocationType.CONF.getExtendedString(),
            LocationType.UNKN.getExtendedString());

    locTypeBox.setItems(locTypes);
  }

  @FXML
  public void addLocation(ActionEvent actionEvent) {

    FacadeRepository.getInstance()
        .addLocation(
            new LocationNameEntity(
                longNameBox.getText(), shortNameBox.getText(), locTypeBox.getSelectedItem()));

    // take care of last selected node
    Pane recentPane = NodeDraw.getSelectedPane();
    if (recentPane != null) {
      recentPane.setPrefSize(5, 5);
      recentPane.setStyle(
          "-fx-background-color: '#224870'; "
              + "-fx-background-radius: 12.5; "
              + "-fx-border-color: '#224870'; "
              + "-fx-border-width: 1;"
              + "-fx-border-radius: 13.5");
      //      int[] updatedCoords = NodeDraw.scaleCoordinates();
      //      recentPane.setLayoutX(updatedCoords[0] - 2.5);
      //      recentPane.setLayoutY(updatedCoords[1] - 2.5);
    }
    MapEditorController.mapEditor.closePopup("location");

    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    mapEditor.closePopup("location");
  }

  @FXML
  public void editLocation(ActionEvent actionEvent) {}

  @FXML
  public void deleteLocation(ActionEvent actionEvent) {}
}
