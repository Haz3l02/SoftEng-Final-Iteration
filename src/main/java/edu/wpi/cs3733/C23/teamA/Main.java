package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass;
import edu.wpi.cs3733.C23.teamA.hibernateDB.EmployeeEntity;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    SessionFactory sessionFactory = ADBSingletonClass.getSessionFactory();
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    EmployeeEntity person = new EmployeeEntity("123", "staff", "staff", "Medical", "Wilson Wong");
    // ComputerrequestEntity com = new ComputerrequestEntity("PC help", );
    session.persist(person);

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
