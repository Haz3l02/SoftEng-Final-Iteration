package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "edge", catalog = "dba")
// @IdClass(EdgeEntityPK.class)
public class EdgeEntity {

  @Id
  @Column(name = "node1", nullable = false, length = -1)
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "nodeid")
  private NodeEntity node1;

  @Id
  @Column(name = "node2", nullable = false, length = -1)
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "nodeid")
  private NodeEntity node2;
}
