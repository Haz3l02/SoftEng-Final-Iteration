package edu.wpi.cs3733.C23.teamA.Database.Entities;

import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.Subject;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accessibility")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class AccessibilityRequestEntity extends ServiceRequestEntity {

  @Basic
  @Column(name = "subject", nullable = false, length = -1)
  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private Subject subject;

  @Basic
  @Column(name = "disability", nullable = false, length = -1)
  @Getter
  @Setter
  private String disability;

  @Basic
  @Column(name = "accommodation", nullable = false, length = -1)
  @Getter
  @Setter
  private String accommodation;

  public AccessibilityRequestEntity() {}

  public AccessibilityRequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      Subject subject,
      String disability,
      String accommodation,
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
    this.subject = subject;
    this.disability = disability;
    this.accommodation = accommodation;
  }

  public AccessibilityRequestEntity(
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      Subject subject,
      String disability,
      String accommodation) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.subject = subject;
    this.disability = disability;
    this.accommodation = accommodation;
  }
}
