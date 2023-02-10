package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import static java.lang.Integer.parseInt;

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

    String hql = "delete from MoveEntity";
    Query q = session.createQuery(hql);
    q.executeUpdate();

    hql = "delete from EdgeEntity";
    q = session.createQuery(hql);
    q.executeUpdate();

    hql = "delete from NodeEntity";
    q = session.createQuery(hql);
    q.executeUpdate();




    File nodes = new File("node.csv");
    File edges = new File("edge.csv");
    File moves = new File("move.csv");
    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(nodes);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()){
      String[] b =  read.nextLine().split(",");
      session.persist(new NodeEntity(b[0], parseInt(b[1]), parseInt(b[2]), b[3], b[4]));
      count++;
      if (count%20==0) {
        session.flush();
        session.clear();
      }
    }


    read = new Scanner(edges);
    read.nextLine();
    while (read.hasNextLine()){
      String[] b =  read.nextLine().split(",");
      session.persist(new EdgeEntity(session.get(NodeEntity.class, b[1]), session.get(NodeEntity.class, b[2]), b[0]));
      count++;
      if (count%20==0) {
        session.flush();
        session.clear();
      }
    }


    read = new Scanner(moves);
    read.nextLine();
    while (read.hasNextLine()){
      String[] b =  read.nextLine().split(",");
      session.persist(new MoveEntity(session.get(NodeEntity.class, b[2]), session.get(LocationNameEntity.class, b[1]), Timestamp.valueOf(b[0])));
      count++;
      if (count%20==0) {
        session.flush();
        session.clear();
      }
    }


  }

}
