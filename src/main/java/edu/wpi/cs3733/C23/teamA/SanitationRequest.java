package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.databases.Adb;
import java.sql.SQLException;
import lombok.*;

// for new request
public class SanitationRequest extends ServiceRequest {
  @Getter private static final String tableName = "SanitationRequest";
  @Getter @Setter String category; // sanitation

  public SanitationRequest() {
    super();
    category = null;
  }

  // for new request
  public SanitationRequest(
      String name,
      String idNum,
      String location,
      String description,
      String category,
      String ul,
      String requestType,
      String status,
      String employeeAssigned)
      throws SQLException {
    super(name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.category = category;
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
      String requestType,
      String status,
      String employeeAssigned) {
    // for selecting existing
    super(requestID, name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.category = category;
  }

  //  public static void initTable() throws SQLException {
  //    String sql =
  //        String.join(
  //            " ",
  //            "CREATE TABLE SanitationRequest",
  //            "(requestID SERIAL PRIMARY KEY, ",
  //            "name text,",
  //            "idNum text,",
  //            "location text,",
  //            "description text,",
  //            "category text,",
  //            "ul text,",
  //            "requestType text,",
  //            "status text,",
  //            "employeeAssigned text);");
  //    Adb.processUpdate(sql);
  //  }

  public void insert() throws SQLException {
    //    String sql =
    //            String.join(
    //                    " ",
    //                    "insert into SanitationRequest",
    //                    "(name, idNum, location, description, category, ul, requestType, status,
    // employeeAssigned) VALUES" +
    //                            "( '" + name + "', '" + idNum + "', '" + location + "', '" +
    // description + "', '" + category + "', '" + ul + "', '" + requestType + "', '" + status, "',
    // '" + employeeAssigned + "');");
    String sql =
        String.join(
            " ",
            "insert into ServiceRequest",
            "(name, idNum, location, description, ul, requestType, status, employeeAssigned) VALUES"
                + "( '"
                + name
                + "', '"
                + idNum
                + "', '"
                + location
                + "', '"
                + description
                + "', '"
                + ul
                + "', '"
                + requestType
                + "', '"
                + status,
            "', '" + employeeAssigned + "');");
    Adb.processUpdate(sql);
    sql =
        String.join(
            " ",
            "insert into SanitationRequest(requestID, category) VALUES",
            "((select max(requestID) from ServiceRequest), '" + category + "');");
    Adb.processUpdate(sql);
  }

  // returns list of sanitation requests based on a userID
  /*
    public ArrayList<SanitationRequest> getSanitationRequestByUser(String id) throws SQLException {
      ArrayList<SanitationRequest> fin = new ArrayList<>();
      String sql =
          "SELECT * FROM "
              + "(SanitationRequest SRR join ServiceRequest SR"
              + "on SRR.requestID = SR.requestID)"
              + "where idNum = '"
              + id
              + "';";
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
                rs.getString("requestType"),
                rs.getString("status"),
                rs.getString("employeeAssigned")));
      }
      return fin;
    }
  */
  /**
   * have an update method take in the string id and the updated data for this iteration youre only
   * updating status and employeeAssigned so you can make the update for just those columns (this
   * week) OR do a full row update but pass in the existing service request info
   *
   * <p>""
   */
}
