package edu.wpi.cs3733.C23.teamA.Database.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locationname")
public class LocationNameEntity {

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

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.getClass() != obj.getClass()) return false;
    LocationNameEntity loc = (LocationNameEntity) obj;
    return (this.longname.equals(loc.getLongname()));
  }

  public LocationNameEntity(String longname, String shortname, String locationtype) {
    this.longname = longname;
    this.shortname = shortname;
    this.locationtype = locationtype;
  }

  public LocationNameEntity() {}

  //  @Column(name = "moves", nullable = false)
  //  @Getter
  //  @Setter
  //  @OneToMany(mappedBy = "locationname", cascade = CascadeType.ALL)
  //  private Set<MoveEntity> moves;
}
