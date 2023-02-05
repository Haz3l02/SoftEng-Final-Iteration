package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
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
}
