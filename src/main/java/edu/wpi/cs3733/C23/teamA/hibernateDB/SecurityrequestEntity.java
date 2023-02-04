package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "securityrequest", schema = "public", catalog = "dba")
public class SecurityrequestEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "requestid", nullable = false)
  private int requestid;

  @Basic
  @Column(name = "request", nullable = true, length = -1)
  private String request;

  @Basic
  @Column(name = "secphone", nullable = true, length = -1)
  private String secphone;

  public int getRequestid() {
    return requestid;
  }

  public void setRequestid(int requestid) {
    this.requestid = requestid;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public String getSecphone() {
    return secphone;
  }

  public void setSecphone(String secphone) {
    this.secphone = secphone;
  }
}
