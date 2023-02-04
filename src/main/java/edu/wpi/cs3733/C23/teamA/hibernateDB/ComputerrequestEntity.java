package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "computerrequest", schema = "public", catalog = "dba")
public class ComputerrequestEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "requestid", nullable = false)
  private int requestid;

  @Basic
  @Column(name = "deviceid", nullable = true, length = -1)
  private String deviceid;

  @Basic
  @Column(name = "device", nullable = true, length = -1)
  private String device;

  public int getRequestid() {
    return requestid;
  }

  public void setRequestid(int requestid) {
    this.requestid = requestid;
  }

  public String getDeviceid() {
    return deviceid;
  }

  public void setDeviceid(String deviceid) {
    this.deviceid = deviceid;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }
}
