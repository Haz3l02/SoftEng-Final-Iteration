package edu.wpi.cs3733.C23.teamA.databases;

import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

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


    public ServiceRequest(int requestID, String name, String idNum, String location, String description, String ul, String requestType, String status, String employeeAssigned) {
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

    public ServiceRequest(String name, String idNum, String location, String description, String ul, String requestType, String status, String employeeAssigned) {
        this.name = name;
        this.idNum = idNum;
        this.location = location;
        this.description = description;
        this.ul = ul;
        this.requestType = requestType;
        this.status = status;
        this.employeeAssigned = employeeAssigned;
    }

    public ServiceRequest() {
    }


    public void updateStatusEmployee (int id, String status, String employeeAssigned) throws SQLException {
        String sql = "update servicerequest set status = '" +status+ "', employeeAssigned = '" + employeeAssigned + "' where requestID = "+id+";";
        Adb.processUpdate(sql);
    }
}
