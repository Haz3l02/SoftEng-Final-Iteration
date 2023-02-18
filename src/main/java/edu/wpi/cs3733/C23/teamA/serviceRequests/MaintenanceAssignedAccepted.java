package edu.wpi.cs3733.C23.teamA.serviceRequests;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import java.util.List;

public class MaintenanceAssignedAccepted {

  String name;
  int numAssigned;
  int numAccepted;

  public MaintenanceAssignedAccepted(String name) {
    this.name = name;
    List<ServiceRequestEntity> sre =
        FacadeRepository.getInstance().getServiceRequestByAssigned(name);
    for (ServiceRequestEntity sr : sre) {
      if (sr.getStatus() == Status.PROCESSING) {
        this.numAccepted++;
      }
    }
    numAssigned = sre.size();
  }
}
