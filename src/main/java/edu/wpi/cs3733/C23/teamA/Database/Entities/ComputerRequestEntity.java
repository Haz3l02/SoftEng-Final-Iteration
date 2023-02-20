package edu.wpi.cs3733.C23.teamA.Database.Entities;

import edu.wpi.cs3733.C23.teamA.enums.DevicesCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "computerrequest")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class ComputerRequestEntity extends ServiceRequestEntity {

  //  @Id
  //  @Column(name = "requestid", nullable = false)
  //  @Getter
  //  @Setter
  //  private int requestid;

  @Basic
  @Column(name = "deviceid", nullable = false, length = -1)
  @Getter
  @Setter
  private String deviceid;

  @Basic
  @Column(name = "device", nullable = false, length = -1)
  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private DevicesCategory device;

  public ComputerRequestEntity() {}

  public ComputerRequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      String deviceid,
      DevicesCategory device,
      Timestamp date) {
    super(
        requestid,
        name,
        employee,
        location,
        description,
        urgency,
        requesttype,
        status,
        employeeassigned,
        date);
    this.deviceid = deviceid;
    this.device = device;
  }

  public ComputerRequestEntity(
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      String deviceid,
      DevicesCategory device) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.deviceid = deviceid;
    this.device = device;
  }
}
