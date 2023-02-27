package edu.wpi.cs3733.C23.teamA.Database.Entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "messageboard")
public class MessageBoardEntity {

  @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(
      name = "sender",
      foreignKey =
          @ForeignKey(
              name = "sender_fk",
              foreignKeyDefinition = "FOREIGN KEY (sender) REFERENCES employee(username)"))
  private EmployeeEntity sender;

  @Id
  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(
      name = "receiver",
      foreignKey =
          @ForeignKey(
              name = "receiver_fk",
              foreignKeyDefinition = "FOREIGN KEY (receiver) REFERENCES employee(username)"))
  private EmployeeEntity receiver;

  @Column(name = "title", nullable = false)
  @Getter
  @Setter
  private String title;

  @Column(name = "message", nullable = false)
  @Getter
  @Setter
  private String message;

  @Id
  @Column(name = "time_sent", nullable = false)
  @Getter
  @Setter
  private Timestamp timeSent;

  public MessageBoardEntity() {}

  public MessageBoardEntity(
      EmployeeEntity sender,
      EmployeeEntity receiver,
      String title,
      String message,
      Timestamp timeSent) {
    this.sender = sender;
    this.receiver = receiver;
    this.title = title;
    this.message = message;
    this.timeSent = timeSent;
  }
}
