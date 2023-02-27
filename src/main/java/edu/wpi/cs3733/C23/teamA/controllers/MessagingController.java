package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class MessagingController extends NavigationController{

    @FXML TableView messagesTable;

    @FXML TableColumn senderCol;
    @FXML TableColumn timeSentCol;
    @FXML TableColumn messageCol;
    @FXML MFXButton newMessageButton;

    PopOver newMessagePopup;

    @FXML
    public void initialize(){

        // initialize the tables


    }
    @FXML
    public void refreshTable(ActionEvent event) {
    }

    @FXML
    public void popupNewMessage(ActionEvent event) throws IOException {

        FXMLLoader locationLoader =
                new FXMLLoader(Main.class.getResource("views/LocationEditorPopupFXML.fxml"));

        newMessagePopup = new PopOver(locationLoader.load());
        newMessagePopup.show(newMessageButton.getScene().getWindow());

        //newMessagePopup.setAnchorX();
        //newMessagePopup.setAnchorY();

    }

    @FXML
    public void rowClicked(MouseEvent mouseEvent) {
    }
}
