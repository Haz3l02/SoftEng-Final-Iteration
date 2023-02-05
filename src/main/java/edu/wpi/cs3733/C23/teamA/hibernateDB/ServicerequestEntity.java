package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "servicerequest", catalog = "dba")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServicerequestEntity {
  @TableGenerator(name = "yourTableGenerator", allocationSize = 1, initialValue = 1)
  @GeneratedValue(
      strategy = GenerationType.TABLE,
      generator = "yourTableGenerator") // (strategy = GenerationType.AUTO,)
  @Id
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @Column(name = "requestid")
  @Setter
  @Getter
  private int requestid;

  @Basic
  @Column(name = "name", nullable = false, length = -1)
  @Setter
  @Getter
  private String name;

  @ManyToOne
  @JoinColumn(name = "employeeid", foreignKey = @ForeignKey(name = "employee_fk"))
  @Setter
  @Getter
  private EmployeeEntity employee;

  @Basic
  @Column(name = "location", nullable = false, length = -1)
  @Setter
  @Getter
  private String location;

  @Basic
  @Column(name = "description", nullable = false, length = -1)
  @Setter
  @Getter
  private String description;

  @Basic
  @Column(name = "urgency", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Urgency urgency;

  @Basic
  @Column(name = "requesttype", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private RequestType requesttype;

  @Basic
  @Column(name = "status", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Status status;

  @Basic
  @Column(name = "employeeassigned", nullable = false, length = -1)
  @Setter
  @Getter
  private String employeeassigned;

  @Getter
  @Column(nullable = false)
  @CreationTimestamp
  private Date date;

  public enum Urgency {
    EXTREMELY_URGENT("Extremely urgent"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    // FILL OUT TOMORROW WITH ISABELLA
    @NonNull public final String urgency;

    Urgency(@NonNull String urgency) {
      this.urgency = urgency;
    }
  }

  public enum RequestType {
    SECURITY("Security"),
    COMPUTER("Computer"),
    SANITATION("Sanitation");

    // FILL OUT TOMORROW WITH ISABELLA
    @NonNull public final String requesttype;

    RequestType(@NonNull String requesttype) {
      this.requesttype = requesttype;
    }
  }

  public enum Status {
    BLANK("Blank"),
    PROCESSING("Processing"),
    DONE("Done");

    @NonNull public final String status;

    Status(@NonNull String status) {
      this.status = status;
    }
  }

  public ServicerequestEntity() {}

  public ServicerequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency urgency,
      RequestType requesttype,
      Status status,
      String employeeassigned,
      Date date) {
    this.requestid = requestid;
    this.name = name;
    this.employee = employee;
    this.location = location;
    this.description = description;
    this.urgency = urgency;
    this.requesttype = requesttype;
    this.status = status;
    this.employeeassigned = employeeassigned;
    this.date = date;
  }

  public ServicerequestEntity(
      String name,
      EmployeeEntity employee,
      String location,
      String description,
      Urgency urgency,
      RequestType requesttype,
      Status status,
      String employeeassigned) {
    this.name = name;
    this.employee = employee;
    this.location = location;
    this.description = description;
    this.urgency = urgency;
    this.requesttype = requesttype;
    this.status = status;
    this.employeeassigned = employeeassigned;
  }
}
