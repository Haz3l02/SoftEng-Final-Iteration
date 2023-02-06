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
    //    EmployeeEntity person = new EmployeeEntity("123", "staff", "staff", "Medical", "Wilson
    // Wong");
    //    session.persist(person);

    EmployeeEntity person2 = session.get(EmployeeEntity.class, "123");

    LocationnameEntity loc = session.get(LocationnameEntity.class, "Anesthesia Conf Floor L1");

    SecurityrequestEntity sec =
        new SecurityrequestEntity(
            "Need backup",
            person2,
            loc,
            "HELP",
            ServicerequestEntity.Urgency.EXTREMELY_URGENT,
            ServicerequestEntity.RequestType.SECURITY,
            ServicerequestEntity.Status.BLANK,
            "Wong",
            SecurityrequestEntity.Assistance.HARASSMENT,
            "911");
    session.persist(sec);
    /*ComputerrequestEntity com =
        new ComputerrequestEntity(
            "PC help",
            person2,
            loc,
            "Need help",
            ServicerequestEntity.Urgency.EXTREMELY_URGENT,
            ServicerequestEntity.RequestType.COMPUTER,
            ServicerequestEntity.Status.BLANK,
            "Harrison",
            "365",
            ComputerrequestEntity.Device.DESKTOP);
    session.persist(com);*/
    //    List<ServicerequestEntity> reqs = getAllRecords(ServicerequestEntity.class, session);
    //
    //    for (ServicerequestEntity ser : reqs) {
    //      System.out.println("Hi");
    //      System.out.println(ser.getRequestid());
    //    }

    tx.commit();
    session.close();

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
  }
}
