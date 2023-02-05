package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "node", catalog = "dba")
public class NodeEntity {

  @Id
  @Column(name = "nodeid", nullable = false, length = -1)
  @Getter
  @Setter
  private String nodeid;

  @Basic
  @Column(name = "xcoord", nullable = false)
  @Getter
  @Setter
  private Integer xcoord;

  @Basic
  @Column(name = "ycoord", nullable = false)
  @Getter
  @Setter
  private Integer ycoord;

  @Basic
  @Column(name = "floor", nullable = false, length = -1)
  @Getter
  @Setter
  private String floor;

  @Basic
  @Column(name = "building", nullable = false, length = -1)
  @Getter
  @Setter
  private String building;


}
