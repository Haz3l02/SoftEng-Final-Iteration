package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.database.Adb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
  @Getter @Setter String status;
  @Getter @Setter String requestType;

  // for new request
  public SanitationRequest(
      String name,
      String idNum,
      String location,
      String description,
      String category,
      String ul,
      String requestType) {
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
    this.requestType = requestType;
    // this.status = status;
  }

  // for selecting existing
  public SanitationRequest(
      int requestID,
      String name,
      String idNum,
      String location,
      String description,
      String category,
      String ul,
      String requestType) {
    this.requestID = requestID;
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
    this.requestType = requestType;
    // this.status = status;
  }

  // for submitting data;
  public SanitationRequest() {
    this.requestID = 0;
    this.name = "";
    this.idNum = "";
    this.location = "";
    this.description = "";
    this.category = "";
    this.ul = "";
    this.status = "";
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
            "(name, idNum, location, description, category, ul, requestType) VALUES"
                + "( '"
                + name
                + "', '"
                + idNum
                + "', '"
                + location
                + "', '"
                + description
                + "', '"
                + category
                + "', '"
                + ul
                + "', '"
                + requestType
                + "');");
    Adb.processUpdate(sql);
  }

  public ArrayList<SanitationRequest> getSanitationRequestByUser(String id) throws SQLException {
    ArrayList<SanitationRequest> fin = new ArrayList<>();

    String sql = "SELECT * FROM SanitationRequest where idNum = '" + id + "';";

    System.out.println(sql);

    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      fin.add(
          new SanitationRequest(
              rs.getInt("requestID"),
              rs.getString("name"),
              rs.getString("idNum"),
              rs.getString("location"),
              rs.getString("description"),
              rs.getString("category"),
              rs.getString("ul"),
              rs.getString("requestType")));
    }
    return fin;
  }

  /**
   * have an update method take in the string id and the updated data for this iteration youre only
   * updating status and employeeAssigned so you can make the update for just those columns (this
   * week) OR do a full row update but pass in the existing service request info
   */
}
