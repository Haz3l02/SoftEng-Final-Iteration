package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.*;

public class ComputerRequest extends ServiceRequest {

  @Getter private static final String tableName = "ComputerRequest";
  @Getter @Setter String deviceID;
  @Getter @Setter String device;

  public ComputerRequest() {
    super();
    deviceID = null;
    device = null;
  }

  // for new request
  public ComputerRequest(
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String deviceID,
      String device)
      throws SQLException {
    super(name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.deviceID = deviceID;
    this.device = device;
  }

  // for selecting existing
  public ComputerRequest(
      int requestID,
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String deviceID,
      String device) {
    // for selecting existing
    super(requestID, name, idNum, location, description, ul, requestType, status, employeeAssigned);
    this.deviceID = deviceID;
    this.device = device;
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
            "insert into ComputerRequest(requestID, deviceID, device) VALUES",
            "((select max(requestID) from ServiceRequest), '" + deviceID + "', '" + device + "');");
    Adb.processUpdate(sql);
  }

  public void updateComputerRequest(
      int requestId,
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned,
      String deviceID,
      String device)
      throws SQLException {
    updateServiceRequest(
        requestId, name, idNum, location, description, ul, requestType, status, employeeAssigned);
    String sql =
        "update servicerequest set deviceId = "
            + deviceID
            + "', device = "
            + device
            + "' where requestID = "
            + requestId
            + ";";
    Adb.processUpdate(sql);
  }

  // returns list of sanitation requests based on a userID

  public ArrayList<ComputerRequest> getComputerRequestByUser(String id) throws SQLException {
    ArrayList<ComputerRequest> fin = new ArrayList<>();
    String sql =
        "SELECT * FROM "
            + "ComputerRequest join ServiceRequest"
            + "on ComputerRequest.requestID = ServiceRequest.requestID"
            + "where idNum = '"
            + id
            + "';";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      fin.add(
          new ComputerRequest(
              rs.getInt("requestID"),
              rs.getString("name"),
              rs.getString("idNum"),
              rs.getString("location"),
              rs.getString("description"),
              rs.getString("ul"),
              rs.getString("requestType"),
              rs.getString("status"),
              rs.getString("employeeAssigned"),
              rs.getString("deviceID"),
              rs.getString("device")));
    }
    return fin;
  }

  public ComputerRequest getComputerRequest(int id) throws SQLException {
    String sql =
        "SELECT * FROM "
            + "ComputerRequest join ServiceRequest "
            + "on ComputerRequest.requestID = ServiceRequest.requestID "
            + "where requestID = "
            + id
            + ";";
    ResultSet rs = Adb.processQuery(sql);
    rs.next();
    return new ComputerRequest(
        rs.getInt("requestID"),
        rs.getString("name"),
        rs.getString("idNum"),
        rs.getString("location"),
        rs.getString("description"),
        rs.getString("ul"),
        rs.getString("requestType"),
        rs.getString("status"),
        rs.getString("employeeAssigned"),
        rs.getString("deviceID"),
        rs.getString("device"));
  }
}
