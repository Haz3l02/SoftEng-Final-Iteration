package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "employee", schema = "public", catalog = "dba")
public class EmployeeEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "employeeid", nullable = false, length = -1)
  private String employeeid;

  @Basic
  @Column(name = "username", nullable = true, length = -1)
  private String username;

  @Basic
  @Column(name = "password", nullable = true, length = -1)
  private String password;

  @Basic
  @Column(name = "job", nullable = true, length = -1)
  private String job;

  @Basic
  @Column(name = "name", nullable = true, length = -1)
  private String name;

  public String getEmployeeid() {
    return employeeid;
  }

  public void setEmployeeid(String employeeid) {
    this.employeeid = employeeid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
