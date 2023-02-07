package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.*;

import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // EmployeeEntity wong = new EmployeeEntity("123", "staff", "staff", "Maintenance", "Wilson
    // Wong");
    // session.persist(wong);

    EmployeeEntity wong = session.get(EmployeeEntity.class, "123");
    LocationnameEntity loc = session.get(LocationnameEntity.class, "Ultrasound Floor L1");

    /*ComputerrequestEntity comp =
        new ComputerrequestEntity(
            "Help",
            wong,
            loc,
            "need help",
            ServicerequestEntity.Urgency.LOW,
            ServicerequestEntity.RequestType.COMPUTER,
            ServicerequestEntity.Status.BLANK,
            "John",
            "My PC",
            ComputerrequestEntity.Device.DESKTOP);
    session.persist(comp);*/

    SecurityrequestEntity comp =
        new SecurityrequestEntity(
            "Security threat",
            wong,
            loc,
            "loitering",
            ServicerequestEntity.Urgency.LOW,
            ServicerequestEntity.RequestType.SECURITY,
            ServicerequestEntity.Status.BLANK,
            "swag",
            SecurityrequestEntity.Assistance.HARASSMENT,
            "401-330-4830");
    session.persist(comp);

    tx.commit();
    session.close();
    // getSessionFactory().close();
  }
}
