package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MessageBoardEntity;
import edu.wpi.cs3733.C23.teamA.Main;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;

public class MessagingController extends NavigationController {

  @FXML TableView<MessageBoardEntity> messagesTable;

  @FXML TableColumn<MessageBoardEntity, String> senderCol;
  @FXML TableColumn<MessageBoardEntity, String> timeSentCol;
  @FXML TableColumn<MessageBoardEntity, String> messageCol;
  @FXML TableColumn<MessageBoardEntity, String> titleCol;
  @FXML TableColumn<MessageBoardEntity, String> recipientCol;

  @FXML MFXButton newMessageButton;
  @FXML MFXButton deleteMessageButton;
  @FXML AnchorPane anchorPane;

  static PopOver newMessagePopup;
  static double mouseX;
  static double mouseY;

  static MessageBoardEntity selectedMessage;

  private ObservableList<MessageBoardEntity> messageBoardModel =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() {

    // initialize the tables

    /*
    senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
    timeSentCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
    titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

    messagesTable.setItems(messageBoardModel);

    // timeSentCol.setCellValueFactory(TextFieldTableCell.forTableColumn(Time));
    messageCol.setCellFactory(TextFieldTableCell.forTableColumn());
    titleCol.setCellFactory(TextFieldTableCell.forTableColumn());

     */
    // ObservableList<MessageBoardEntity> allMsgsToYou = FXCollections.observableArrayList();

    messageCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getMessage()));
    titleCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
    senderCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getSender().getUsername()));
    recipientCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getReceiver().getUsername()));
    timeSentCol.setCellValueFactory(
        param -> new SimpleStringProperty(param.getValue().getTimeSent().toString()));

    //
    messageBoardModel.setAll(
        FacadeRepository.getInstance()
            .getMessageByUser(
                FacadeRepository.getInstance()
                    .getEmployee(IdNumberHolder.getInstance().getEmployeeID())));
    messagesTable.setItems(messageBoardModel);
  }

  @FXML
  public void updateMouseCoords(MouseEvent event) {

    mouseX = event.getSceneX();
    mouseY = event.getSceneY();
  }

  @FXML
  public void refreshTable(ActionEvent event) {
    initialize();
  }

  @FXML
  public void popupNewMessage(ActionEvent event) throws IOException {

    FXMLLoader locationLoader =
        new FXMLLoader(Main.class.getResource("views/NewMessagePopupFXML.fxml"));

    newMessagePopup = new PopOver(locationLoader.load());
    newMessagePopup.show(newMessageButton.getScene().getWindow());

    newMessagePopup.setAnchorX(mouseX);
    newMessagePopup.setAnchorY(mouseY);
  }

  @FXML
  public void rowClicked(MouseEvent mouseEvent) {

    selectedMessage = messagesTable.getSelectionModel().getSelectedItem();

    deleteMessageButton.setDisable(false);
  }

  public static void hidePopup() {

    newMessagePopup.hide();
  }

  @FXML
  public void deleteMessage(ActionEvent event) {
    // sender, receiver, time sent
    List<String> msg = new ArrayList<>();
    msg.add(selectedMessage.getSender().getUsername());
    msg.add(selectedMessage.getReceiver().getUsername());
    msg.add(selectedMessage.getTimeSent().toString());

    if (selectedMessage != null) {
      FacadeRepository.getInstance().deleteMessage(msg);
    }

    deleteMessageButton.setDisable(true);
    refreshTable(event);
  }
}
