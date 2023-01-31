package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Node {
  @Getter private static final String tableName = "node";
  @Setter @Getter private String nodeID;
  @Setter @Getter private int xcoord;
  @Setter @Getter private int ycoord;
  @Setter @Getter private String floor;
  @Setter @Getter private String building;

  public Node() {
    nodeID = null;
    xcoord = 0;
    ycoord = 0;
    floor = null;
    building = null;
  }

  public Node(String nodeID, int xcoord, int ycoord, String floor, String building) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "create table node (\n"
                + "  nodeid text primary key not null,\n"
                + "  xcoord integer,\n"
                + "  ycoord integer,\n"
                + "  floor text,\n"
                + "  building text\n);");
    Adb.processUpdate(sql);
  }

  public static ArrayList<Node> getAll() throws SQLException {
    ArrayList<Node> nodes = new ArrayList<>();
    String sql = "SELECT * FROM Node;";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      nodes.add(
          new Node(
              rs.getString("nodeID"),
              rs.getInt("xcoord"),
              rs.getInt("ycoord"),
              rs.getString("floor"),
              rs.getString("building")));
    }
    return nodes;
  }

  public static ResultSet getSpecificNode(String id) throws SQLException {
    String sql = String.format("select * from Node where nodeID = %s;", id);
    return Adb.processQuery(sql);
  }

  public static void delete(String id) throws SQLException {
    String sql = String.join(" ", "DELETE from Node where nodeID = '" + id + "'");
    Adb.processUpdate(sql);
  }

  public void update() throws SQLException {
    String sql =
        String.format(
            "update node set (xcoord, ycoord, floor, building) = (%d,%d,'%s','%s') where nodeid = '%s';",
            xcoord, ycoord, floor, building, nodeID);
    String idCorrection =
        String.format(
            "update node set nodeid = floor  || 'X' || to_char(xcoord, 'fm0000') || 'Y' || to_char(ycoord, 'fm0000');");
    Adb.processUpdate(sql);
    Adb.processUpdate(idCorrection);
  }

  public String toString() {
    return String.format("%s %d %d %s %s", nodeID, xcoord, ycoord, floor, building);
  }

  //  public int setLocation(String shortName, String longName, String node) throws SQLException {
  //    String sql =
  //        String.join(
  //            " ",
  //            "UPDATE Node "
  //                + "SET shortname = '"
  //                + shortName
  //                + "', longname = '"
  //                + longName
  //                + "' WHERE nodeID = '"
  //                + node
  //                + "';");
  //    return Adb.processUpdate(sql);
  //  }
}
