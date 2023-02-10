package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.ServiceRequestStatusController.newEdit;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.enums.RequestCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.serviceRequests.IdNumberHolder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SecurityController extends ServiceRequestController {

  @FXML private MFXTextField phone;
  @FXML private MFXComboBox<String> requestsBox;

  UrgencyLevel urgent;
  RequestCategory assistance;

  @FXML
  public void initialize() throws SQLException {
    IdNumberHolder holder = IdNumberHolder.getInstance();
    String name = holder.getName();
    String id = holder.getId();

    if (reminder != null) {
      reminder.setVisible(false);
      reminderPane.setVisible(false);
    }
    if (requestsBox != null) {
      ObservableList<String> requests =
          FXCollections.observableArrayList(RequestCategory.categoryList());
      ObservableList<String> urgencies =
          FXCollections.observableArrayList(UrgencyLevel.urgencyList());

      Session session = getSessionFactory().openSession();
      List<LocationNameEntity> temp = getAllRecords(LocationNameEntity.class, session);

      // ArrayList<Move> moves = Move.getAll();
      ObservableList<String> locations = FXCollections.observableArrayList();
      for (LocationNameEntity move : temp) {
        locations.add(move.getLongname());
      }

      Collections.sort(locations, String.CASE_INSENSITIVE_ORDER);

      nameBox.setText(name);
      IDNum.setText(id);

      requestsBox.setItems(requests);
      urgencyBox.setItems(urgencies);
      locationBox.setItems(locations);
    }
    if (newEdit.needEdits && newEdit.getRequestType().equals("SECURITY")) {

      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      SecurityRequestEntity editRequest =
          session.get(SecurityRequestEntity.class, newEdit.getRequestID());
      nameBox.setText(editRequest.getName());
      IDNum.setText(editRequest.getEmployee().getEmployeeid());
      requestsBox.setText(editRequest.getRequestType().requestType);
      locationBox.setText(editRequest.getLocation().getLongname());
      urgencyBox.setText(editRequest.getUrgency().getUrgency());
      descBox.setText(editRequest.getDescription());
      phone.setText(editRequest.getSecphone());
      tx.commit();
      session.close();
    }
  }

  @FXML
  public void switchToConfirmationScene(ActionEvent event) {
    Navigation.navigate(Screen.SECURITY_CONFIRMATION);
  }

  @FXML
  public void switchToSecurityScene(ActionEvent event) {
    Navigation.navigate(Screen.SECURITY);
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  @FXML
  public void switchToHomeServiceRequestScene(ActionEvent event) {
    Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  void submitRequest(ActionEvent event) {
    if (nameBox.getText().equals("")
        || phone.getText().equals("")
        || IDNum.getText().equals("")
        || locationBox.getValue() == null
        || descBox.getText().equals("")
        || requestsBox.getValue() == null
        || urgencyBox.getValue() == null) {
      reminder.setVisible(true);
      reminderPane.setVisible(true);
    } else {
      if (newEdit.needEdits) {
        // something that submits it

        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        urgent = UrgencyLevel.value(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue().toUpperCase());

        SecurityRequestEntity submission =
            session.get(SecurityRequestEntity.class, newEdit.getRequestID());
        submission.setName(nameBox.getText());
        LocationNameEntity loc = session.get(LocationNameEntity.class, locationBox.getValue());
        submission.setLocation(loc);
        submission.setDescription(descBox.getText());
        submission.setUrgency(urgent);
        submission.setAssistance(assistance);
        submission.setSecphone(phone.getText());

        session.persist(submission);

        tx.commit();
        session.close();
      } else {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        EmployeeEntity person = session.get(EmployeeEntity.class, IDNum.getText());
        LocationNameEntity location = session.get(LocationNameEntity.class, locationBox.getText());

        urgent = UrgencyLevel.value(urgencyBox.getValue().toUpperCase());
        assistance = RequestCategory.value(requestsBox.getValue());

        SecurityRequestEntity submission =
            new SecurityRequestEntity(
                nameBox.getText(),
                person,
                location,
                descBox.getText(),
                urgent,
                ServiceRequestEntity.RequestType.SECURITY,
                Status.BLANK,
                "Unassigned",
                assistance,
                phone.getText());
        session.persist(submission);
        tx.commit();
        session.close();
      }

      newEdit.setNeedEdits(false);
      switchToConfirmationScene(event);
    }
  }

  @FXML
  void clearForm() {
    super.clearForm();
    phone.clear();
    requestsBox.clear();
  }
}
