package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.Entities.ComputerRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

public class ComputerRequestImpl {
    public void add(ComputerRequestEntity c) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(c);

        tx.commit();
        session.close();
    }
    public void delete(ComputerRequestEntity c) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(c);

        tx.commit();
        session.close();
    }
}
