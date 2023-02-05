package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
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

  @Basic
  @Column(name = "idnum", nullable = false, length = -1)
  @Setter
  @Getter
  private String idnum;

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
  @Column(name = "ul", nullable = false, length = -1)
  @Setter
  @Getter
  private String ul;

  @Basic
  @Column(name = "requesttype", nullable = false, length = -1)
  @Setter
  @Getter
  private String requesttype;

  @Basic
  @Column(name = "status", nullable = false, length = -1)
  @Setter
  @Getter
  private String status;

  @Basic
  @Column(name = "employeeassigned", nullable = false, length = -1)
  @Setter
  @Getter
  private String employeeassigned;



  public enum Urgency {
    VERY_URGENT("very urgent"),
    MODERATELY_URGENT("moderately urgent"),
    NOT_URGENT("not urgent");


    //FILL OUT TOMORROW WITH ISABELLA
    @NonNull
    public final String Urgency;

    Urgency(@NonNull String urgency) {
      Urgency = urgency;
    }

  }


  public enum Status {
    BLANK("Blank"),
    PROCESSING("Processing"),
    DONE("Done");

    @NonNull public final String status;

    Status(@NonNull String statusVal) {
      status = statusVal;
    }
  }

}
