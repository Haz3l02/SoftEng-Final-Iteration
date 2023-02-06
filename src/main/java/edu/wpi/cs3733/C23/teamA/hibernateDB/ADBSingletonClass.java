package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class ADBSingletonClass {

  public static SessionFactory factory;

  private ADBSingletonClass() {}

  public static synchronized SessionFactory getSessionFactory() {
    StandardServiceRegistry registry;
    if (factory == null) {
      registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
      factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    return factory;
  }



  public static <T> List<T> loadAllData(Class<T> type, Session session) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<T> criteria = builder.createQuery(type);
    criteria.from(type);
    List<T> data = session.createQuery(criteria).getResultList();
    return data;
  }
}
