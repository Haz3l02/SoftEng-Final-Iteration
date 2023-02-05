package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
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
  private String category;

  public SanitationrequestEntity() {}

  public SanitationrequestEntity(
      int requestid,
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String category,
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
    this.category = category;
  }

  public SanitationrequestEntity(
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String category) {
    super(name, employeeid, location, description, ul, requesttype, status, employeeassigned);
    this.category = category;
  }
}
