package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javax.swing.*;

public class SRChartController extends MenuController {

  @FXML private BarChart srChart;
  @FXML private BarChart srStatusChart;

  @FXML
  public void initialize() throws SQLException, IOException {
    // date for amount of request by request type
    int accessCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.ACCESSIBILITY);
    int avCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.AV);
    int compCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.COMPUTER);
    int patCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.PATIENT_TRANSPORT);
    int sanCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.SANITATION);
    int secCount =
        FacadeRepository.getInstance()
            .countServiceRequestsByType(ServiceRequestEntity.RequestType.SECURITY);

    int newCount = FacadeRepository.getInstance().countServiceRequestsByStatus(Status.NEW);
    int assignedCount =
        FacadeRepository.getInstance().countServiceRequestsByStatus(Status.ASSIGNED);
    int processingCount =
        FacadeRepository.getInstance().countServiceRequestsByStatus(Status.PROCESSING);
    int doneCount = FacadeRepository.getInstance().countServiceRequestsByStatus(Status.DONE);

    XYChart.Series count = new XYChart.Series<>();

    count.setName("# of Requests");
    count.getData().add(new XYChart.Data("Accessability", accessCount));
    count.getData().add(new XYChart.Data("Audio/Visual", avCount));
    count.getData().add(new XYChart.Data("Computer", compCount));
    count.getData().add(new XYChart.Data("Patient Transportation", patCount));
    count.getData().add(new XYChart.Data("Sanitation", sanCount));
    count.getData().add(new XYChart.Data("Security", secCount));

    XYChart.Series count2 = new XYChart.Series();

    count2.setName("# of Requests");
    count2.getData().add(new XYChart.Data("New", newCount));
    count2.getData().add(new XYChart.Data("Assigned", assignedCount));
    count2.getData().add(new XYChart.Data("Processing", processingCount));
    count2.getData().add(new XYChart.Data("Done", doneCount));

    // adding all
    srChart.getData().addAll(count);
    srStatusChart.getData().addAll(count2);
  }

  @FXML
  public void back() {
    switchToServiceRequestStatus();
  }
}
