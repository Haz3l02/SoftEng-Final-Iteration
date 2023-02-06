package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Employee {
  @Getter private static final String tableName = "employee";

  @Getter @Setter private String employeeID;
  @Getter @Setter private String username;
  @Getter @Setter private String password;
  @Getter @Setter private String job;
  @Getter @Setter private String name;

  public Employee() {}

  public Employee(String employeeID, String username, String password, String job, String name) {
    this.employeeID = employeeID;
    this.username = username;
    this.password = password;
    this.job = job;
    this.name = name;
  }

  // Returns employeeID if correct, empty String is rejected
  public ArrayList<String> checkPass(String user, String pwd) throws SQLException, SQLException {
    ArrayList<String> info = new ArrayList<String>();
    String sql = "SELECT * FROM Employee where username = '" + user + "';";
    ResultSet rs = Adb.processQuery(sql);
    if (rs.next()) {
      if (rs.getString("password").equals(pwd)) {
        info.add(rs.getString("employeeID"));
        info.add(rs.getString("job"));
        // return rs.getString("employeeID");
        return info;
      }
    }
    info.add("");
    info.add("");
    return info;
  }
}
