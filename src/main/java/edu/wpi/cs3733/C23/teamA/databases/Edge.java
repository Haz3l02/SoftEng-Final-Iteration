package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Edge {
  private static final String tableName = "edge";

  @Setter @Getter private String node1;
  @Setter @Getter private String node2;

  public Edge() {
    node1 = null;
    node2 = null;
  }

  public Edge(String node1, String node2) {
    this.node1 = node1;
    this.node2 = node2;
  }

  public static ArrayList<Edge> getAll() throws SQLException {
    ArrayList<Edge> edges = new ArrayList<>();
    String sql = "SELECT * FROM Edge;";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      edges.add(new Edge(rs.getString("node1"), rs.getString("node2")));
    }
    return edges;
  }

  public static void initTable() throws SQLException {
    String sql = String.join(" ", "CREATE TABLE Edge", "(node1 text,", "node2 text);");
    Adb.processUpdate(sql);
  }

  //  public ResultSet getSpecificEdge(String id) throws SQLException {
  //    String sql = String.join(" ", "select * from Edge where edgeId = '" + id + "';");
  //    return Adb.processQuery(sql);
  //  }

  //  public void delete(String id) throws SQLException {
  //    String sql = String.join(" ", "DELETE from Edge where edgeID = '" + id + "'");
  //    Adb.processUpdate(sql);
  //  }

  public static String getTableName() {
    return tableName;
  }

  public String toString() {
    return String.format("%s %s", node1, node2);
  }
}
