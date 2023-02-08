package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.*;

import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import jakarta.persistence.*;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {

    // AApp.launch(AApp.class, args);

    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // ComputerrequestEntity per = session.get(ComputerrequestEntity.class, 2);
    // per.setUrgency(ServicerequestEntity.Urgency.MEDIUM);
    // per.setDevice(ComputerrequestEntity.Device.MONITOR);
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

    //    SecurityrequestEntity comp =
    //        new SecurityrequestEntity(
    //            "Security threat",
    //            wong,
    //            loc,
    //            "loitering",
    //            ServicerequestEntity.Urgency.LOW,
    //            ServicerequestEntity.RequestType.SECURITY,
    //            ServicerequestEntity.Status.BLANK,
    //            "swag",
    //            SecurityrequestEntity.Assistance.HARASSMENT,
    //            "401-330-4830");
    //    session.persist(comp);
    //
    //    tx.commit();
    //    session.close();
    //    if (session.getTransaction().isActive()) {
    //      System.out.println("active");
    //    }
    AApp.launch(AApp.class, args);

    // getSessionFactory().close();

    //    com.editComputerRequest(
    //       3, "jay", "bruh", "bruh", "burhbruh", "HIGH", "bruh", "bruh", "john", "bruh", "bruh");
    //    SanitationrequestEntity sanreq =
    //        new SanitationrequestEntity(
    //            "Clean",
    //            "123",
    //            "there",
    //            "stuff",
    //            ServicerequestEntity.Urgency.HIGH,
    //            ServicerequestEntity.RequestType.SANITATION,
    //            ServicerequestEntity.Status.BLANK,
    //            "John",
    //            SanitationrequestEntity.Category.BIOHAZARD);
    //
    //    session.persist(sanreq);
    //    tx.commit();
    //    session.close();

    // getSessionFactory().close();
  }
}
