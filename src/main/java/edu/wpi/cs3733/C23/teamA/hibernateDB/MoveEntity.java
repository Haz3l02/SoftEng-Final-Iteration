package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "move", catalog = "dba")
// @IdClass(MoveEntityPK.class)
public class MoveEntity {

  @Id
  @Column(name = "node", nullable = false, length = -1)
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "nodeid")
  private NodeEntity node;

  @Id
  @Column(name = "locationname", nullable = false, length = -1)
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "longname")
  private LocationnameEntity locationname;


  @Id
  @Column(name = "movedate", nullable = false)
  @Getter
  @Setter
  private Timestamp movedate;


  public MoveEntity() {
  }

  public MoveEntity(NodeEntity node, LocationnameEntity locationname, Timestamp movedate) {
    this.node = node;
    this.locationname = locationname;
    this.movedate = movedate;
  }


  public MoveEntity(NodeEntity node, LocationnameEntity locationname) {
    this.node = node;
    this.locationname = locationname;
  }



}


