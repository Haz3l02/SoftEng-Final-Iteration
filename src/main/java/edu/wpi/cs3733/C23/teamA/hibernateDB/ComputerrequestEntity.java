package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Entity
@Table(name = "computerrequest", catalog = "dba")
@PrimaryKeyJoinColumn(name = "requestid_FK", foreignKey = @ForeignKey(name = "requestid_FK"))
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
      Urgency urgency,
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
        urgency,
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
      Urgency urgency,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      String deviceid,
      Device device) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.deviceid = deviceid;
    this.device = device;
  }

  public void editComputerRequest(
      int requestId,
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String deviceID,
      String device) {
    // administrative stuff
    SessionFactory sessionFactory = ADBSingletonClass.getSessionFactory();
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    ComputerrequestEntity comRqst = Adb.getInstance().load(ComputerrequestEntity.class, 1);
    comRqst.setName(name);
    comRqst.setName(idNum);
    comRqst.setName(location);
    comRqst.setName(description);
    comRqst.setName(ul);
    comRqst.setName(requestType);
    comRqst.setName(status);
    comRqst.setName(employeeAssigned);
    comRqst.setName(description);
    comRqst.setName(deviceID);
    comRqst.setName(device);

    tx.commit();
    session.close();
  }
}
