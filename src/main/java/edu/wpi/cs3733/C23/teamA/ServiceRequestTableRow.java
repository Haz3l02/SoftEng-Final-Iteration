package edu.wpi.cs3733.C23.teamA;

import java.awt.*;
import javafx.scene.control.CheckBox;
import lombok.Getter;
import lombok.Setter;

public class ServiceRequestTableRow {

  @Getter @Setter private CheckBox checkbox;
  @Getter @Setter private String formType;
  @Getter @Setter private String date;
  @Getter @Setter private String status;
  @Getter @Setter private String urgency;
  @Getter @Setter private String employeeAssigned;

  public ServiceRequestTableRow(
      CheckBox checkbox,
      String formType,
      String date,
      String status,
      String urgency,
      String employeeAssigned) {

    this.checkbox = checkbox;
    this.date = date;
    this.formType = formType;
    this.status = status;
    this.urgency = urgency;
    this.employeeAssigned = employeeAssigned;
  }
}
