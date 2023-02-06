package edu.wpi.cs3733.C23.teamA.hibernateDB;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.Session;
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
  @JoinColumn(name = "employeeid", foreignKey = @ForeignKey(name = "employeeid"))
  @Setter
  @Getter
  private EmployeeEntity employee;

  @ManyToOne
  @JoinColumn(name = "location", foreignKey = @ForeignKey(name = "longname"))
  @Setter
  @Getter
  private LocationnameEntity location;

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
      LocationnameEntity location,
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
      LocationnameEntity location,
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

  //  public static List<ServicerequestEntity> findAllService() {
  //    Session session = getSessionFactory().openSession();
  //    CriteriaBuilder builder = session.getCriteriaBuilder();
  //    CriteriaQuery<ServicerequestEntity> criteria = builder.createQuery(ServicerequestEntity);
  //    //criteria.from();
  //    List<ServicerequestEntity> data = session.createQuery(criteria).getResultList();
  //    return data;
  //  }

  public static ArrayList<ServicerequestEntity> getServiceByEmployee(String id, Session session) {
    List<ServicerequestEntity> all = getAllRecords(ServicerequestEntity.class, session);
    ArrayList<ServicerequestEntity> fin = new ArrayList<>();
    for (ServicerequestEntity ser : all) {
      if (ser.getEmployee().getEmployeeid().equals(id)) {
        fin.add(ser);
      }
    }
    return fin;
  }
}
