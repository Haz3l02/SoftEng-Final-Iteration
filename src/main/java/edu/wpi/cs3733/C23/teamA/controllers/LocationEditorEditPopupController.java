package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.LocationType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Setter;

public class LocationEditorEditPopupController {

  @FXML MFXTextField longNameBox;
  @FXML MFXTextField shortNameBox;
  @FXML MFXComboBox<String> locTypeBox;

  @Setter private static String longname;
  @Setter private static String shortname;
  @Setter private static String locType;
  @Setter private static LocationNameEntity locNameEntity = new LocationNameEntity();
  NodeEntity selected = new NodeEntity();

  MapEditorController MEC = new MapEditorController();

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

    longNameBox.setText(longname);
    shortNameBox.setText(shortname);
    locTypeBox.setText(locType);
    locTypeBox.setValue(locType);
    // locTypeBox.selectItem(locType);
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    mapEditor.closePopup("location edit");
  }

  @FXML
  public void saveLocEdit(ActionEvent actionEvent) {

    locNameEntity.setLocationtype(locTypeBox.getSelectedItem());
    locNameEntity.setLongname(longNameBox.getText());
    locNameEntity.setShortname(shortNameBox.getText());
    /*
       if (!Objects.equals(locNameEntity.getLongname(), longname)) {
         FacadeRepository.getInstance().addLocation(locNameEntity);

         FacadeRepository.getInstance().deleteLocation(longname);


    */

    FacadeRepository.getInstance().updateLocation(locNameEntity.getLongname(), locNameEntity);

    mapEditor.closePopup("location edit");
  }

  @FXML
  public void deleteLocation(ActionEvent event) {

    System.out.println("delete location");

    FacadeRepository.getInstance().deleteLocation(locNameEntity.getLongname());
  }
}
