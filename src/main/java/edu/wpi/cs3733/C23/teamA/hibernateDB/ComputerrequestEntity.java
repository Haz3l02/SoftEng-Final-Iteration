package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "computerrequest", catalog = "dba")
public class ComputerrequestEntity extends ServicerequestEntity {

  @Id
  @Column(name = "requestid", nullable = false)
  @Getter
  @Setter
  private int requestid;

  @Basic
  @Column(name = "deviceid", nullable = false, length = -1)
  @Getter
  @Setter
  private String deviceid;

  @Basic
  @Column(name = "device", nullable = false, length = -1)
  @Getter
  @Setter
  private String device;


}
