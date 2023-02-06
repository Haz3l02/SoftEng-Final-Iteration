package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import org.hibernate.Session;
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

  public static <T> List<T> getAllRecords(Class<T> type, Session session) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<T> criteria = builder.createQuery(type);
    criteria.from(type);
    List<T> records = session.createQuery(criteria).getResultList();
    return records;
  }
}
