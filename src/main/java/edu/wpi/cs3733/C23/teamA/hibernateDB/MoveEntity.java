package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "move", schema = "public", catalog = "dba")
@IdClass(MoveEntityPK.class)
public class MoveEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "nodeid", nullable = false, length = -1)
    private String nodeid;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "longname", nullable = false, length = -1)
    private String longname;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movedate", nullable = false)
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
}
