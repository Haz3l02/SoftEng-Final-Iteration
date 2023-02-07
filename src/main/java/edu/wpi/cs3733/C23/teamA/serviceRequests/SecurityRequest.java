package edu.wpi.cs3733.C23.teamA.serviceRequests;

import edu.wpi.cs3733.C23.teamA.databases.Adb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.*;

@Deprecated
public class SecurityRequest extends ServiceRequest {

  @Getter private static final String tableName = "SecurityRequest";
  @Getter @Setter String request;
  @Getter @Setter String secPhone;

  public SecurityRequest() {
    super();
    request = null;
    secPhone = null;
  }

  // for new request
  public SecurityRequest(
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String request,
      String secPhone)
      throws SQLException {
    super(name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.request = request;
    this.secPhone = secPhone;
  }

  // for selecting existing
  public SecurityRequest(
      int requestID,
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String request,
      String secPhone) {
    // for selecting existing
    super(requestID, name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.request = request;
    this.secPhone = secPhone;
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
                + status
                + "', '"
                + employeeAssigned
                + "');");
    Adb.processUpdate(sql);
    sql =
        String.join(
            " ",
            "insert into SecurityRequest(requestID, request, secPhone) VALUES",
            "((select max(requestID) from ServiceRequest), '"
                + request
                + "', '"
                + secPhone
                + "');");
    Adb.processUpdate(sql);
  }

  // returns list of sanitation requests based on a userID

  public ArrayList<SecurityRequest> getSecurityRequestByUser(String id) throws SQLException {
    ArrayList<SecurityRequest> fin = new ArrayList<>();
    String sql =
        "SELECT * FROM "
            + "SecurityRequest join ServiceRequest"
            + "on SecurityRequest.requestID = ServiceRequest.requestID"
            + "where idNum = '"
            + id
            + "';";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      fin.add(
          new SecurityRequest(
              rs.getInt("requestID"),
              rs.getString("name"),
              rs.getString("idNum"),
              rs.getString("location"),
              rs.getString("description"),
              rs.getString("ul"),
              rs.getString("requestType"),
              rs.getString("status"),
              rs.getString("employeeAssigned"),
              rs.getString("request"),
              rs.getString("secPhone")));
    }
    return fin;
  }
}
