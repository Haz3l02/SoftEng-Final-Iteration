package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee", catalog = "dba")
public class EmployeeEntity {

  @Id
  @Column(name = "employeeid", nullable = false, length = -1)
  @Getter
  @Setter
  private String employeeid;

  @Basic
  @Column(name = "username", nullable = false, length = -1)
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

  @Column(name = "requests", nullable = false)
  @Getter
  @Setter
  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<ServicerequestEntity> requests;

  public EmployeeEntity addServiceRequest(ServicerequestEntity req) {
    this.requests.add(req);
    return this;
  }
}
