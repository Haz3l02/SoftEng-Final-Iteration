package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.ImportExportCSV;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.*;
import org.controlsfx.control.PopOver;

public class HomeDatabaseController extends MenuController {
  @FXML TableView<MoveEntity> moveTable;
  @FXML TableColumn<MoveEntity, String> moveCol;
  @FXML TableColumn<MoveEntity, String> destinationCol;
  @FXML TableColumn<MoveEntity, String> dateCol;
  @FXML ComboBox<String> serviceRequest = new ComboBox<>();
  @FXML ComboBox<String> admin = new ComboBox<>();

  List<MoveEntity> data;

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();
  public static ImportExportCSV iecsv = new ImportExportCSV("");

  @Override
  public void initialize() throws SQLException, IOException, InterruptedException {
    //    HashMap<MoveEntity, MoveEntity> vector =
    //        FacadeRepository.getInstance()
    //            .getLocationChanges(LocalDate.now(), LocalDate.now().plusWeeks((long) 1.0));
    //    data = vector.keySet().stream().toList();
    //    dbTableRowsModel.addAll(data);
    //
    //    moveCol.setCellValueFactory(
    //        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    //    destinationCol.setCellValueFactory(
    //        param ->
    //            new
    // SimpleStringProperty(vector.get(param.getValue()).getLocationName().getLongname()));
    //    dateCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));
    //
    //    moveTable.setItems(dbTableRowsModel);
    admin
        .getItems()
        .addAll("Map Editor", "Access Employee Records", "Department Moves", "Create Nodes");
    serviceRequest
        .getItems()
        .addAll(
            "Sanitation",
            "Security",
            "Computer",
            "Patient Transportation",
            "Audio/Visual Request",
            "Accessibility");
  }

  public void switchToEdgeScene(ActionEvent event) {
    Navigation.navigate(Screen.EDGE);
  }

  public void switchToNodeScene(ActionEvent event) {
    iecsv = new ImportExportCSV("node");
    Navigation.navigate(Screen.NODE);
  }

  public void switchToMapScene(ActionEvent event) {
    Navigation.navigate(Screen.NODE_MAP);
  }

  public void switchToMoveScene(ActionEvent event) {
    iecsv = new ImportExportCSV("move");
    Navigation.navigate(Screen.MOVE);
  }

  public void switchToEmployeeScene(ActionEvent event) {
    iecsv = new ImportExportCSV("employee");
    Navigation.navigate(Screen.EMPLOYEE);
  }

  public void switchToCredits(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/CreditsFXML.fxml"));
    PopOver creditsPopUp = new PopOver(loader.load());
    creditsPopUp.show(((Node) event.getSource()).getScene().getWindow());
  }
}
