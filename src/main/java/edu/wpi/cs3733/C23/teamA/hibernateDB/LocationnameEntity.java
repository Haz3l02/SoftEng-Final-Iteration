package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "locationname", schema = "public", catalog = "dba")
public class LocationnameEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "longname", nullable = false, length = -1)
  private String longname;

  @Basic
  @Column(name = "shortname", nullable = true, length = -1)
  private String shortname;

  @Basic
  @Column(name = "locationtype", nullable = true, length = -1)
  private String locationtype;

  public String getLongname() {
    return longname;
  }

  public void setLongname(String longname) {
    this.longname = longname;
  }

  public String getShortname() {
    return shortname;
  }

  public void setShortname(String shortname) {
    this.shortname = shortname;
  }

  public String getLocationtype() {
    return locationtype;
  }

  public void setLocationtype(String locationtype) {
    this.locationtype = locationtype;
  }
}
