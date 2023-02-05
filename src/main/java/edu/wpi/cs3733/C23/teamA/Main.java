package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.hibernateDB.ServicerequestEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {

  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
    SessionFactory factory = configuration.buildSessionFactory();
    Session session = factory.openSession();

    Transaction tx = session.beginTransaction();
    ServicerequestEntity req = new ServicerequestEntity();
    req.setName("Wilson Wong");
    req.setIdnum("69420");
    req.setUl("High");
    req.setStatus("Blank");
    req.setDescription("Stuff");
    req.setLocation("Places");
    req.setEmployeeassigned("Wilson Wong");
    req.setRequesttype("SanitationRequest");

    session.persist(req);
    tx.commit();
    session.close();
  }
}
