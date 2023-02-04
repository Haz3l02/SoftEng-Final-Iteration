package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EdgeEntityPK implements Serializable {
  @Column(name = "node1", nullable = false, length = -1)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String node1;

  @Column(name = "node2", nullable = false, length = -1)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EdgeEntityPK that = (EdgeEntityPK) o;
    return Objects.equals(node1, that.node1) && Objects.equals(node2, that.node2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(node1, node2);
  }
}
