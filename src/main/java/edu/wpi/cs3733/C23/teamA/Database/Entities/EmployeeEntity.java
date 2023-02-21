package edu.wpi.cs3733.C23.teamA.Database.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Table(
    name = "employee",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_emp_username",
            columnNames = {"username", "hospitalid"}))
public class EmployeeEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY) // , generator = "serviceseq")
  @Id
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @Column(name = "employeeid")
  @Setter
  @Getter
  private int employeeid;

  @Basic
  @Column(name = "hospitalid", nullable = false, length = -1)
  @Getter
  @Setter
  private String hospitalid;

  @Basic
  @Column(name = "username", nullable = false, length = -1, unique = true)
  @Getter
  @Setter
  private String username;

  @Basic
  @Column(name = "password", nullable = false, length = -1)
  @Getter
  @Setter
  private String password;

  @Basic
  @Column(name = "job", nullable = false, length = -1)
  @Getter
  @Setter
  private String job;

  @Basic
  @Column(name = "name", nullable = false, length = -1)
  @Getter
  @Setter
  private String name;

  public EmployeeEntity(
      String hospitalid, String username, String password, String job, String name) {
    this.hospitalid = hospitalid;
    this.username = username;
    this.password = password;
    this.job = job;
    this.name = name;
  }

  public EmployeeEntity(
      int employeeid,
      String hospitalid,
      String username,
      String password,
      String job,
      String name) {
    this.hospitalid = hospitalid;
    this.employeeid = employeeid;
    this.username = username;
    this.password = password;
    this.job = job;
    this.name = name;
  }

  public EmployeeEntity() {}
}
