package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "securityrequest", catalog = "dba")
@PrimaryKeyJoinColumn(name = "requestid_FK", foreignKey = @ForeignKey(name = "requestid_FK"))
public class SecurityrequestEntity extends ServicerequestEntity {

  @Id
  @Column(name = "requestid", nullable = false)
  @Setter
  @Getter
  private int requestid;

  @Basic
  @Column(name = "request", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Assistance assistance;

  @Basic
  @Column(name = "secphone", nullable = false, length = -1)
  @Setter
  @Getter
  private String secphone;

  public enum Assistance {
    HARASSMENT("Harassment"),
    SECURITY_ESCORT("Security Escort"),
    POTENTIAL_THREAT("Potential Threat");

    @NonNull public final String assistance;

    Assistance(@NonNull String assistance) {
      this.assistance = assistance;
    }
  }

  public SecurityrequestEntity() {}

  public SecurityrequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency urgency,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      Assistance assistance,
      String secphone,
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
    this.assistance = assistance;
    this.secphone = secphone;
  }

  public SecurityrequestEntity(
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency urgency,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      Assistance assistance,
      String secphone) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.assistance = assistance;
    this.secphone = secphone;
  }
}
