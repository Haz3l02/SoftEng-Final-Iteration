package edu.wpi.cs3733.C23.teamA.Database.Entities;

import edu.wpi.cs3733.C23.teamA.enums.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audiovisualrequest")
@PrimaryKeyJoinColumn(name = "requestid", foreignKey = @ForeignKey(name = "requestid"))
public class AudioVisualRequestEntity extends ServiceRequestEntity {

    @Basic
    @Column(name = "installationrequired", nullable = false)
    @Getter
    @Setter
    private boolean installationrequired;

    @Basic
    @Column(name = "numdevices", nullable = false)
    @Getter
    @Setter
    private int numdevices;

    @Basic
    @Column(name = "avdevice", nullable = false, length = -1)
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private AVDevice avdevice;

    @Basic
    @Column(name = "subject", nullable = false, length = -1)
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Basic
    @Column(name = "additionalequipment", nullable = false)
    @Getter
    @Setter
    private String additionalequipment;

    @Basic
    @Column(name = "returndate", nullable = false)
    @Getter
    @Setter
    private LocalDate returndate;


    public AudioVisualRequestEntity() {}

    public AudioVisualRequestEntity(
            int requestid,
            String name,
            EmployeeEntity employee,
            LocationNameEntity location,
            String description,
            UrgencyLevel urgency,
            RequestType requesttype,
            Status status,
            String employeeassigned,
            Timestamp date,
            boolean installationrequired,
            int numdevices,
            AVDevice avdevice,
            Subject subject,
            String additionalequipment,
            LocalDate returndate) {
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
        this.installationrequired = installationrequired;
        this.numdevices = numdevices;
        this.avdevice = avdevice;
        this.subject = subject;
        this.additionalequipment = additionalequipment;
        this.returndate = returndate;
    }

    public AudioVisualRequestEntity(
            String name,
            EmployeeEntity employee,
            LocationNameEntity location,
            String description,
            UrgencyLevel urgency,
            RequestType requesttype,
            Status status,
            String employeeassigned,
            boolean installationrequired,
            int numdevices,
            AVDevice avdevice,
            Subject subject,
            String additionalequipment,
            LocalDate returndate) {
        super(name, employee, location, description, urgency, requesttype, status, employeeassigned);
        this.installationrequired = installationrequired;
        this.numdevices = numdevices;
        this.avdevice = avdevice;
        this.subject = subject;
        this.additionalequipment = additionalequipment;
        this.returndate = returndate;

    }
}