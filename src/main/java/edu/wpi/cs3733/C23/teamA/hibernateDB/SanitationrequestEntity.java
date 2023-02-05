package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "sanitationrequest", catalog = "dba")
public class SanitationrequestEntity extends ServicerequestEntity {

  @Id
  @Column(name = "requestid", nullable = false)
  @Setter
  @Getter
  private int requestid;

  @Basic
  @Column(name = "category", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Category category;

  public enum Category {
    BIOHAZARD("Biohazard"),
    STANDARD("Standard"),
    WONG("Wong");

    @NonNull public final String category;

    Category(@NonNull String category) {
      this.category = category;
    }
  }

  public SanitationrequestEntity() {}

  public SanitationrequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency ul,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      Category category,
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
    this.category = category;
  }

  public SanitationrequestEntity(
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency ul,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      Category category) {
    super(name, employee, location, description, ul, requesttype, status, employeeassigned);
    this.category = category;
  }
}
