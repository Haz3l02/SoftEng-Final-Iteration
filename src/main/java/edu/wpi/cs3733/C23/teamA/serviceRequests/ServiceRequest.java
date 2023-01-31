package edu.wpi.cs3733.C23.teamA.serviceRequests;

import edu.wpi.cs3733.C23.teamA.databases.Adb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class ServiceRequest {
  @Getter @Setter int requestID;
  @Getter @Setter String name;
  @Getter @Setter String idNum;
  @Getter @Setter String location;
  @Getter @Setter String description;
  @Getter @Setter String ul;
  @Getter @Setter String requestType;
  @Getter @Setter String status;
  @Getter @Setter String employeeAssigned;

  public ServiceRequest(
      int requestID,
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned) {
    this.requestID = requestID;
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.ul = ul;
    this.requestType = requestType;
    this.status = status;
    this.employeeAssigned = employeeAssigned;
  }

  public ServiceRequest(
      String name,
      String idNum,
      String location,
      String description,
      String ul,
      String requestType,
      String status,
      String employeeAssigned) {
    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.ul = ul;
    this.requestType = requestType;
    this.status = status;
    this.employeeAssigned = employeeAssigned;
  }

  public ServiceRequest() {}

  public void updateStatusEmployee(int id, String status, String employeeAssigned)
      throws SQLException {
    String sql =
        "update servicerequest set status = '"
            + status
            + "', employeeAssigned = '"
            + employeeAssigned
            + "' where requestID = "
            + id
            + ";";
    Adb.processUpdate(sql);
  }

  public ArrayList<ServiceRequest> getServiceRequestsByID(String id) throws SQLException {
    ArrayList<ServiceRequest> fin = new ArrayList<>();
    String sql = "SELECT * FROM " + "ServiceRequest " + "where idNum = '" + id + "';";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      fin.add(
          new ServiceRequest(
              rs.getInt("requestID"),
              rs.getString("name"),
              rs.getString("idNum"),
              rs.getString("location"),
              rs.getString("description"),
              rs.getString("ul"),
              rs.getString("requestType"),
              rs.getString("status"),
              rs.getString("employeeAssigned")));
    }
    return fin;
  }
}
