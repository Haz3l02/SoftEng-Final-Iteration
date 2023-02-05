package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "securityrequest", schema = "public", catalog = "dba")
public class SecurityrequestEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "requestid", nullable = false)
  @Setter
  @Getter
  private int requestid;

  @Basic
  @Column(name = "request", nullable = true, length = -1)
  @Setter
  @Getter
  private String request;

  @Basic
  @Column(name = "secphone", nullable = true, length = -1)
  @Setter
  @Getter
  private String secphone;
}
