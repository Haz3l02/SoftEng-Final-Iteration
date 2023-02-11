package edu.wpi.cs3733.C23.teamA.Database.API;

import edu.wpi.cs3733.C23.teamA.Database.Implementation.EdgeImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.LocationNameImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.MoveImpl;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.NodeImpl;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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

  public static <T> List<T> getAllRecords(Class<T> type, Session session) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<T> criteria = builder.createQuery(type);
    criteria.from(type);
    List<T> records = session.createQuery(criteria).getResultList();
    return records;
  }

  public static void rewriteNodesEdgesMoves(Session session) throws FileNotFoundException {
    Transaction tx = session.beginTransaction();
    String hql = "delete from MoveEntity";
    Query q = session.createQuery(hql);
    q.executeUpdate();

    hql = "delete from EdgeEntity";
    q = session.createQuery(hql);
    q.executeUpdate();

    hql = "delete from NodeEntity";
    q = session.createQuery(hql);
    q.executeUpdate();

    hql = "delete from LocationNameEntity";
    q = session.createQuery(hql);
    q.executeUpdate();
    tx.commit();

    File nodes = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/nodes.csv");
    File edges = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/edges.csv");
    File moves = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/move.csv");
    File locationname =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/locationname.csv");
    NodeImpl node = new NodeImpl();
    EdgeImpl edge = new EdgeImpl();
    LocationNameImpl location = new LocationNameImpl();
    MoveImpl move = new MoveImpl();
    node.importFromCSV();
    edge.importFromCSV();
    location.importFromCSV();
    move.importFromCSV();
  }
}
