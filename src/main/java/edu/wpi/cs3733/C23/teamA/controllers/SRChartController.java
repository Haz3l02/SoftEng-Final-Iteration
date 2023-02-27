package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javax.swing.*;

public class SRChartController extends MenuController {

  @FXML private BarChart srChart;

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

    XYChart.Series count = new XYChart.Series<>();

    count.setName("# of Requests");
    count.getData().add(new XYChart.Data("Accessability", accessCount));
    count.getData().add(new XYChart.Data("Audio/Visual", avCount));
    count.getData().add(new XYChart.Data("Computer", compCount));
    count.getData().add(new XYChart.Data("Patient Transportation", patCount));
    count.getData().add(new XYChart.Data("Sanitation", sanCount));
    count.getData().add(new XYChart.Data("Security", secCount));

    // adding all
    srChart.getData().addAll(count);
  }

  @FXML
  public void back() {
    switchToServiceRequestStatus();
  }
}
