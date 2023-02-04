package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;

@Entity
@Table(name = "servicerequest", schema = "public", catalog = "dba")
public class ServicerequestEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "requestid", nullable = false)
    private int requestid;
    @Basic
    @Column(name = "name", nullable = true, length = -1)
    private String name;
    @Basic
    @Column(name = "idnum", nullable = true, length = -1)
    private String idnum;
    @Basic
    @Column(name = "location", nullable = true, length = -1)
    private String location;
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;
    @Basic
    @Column(name = "ul", nullable = true, length = -1)
    private String ul;
    @Basic
    @Column(name = "requesttype", nullable = true, length = -1)
    private String requesttype;
    @Basic
    @Column(name = "status", nullable = true, length = -1)
    private String status;
    @Basic
    @Column(name = "employeeassigned", nullable = true, length = -1)
    private String employeeassigned;

    public int getRequestid() {
        return requestid;
    }

    public void setRequestid(int requestid) {
        this.requestid = requestid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeassigned() {
        return employeeassigned;
    }

    public void setEmployeeassigned(String employeeassigned) {
        this.employeeassigned = employeeassigned;
    }
}
