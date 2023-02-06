package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    Session session = ADBSingletonClass.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // EmployeeEntity person = new EmployeeEntity("123", "staff", "staff", "Medical", "Wilson
    // Wong");
    // session.persist(person);

    // LocationnameEntity loc =
    //    new LocationnameEntity("Anesthesia Conf Floor L1", "Conf C001L1", "CONF");
    // session.persist(loc);
    EmployeeEntity person = session.get(EmployeeEntity.class, "123");
    LocationnameEntity loc = session.get(LocationnameEntity.class, "Anesthesia Conf Floor L1");
    ComputerrequestEntity com =
        new ComputerrequestEntity(
            "PC help",
            person,
            loc,
            "Need help",
            ServicerequestEntity.Urgency.EXTREMELY_URGENT,
            ServicerequestEntity.RequestType.COMPUTER,
            ServicerequestEntity.Status.BLANK,
            "Harrison",
            "365",
            ComputerrequestEntity.Device.DESKTOP);
    session.persist(com);

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
