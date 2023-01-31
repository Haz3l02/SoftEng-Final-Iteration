package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Move {

  private static final String tableName = "move";

  @Setter @Getter private String nodeID;
  @Setter @Getter private String longName;
  @Setter @Getter private String moveDate;

  public Move(String nodeID, String longName, String moveDate) {
    this.nodeID = nodeID;
    this.longName = longName;
    this.moveDate = moveDate;
  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "create table move (\n"
                + "  nodeid text not null,\n"
                + "  longname text not null,\n"
                + "  movedate timestamp without time zone not null,\n"
                + "  primary key (nodeid, longname, movedate),\n"
                + "  foreign key (longname) references locationname (longname)\n"
                + "  match simple on update no action on delete no action,\n"
                + "  foreign key (nodeid) references node (nodeid)\n"
                + "  match simple on update cascade on delete no action);");
    Adb.processUpdate(sql);
  }

  public static ArrayList<Move> getAll() throws SQLException {
    ArrayList<Move> moves = new ArrayList<>();
    String sql = "SELECT * FROM Move;";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      moves.add(
          new Move(rs.getString("nodeid"), rs.getString("longname"), rs.getString("movedate")));
    }
    return moves;
  }

  public void update() throws SQLException {
    String sql = String.format("insert into move values('%s','%s',NOW());", nodeID, longName);
    Adb.processUpdate(sql);
  }

  public static String mostRecentLoc(String node) throws SQLException {
    String loc = "";
    String sql =
        String.join(
            " ",
            "select * from move where nodeID = '" + node + "' order by moveDate desc limit 1;");
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      loc = rs.getString("longName");
    }
    return loc;
  }

  public String toString() {
    return String.format("%s %s %s", nodeID, longName, moveDate);
  }
}
