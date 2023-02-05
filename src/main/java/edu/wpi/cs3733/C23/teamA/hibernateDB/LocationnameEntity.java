package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locationname", catalog = "dba")
public class LocationnameEntity {

  @Id
  @Column(name = "longname", nullable = false, length = -1)
  @Getter
  @Setter
  private String longname;

  @Basic
  @Column(name = "shortname", nullable = false, length = -1)
  @Getter
  @Setter
  private String shortname;

  @Basic
  @Column(name = "locationtype", nullable = false, length = -1)
  @Getter
  @Setter
  private String locationtype;
}
