package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "node", schema = "public", catalog = "dba")
public class NodeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "nodeid", nullable = false, length = -1)
    private String nodeid;
    @Basic
    @Column(name = "xcoord", nullable = true)
    private Integer xcoord;
    @Basic
    @Column(name = "ycoord", nullable = true)
    private Integer ycoord;
    @Basic
    @Column(name = "floor", nullable = true, length = -1)
    private String floor;
    @Basic
    @Column(name = "building", nullable = true, length = -1)
    private String building;

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public Integer getXcoord() {
        return xcoord;
    }

    public void setXcoord(Integer xcoord) {
        this.xcoord = xcoord;
    }

    public Integer getYcoord() {
        return ycoord;
    }

    public void setYcoord(Integer ycoord) {
        this.ycoord = ycoord;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
