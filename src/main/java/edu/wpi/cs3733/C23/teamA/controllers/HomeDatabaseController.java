package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.PopOver;

public class HomeDatabaseController extends MenuController {
  @FXML TableView<MoveEntity> moveTable;
  @FXML TableColumn<MoveEntity, String> moveCol;
  @FXML TableColumn<MoveEntity, String> destinationCol;
  @FXML TableColumn<MoveEntity, String> dateCol;
  List<MoveEntity> data;

  private ObservableList<MoveEntity> dbTableRowsModel = FXCollections.observableArrayList();

  @Override
  public void initialize() throws SQLException, IOException, InterruptedException {
    HashMap<MoveEntity, MoveEntity> vector =
        FacadeRepository.getInstance()
            .getLocationChanges(LocalDate.now(), LocalDate.now().plusWeeks((long) 1.0));
    data = vector.keySet().stream().toList();
    dbTableRowsModel.addAll(data);

    moveCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getLocationName().getLongname()));
    destinationCol.setCellValueFactory(
        param ->
            new SimpleStringProperty(vector.get(param.getValue()).getLocationName().getLongname()));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("movedate"));

    moveTable.setItems(dbTableRowsModel);
  }

  public void switchToCredits(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/CreditsFXML.fxml"));
    PopOver creditsPopUp = new PopOver(loader.load());
    creditsPopUp.show(((Node) event.getSource()).getScene().getWindow());
  }
}
