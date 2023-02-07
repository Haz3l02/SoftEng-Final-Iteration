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

    EmployeeEntity per = session.get(EmployeeEntity.class, "123");
    per.setName("Wilson Wong");

    tx.commit();
    session.close();
    // getSessionFactory().close();
  }
}
