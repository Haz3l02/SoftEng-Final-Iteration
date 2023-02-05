package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
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
  @Enumerated(EnumType.STRING)
  private Device device;

  public enum Device {
    DESKTOP("Desktop"),
    TABLET("Tablet"),
    LAPTOP("Laptop"),
    PERIPHERALS("Peripherals"),
    KIOSK("Kiosk"),
    MONITOR("Monitor"),
    PRINTER("Printer");

    @NonNull public final String device;

    Device(@NonNull String device) {
      this.device = device;
    }
  }

  public ComputerrequestEntity() {}

  public ComputerrequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency ul,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      String deviceid,
      Device device,
      Date date) {
    super(
        requestid,
        name,
        employee,
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
      EmployeeEntity employee,
      String location,
      String description,
      Urgency ul,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      String deviceid,
      Device device) {
    super(name, employee, location, description, ul, requesttype, status, employeeassigned);
    this.deviceid = deviceid;
    this.device = device;
  }
}
