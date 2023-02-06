package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "edge")
// @IdClass(EdgeEntityPK.class)
public class EdgeEntity {

  @Id
  @Column(name = "edgeid", length = -1)
  @Getter
  @Setter
  private String edgeid;

  // @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "node1", foreignKey = @ForeignKey(name = "node1"))
  private NodeEntity node1;

  // @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "node2", foreignKey = @ForeignKey(name = "node2"))
  private NodeEntity node2;

  public EdgeEntity(NodeEntity node1, NodeEntity node2, String edgeid) {
    this.node1 = node1;
    this.node2 = node2;
    this.edgeid = edgeid;
  }

  public EdgeEntity() {}

  @Override
  public int hashCode() {
    return node1.getXcoord() * node1.getYcoord() + node2.getXcoord() * node2.getYcoord();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.getClass() != obj.getClass()) return false;
    EdgeEntity ed = (EdgeEntity) obj;
    return (node1.equals(ed.node1) && node2.equals(ed.node2)
        || node1.equals(ed.node2) && node2.equals(ed.node1));
  }
}
