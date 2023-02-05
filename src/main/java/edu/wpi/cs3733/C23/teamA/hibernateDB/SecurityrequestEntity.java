package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "securityrequest", catalog = "dba")
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
  private String request;

  @Basic
  @Column(name = "secphone", nullable = false, length = -1)
  @Setter
  @Getter
  private String secphone;

  public SecurityrequestEntity() {}

  public SecurityrequestEntity(
      int requestid,
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String request,
      String secphone,
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
    this.request = request;
    this.secphone = secphone;
  }

  public SecurityrequestEntity(
      String name,
      String employeeid,
      String location,
      String description,
      Urgency ul,
      String requesttype,
      Status status,
      String employeeassigned,
      String request,
      String secphone) {
    super(name, employeeid, location, description, ul, requesttype, status, employeeassigned);
    this.request = request;
    this.secphone = secphone;
  }
}
