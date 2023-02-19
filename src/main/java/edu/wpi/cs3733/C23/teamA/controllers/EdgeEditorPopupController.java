package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class EdgeEditorPopupController {

  @FXML MFXTextField node1;
  @FXML MFXTextField node2;

  @FXML
  public void initialize() {}

  @FXML
  public void addEdge(ActionEvent actionEvent) {

    EdgeEntity newEdge = new EdgeEntity();

    newEdge.setNode1(FacadeRepository.getInstance().getNode(node1.getText()));
    newEdge.setNode2(FacadeRepository.getInstance().getNode(node2.getText()));

    FacadeRepository.getInstance().addEdge(newEdge);

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
    MapEditorController.mapEditor.closePopup("edge");

    Navigation.navigate(Screen.NODE_MAP);
  }

  @FXML
  public void hidePopup(ActionEvent event) {
    mapEditor.closePopup("edge");
  }

  @FXML
  public void editEdge(ActionEvent actionEvent) {}

  @FXML
  public void deleteEdge(ActionEvent actionEvent) {}
}
