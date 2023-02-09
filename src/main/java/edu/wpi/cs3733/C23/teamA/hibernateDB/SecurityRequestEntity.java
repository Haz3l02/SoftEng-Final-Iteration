package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "securityrequest")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class SecurityRequestEntity extends ServiceRequestEntity {

  //  @Id
  //  @Column(name = "requestid", nullable = false)
  //  @Setter
  //  @Getter
  //  private int requestid;

  @Basic
  @Column(name = "assistance", nullable = false, length = -1)
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

  public SecurityRequestEntity() {}

  public SecurityRequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
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

  public SecurityRequestEntity(
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
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
