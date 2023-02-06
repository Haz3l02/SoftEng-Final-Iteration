package edu.wpi.cs3733.C23.teamA.hibernateDB;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class ADBSingletonClass {

  public static SessionFactory factory;

  private ADBSingletonClass() {}

  public static synchronized SessionFactory getSessionFactory() {
    if (factory == null) {
      try {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder builder =
            new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        factory = configuration.buildSessionFactory(builder.build());
      } catch (Throwable ex) {
        System.err.println("Initial SessionFactory creation failed." + ex);
        ex.printStackTrace();
        throw new ExceptionInInitializerError(ex);
      }
    }
    return factory;
  }
}
