package edu.wpi.cs3733.C23.teamA.hibernateDB;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum ADBSingleton {
  _ADB;

  Session session;

  ADBSingleton() { // Should not be void, fix enum
    try {
      SessionFactory sessionFactory =
          new Configuration()
              .configure("edu.wpi.cs3733.C23.teamA.hibernate.cfg.xml")
              .buildSessionFactory();
      session = sessionFactory.openSession();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Session getSession() {
    return session;
  }
}
