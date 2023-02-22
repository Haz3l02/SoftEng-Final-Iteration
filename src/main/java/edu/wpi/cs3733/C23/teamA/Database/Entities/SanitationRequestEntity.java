package edu.wpi.cs3733.C23.teamA.Database.Entities;

import edu.wpi.cs3733.C23.teamA.enums.IssueCategory;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import edu.wpi.cs3733.C23.teamA.enums.UrgencyLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sanitationrequest")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class SanitationRequestEntity extends ServiceRequestEntity {

  //  @Id
  //  @Column(name = "requestid", nullable = false)
  //  @Setter
  //  @Getter
  //  private int requestid;

  @Basic
  @Column(name = "category", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private IssueCategory category;

  public SanitationRequestEntity() {}

  public SanitationRequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      IssueCategory category,
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
    this.category = category;
  }

  public SanitationRequestEntity(
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      IssueCategory category) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.category = category;
  }
}
