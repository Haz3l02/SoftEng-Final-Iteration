package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Edge {
  @Getter private static final String tableName = "edge";

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
    String sql =
        String.join(
            " ",
            "create table public.edge (\n"
                + "  node1 text not null,\n"
                + "  node2 text not null,\n"
                + "  primary key (node1, node2),\n"
                + "  foreign key (node1) references public.node (nodeid)\n"
                + "  match simple on update cascade on delete no action,\n"
                + "  foreign key (node2) references public.node (nodeid)\n"
                + "  match simple on update cascade on delete no action);");
    Adb.processUpdate(sql);
  }

  public static ArrayList<String> connections(String nodeid) throws SQLException {
    ArrayList<String> nodes = new ArrayList<>();
    String sql = String.format("select node2 from edge where node1 = '%s';", nodeid);
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      nodes.add(rs.getString("node2"));
    }
    sql = String.format("select node1 from edge where node2 = '%s';", nodeid);
    rs = Adb.processQuery(sql);
    while (rs.next()) {
      nodes.add(rs.getString("node1"));
    }
    return nodes;
  }

  public void update() throws SQLException {
    String sql =
        String.format("insert into edge values('%s','%s') on conflict do update;", node1, node2);
    Adb.processUpdate(sql);
  }

  public String toString() {
    return String.format("%s %s", node1, node2);
  }
}
