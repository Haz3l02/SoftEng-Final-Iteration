package edu.wpi.cs3733.C23.teamA.Database.Entities;

import edu.wpi.cs3733.C23.teamA.enums.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patienttransportrequest")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class PatientTransportRequestEntity extends ServiceRequestEntity {

  //  @Id
  //  @Column(name = "requestid", nullable = false)
  //  @Setter
  //  @Getter
  //  private int requestid;

  @Basic
  @Column(name = "patientname", nullable = false, length = -1)
  @Setter
  @Getter
  private String patientName;

  @Basic
  @Column(name = "patientid", nullable = false, length = -1)
  @Setter
  @Getter
  private String patientID;

  @ManyToOne
  @JoinColumn(
      name = "moveto",
      foreignKey =
          @ForeignKey(
              name = "longname",
              foreignKeyDefinition =
                  "FOREIGN KEY (moveto) REFERENCES locationname(longname) ON UPDATE CASCADE ON DELETE SET NULL"))
  @Setter
  @Getter
  private LocationNameEntity moveTo;

  @Basic
  @Column(name = "equipment", nullable = false, length = -1)
  @Setter
  @Getter
  private String equipment;

  @Basic
  @Column(name = "gender", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Basic
  @Column(name = "mode", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private ModeOfTransfer mode;

  @Basic
  @Column(name = "mobility", nullable = false, length = -1)
  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  private Mobility mobility;

  @Basic
  @Column(name = "isbaby", nullable = false, length = -1)
  @Setter
  @Getter
  private boolean isBaby;

  @Basic
  @Column(name = "isimmunecomp", nullable = false, length = -1)
  @Setter
  @Getter
  private boolean isImmuneComp;

  public PatientTransportRequestEntity() {}

  public PatientTransportRequestEntity(
      int requestid,
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      String patientName,
      String patientID,
      LocationNameEntity moveTo, // moveTo--there's already a location in servicerequest super
      String equipment,
      Gender gender,
      ModeOfTransfer mode,
      Mobility mobility,
      boolean isBaby,
      boolean isImmuneComp,
      Timestamp date) {
    super(
        requestid,
        name,
        employee,
        location,
        description,
        urgency,
        requesttype,
        status,
        employeeassigned,
        date);
    this.patientName = patientName;
    this.patientID = patientID;
    this.moveTo = moveTo;
    this.equipment = equipment;
    this.gender = gender;
    this.mode = mode;
    this.mobility = mobility;
    this.isBaby = isBaby;
    this.isImmuneComp = isImmuneComp;
  }

  public PatientTransportRequestEntity(
      String name,
      EmployeeEntity employee,
      LocationNameEntity location,
      String description,
      UrgencyLevel urgency,
      RequestType requesttype,
      Status status,
      EmployeeEntity employeeassigned,
      String patientName,
      String patientID,
      LocationNameEntity moveTo, // moveTo--there's already a location in servicerequest super
      String equipment,
      Gender gender,
      ModeOfTransfer mode,
      Mobility mobility,
      boolean isBaby,
      boolean isImmuneComp) {
    super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
    this.patientName = patientName;
    this.patientID = patientID;
    this.moveTo = moveTo;
    this.equipment = equipment;
    this.gender = gender;
    this.mode = mode;
    this.mobility = mobility;
    this.isBaby = isBaby;
    this.isImmuneComp = isImmuneComp;
  }
}
