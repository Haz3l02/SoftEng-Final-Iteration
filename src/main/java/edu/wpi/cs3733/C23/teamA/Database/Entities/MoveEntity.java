package edu.wpi.cs3733.C23.teamA.Database.Entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "move")
// @IdClass(MoveEntityPK.class)
public class MoveEntity {

  @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(
      name = "nodeid",
      foreignKey =
          @ForeignKey(
              name = "node_fk",
              foreignKeyDefinition =
                  "FOREIGN KEY (nodeid) REFERENCES node(nodeid) ON UPDATE CASCADE ON DELETE CASCADE"))
  private NodeEntity node;

  @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(
      name = "longname",
      foreignKey =
          @ForeignKey(
              name = "longname_fk",
              foreignKeyDefinition =
                  "FOREIGN KEY (longname) REFERENCES locationname(longname) ON UPDATE CASCADE ON DELETE CASCADE"))
  private LocationNameEntity locationName;

  @Id
  @Column(name = "movedate", nullable = false)
  @Getter
  @Setter
  private Timestamp movedate;

  public MoveEntity() {}

  public MoveEntity(NodeEntity node, LocationNameEntity locationName, Timestamp movedate) {
    this.node = node;
    this.locationName = locationName;
    this.movedate = movedate;
  }

  public MoveEntity(NodeEntity node, LocationNameEntity locationName) {
    this.node = node;
    this.locationName = locationName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.getClass() != obj.getClass()) return false;
    MoveEntity mo = (MoveEntity) obj;
    return (this.locationName.equals(mo.locationName)
        && this.movedate.equals(mo.movedate)
        && this.node.equals(mo.node));
  }

  @Override
  public int hashCode() {
    return node.getXcoord() * node.getYcoord() + movedate.getNanos();
  }
}
