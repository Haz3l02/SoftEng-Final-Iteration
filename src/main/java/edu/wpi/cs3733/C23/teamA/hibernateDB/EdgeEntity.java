package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "edge", schema = "public", catalog = "dba")
@IdClass(EdgeEntityPK.class)
public class EdgeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "node1", nullable = false, length = -1)
    private String node1;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "node2", nullable = false, length = -1)
    private String node2;

    public String getNode1() {
        return node1;
    }

    public void setNode1(String node1) {
        this.node1 = node1;
    }

    public String getNode2() {
        return node2;
    }

    public void setNode2(String node2) {
        this.node2 = node2;
    }
}
