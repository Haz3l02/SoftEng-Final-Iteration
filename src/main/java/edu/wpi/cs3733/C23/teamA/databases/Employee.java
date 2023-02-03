package edu.wpi.cs3733.C23.teamA.databases;

import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    @Getter
    private static final String tableName = "employee";

    @Getter @Setter private String employeeID;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private String job;
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;


    public Employee() {
    }

    public Employee(String employeeID, String username, String password, String job, String firstName, String lastName) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.job = job;
        this.firstName = firstName;
        this.lastName = lastName;
    }



    public boolean checkPass(String user, String pwd) throws SQLException {
        String sql = "SELECT * FROM Employee where username = '" + user + "';";
        ResultSet rs = Adb.processQuery(sql);
        if(rs.next()){
            if (rs.getString("password").equals(pwd)) return true;
        }
        return false;
    }
}
