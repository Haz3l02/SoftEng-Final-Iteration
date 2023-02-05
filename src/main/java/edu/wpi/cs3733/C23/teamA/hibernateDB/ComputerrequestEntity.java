package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "computerrequest", catalog = "dba")
public class ComputerrequestEntity extends ServicerequestEntity {

  @Id
  @Column(name = "requestid", nullable = false)
  @Getter
  @Setter
  private int requestid;

  @Basic
  @Column(name = "deviceid", nullable = false, length = -1)
  @Getter
  @Setter
  private String deviceid;

  @Basic
  @Column(name = "device", nullable = false, length = -1)
  @Getter
  @Setter
  private String device;

  public ComputerrequestEntity() {}

  public ComputerrequestEntity(
      int requestid,
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String deviceid,
      String device,
      Date date) {
    super(
        requestid,
        name,
        employeeid,
        location,
        description,
        ul,
        requesttype,
        status,
        employeeassigned,
        date);
    this.deviceid = deviceid;
    this.device = device;
  }

  public ComputerrequestEntity(
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String deviceid,
      String device) {
    super(name, employeeid, location, description, ul, requesttype, status, employeeassigned);
    this.deviceid = deviceid;
    this.device = device;
  }
}
