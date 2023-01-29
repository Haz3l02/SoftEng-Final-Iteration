package edu.wpi.cs3733.C23.teamA.databases;

import lombok.Getter;
import lombok.Setter;
import edu.wpi.cs3733.C23.teamA.databases.Adb;


import java.sql.SQLException;

public class Edge {
  private static final String tableName = "Edge";

  @Setter @Getter
  private String node1;
  @Setter @Getter
  private String node2;

  public Edge() {
    node1 = null;
    node2 = null;
  }

  public Edge(String node1, String node2) {
    this.node1 = node1;
    this.node2 = node2;
  }


//  public static HashMap<String, Edge> getAll() throws SQLException {
//    HashMap<String, Edge> edges = new HashMap<>();
//    String sql = "SELECT * FROM Edge;";
//    ResultSet rs = Adb.processQuery(sql);
//    while (rs.next()) {
//      edges.put(new Edge(rs.getString("node1"), rs.getString("node2")));
//    }
//    return edges;
//  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "CREATE TABLE Edge",
            "(node1 text,",
            "node2 text);");
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


}
