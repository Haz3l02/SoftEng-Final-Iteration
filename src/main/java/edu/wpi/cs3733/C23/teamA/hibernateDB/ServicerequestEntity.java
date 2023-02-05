package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "servicerequest", schema = "public", catalog = "dba")
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
  @Column(name = "name", nullable = true, length = -1)
  @Setter
  @Getter
  private String name;

  @Basic
  @Column(name = "idnum", nullable = true, length = -1)
  @Setter
  @Getter
  private String idnum;

  @Basic
  @Column(name = "location", nullable = true, length = -1)
  @Setter
  @Getter
  private String location;

  @Basic
  @Column(name = "description", nullable = true, length = -1)
  @Setter
  @Getter
  private String description;

  @Basic
  @Column(name = "ul", nullable = true, length = -1)
  @Setter
  @Getter
  private String ul;

  @Basic
  @Column(name = "requesttype", nullable = true, length = -1)
  @Setter
  @Getter
  private String requesttype;

  @Basic
  @Column(name = "status", nullable = true, length = -1)
  @Setter
  @Getter
  private String status;

  @Basic
  @Column(name = "employeeassigned", nullable = true, length = -1)
  @Setter
  @Getter
  private String employeeassigned;
}
