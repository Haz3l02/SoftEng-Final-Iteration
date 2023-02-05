package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "move", catalog = "dba")
// @IdClass(MoveEntityPK.class)
public class MoveEntity {

  @Id
  @Column(name = "nodeid", nullable = false, length = -1)
  @Getter
  @Setter
  private String nodeid;

  @Id
  @Column(name = "longname", nullable = false, length = -1)
  @Getter
  @Setter
  private String longname;

  @Id
  @Column(name = "movedate", nullable = false)
  @Getter
  @Setter
  private Timestamp movedate;


}
