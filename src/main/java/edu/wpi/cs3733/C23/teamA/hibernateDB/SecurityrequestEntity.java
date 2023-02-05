package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
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
}
