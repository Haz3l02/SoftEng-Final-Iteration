package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class MoveEntityPK implements Serializable {
  @Column(name = "nodeid", nullable = false, length = -1)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String nodeid;

  @Column(name = "longname", nullable = false, length = -1)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String longname;

  @Column(name = "movedate", nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Timestamp movedate;

  public String getNodeid() {
    return nodeid;
  }

  public void setNodeid(String nodeid) {
    this.nodeid = nodeid;
  }

  public String getLongname() {
    return longname;
  }

  public void setLongname(String longname) {
    this.longname = longname;
  }

  public Timestamp getMovedate() {
    return movedate;
  }

  public void setMovedate(Timestamp movedate) {
    this.movedate = movedate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MoveEntityPK that = (MoveEntityPK) o;
    return Objects.equals(nodeid, that.nodeid)
        && Objects.equals(longname, that.longname)
        && Objects.equals(movedate, that.movedate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeid, longname, movedate);
  }
}
