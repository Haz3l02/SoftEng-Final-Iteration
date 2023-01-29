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
  @Getter @Setter String requestType;


  public SanitationRequest() {
    requestID = 0;
    name=null;
    idNum=null;
    location=null;
    description=null;
    category=null;
    ul=null;
    requestType=null;
  }

  //for new request
  public SanitationRequest(
      String name, String idNum, String location, String description, String category, String ul, String requestType) {
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
    this.requestType = requestType;
  }


  //for selecting existing
  public SanitationRequest(int requestID, String name, String idNum, String location, String description, String category, String ul, String requestType) {
    this.requestID = requestID;
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
    this.requestType = requestType;
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
            "ul text,",
            "requestType text);");
    Adb.processUpdate(sql);
  }



  public void insert() throws SQLException {
    String sql =
            String.join(
                    " ",
                    "insert into SanitationRequest",
                    "(name, idNum, location, description, category, ul) VALUES" +
                            "( " + name + ", " + idNum + ", " + location + ", " + description + ", " + category + ", " + ul + ", " + requestType + ");");
    Adb.processUpdate(sql);
  }


  //returns list of sanitation requests based on a userID
  public ArrayList<SanitationRequest> getSanitationRequestByUser(String id) throws SQLException {
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
                      rs.getString("ul"),
                      rs.getString("requestID")));
    }
    return fin;
  }
}
