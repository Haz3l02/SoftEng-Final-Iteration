package edu.wpi.cs3733.C23.teamA.serviceRequests;

import java.awt.*;
import lombok.Getter;
import lombok.Setter;

@Deprecated
public class ServiceRequestTableRow {

  @Getter @Setter private int ID;
  @Getter @Setter private String formType;
  @Getter @Setter private String date;
  @Getter @Setter private String status;
  @Getter @Setter private String urgency;
  @Getter @Setter private String employeeAssigned;

  public ServiceRequestTableRow(
      int ID,
      String formType,
      String date,
      String status,
      String urgency,
      String employeeAssigned) {

    this.ID = ID;
    this.date = date;
    this.formType = formType;
    this.status = status;
    this.urgency = urgency;
    this.employeeAssigned = employeeAssigned;
  }
}
