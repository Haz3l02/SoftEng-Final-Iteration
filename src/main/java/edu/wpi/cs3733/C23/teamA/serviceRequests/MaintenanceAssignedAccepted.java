package edu.wpi.cs3733.C23.teamA.serviceRequests;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class MaintenanceAssignedAccepted {

  @Getter @Setter String name;
  @Getter @Setter int employeeID;
  @Getter @Setter String numAssigned;
  @Getter @Setter String numAccepted;

  public MaintenanceAssignedAccepted(String name, int employeeID) {
    this.name = name;
    this.employeeID = employeeID;
    int temp = 0;
    List<ServiceRequestEntity> sre =
        FacadeRepository.getInstance().getServiceRequestByAssigned(employeeID);
    for (ServiceRequestEntity sr : sre) {
      if (sr.getStatus() == Status.PROCESSING) {
        temp++;
      }
    }
    numAccepted = Integer.toString(temp);
    numAssigned = Integer.toString(sre.size());
  }
}
