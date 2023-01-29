package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.wpi.cs3733.C23.teamA.databases.Adb;

import lombok.*;

public class SanitationRequest {

  @Getter private static final String tableName = "SanitationRequest";
  @Getter @Setter int requestID;
  @Getter @Setter String name;
  @Getter @Setter String idNum;
  @Getter @Setter String location;
  @Getter @Setter String description;
  @Getter @Setter String category;
  @Getter @Setter String ul;



  //for new request
  public SanitationRequest(
      String name, String idNum, String location, String description, String category, String ul) {
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
  }


  //for selecting existing
  public SanitationRequest(int requestID, String name, String idNum, String location, String description, String category, String ul) {
    this.requestID = requestID;
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "CREATE TABLE SanitationRequest",
            "(requestID SERIAL PRIMARY KEY, ",
            "name text,",
            "idNum text,",
            "location text,",
            "description text,",
            "category text,",
            "ul text);");
    Adb.processUpdate(sql);
  }



  public void insert() throws SQLException {
    String sql =
            String.join(
                    " ",
                    "insert into SanitationRequest",
                    "(name, idNum, location, description, category, ul) VALUES" +
                            "( " + name + ", " + idNum + ", " + location + ", " + description + ", " + category + ", " + ul + ");");
    Adb.processUpdate(sql);
  }

  public ArrayList<SanitationRequest> getSanitationRequestByUser(int id) throws SQLException {
    ArrayList<SanitationRequest> fin = new ArrayList<>();
    String sql = "SELECT * FROM SanitationRequest where idNum = " +id+ ";";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      fin.add(
              new SanitationRequest(rs.getInt("requestID"),
                      rs.getString("name"),
                      rs.getString("idNum"),
                      rs.getString("location"),
                      rs.getString("description"),
                      rs.getString("category"),
                      rs.getString("ul")));
    }
    return fin;
  }
}
