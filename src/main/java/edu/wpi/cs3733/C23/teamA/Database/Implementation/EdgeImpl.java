package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class EdgeImpl implements IDatabaseAPI<EdgeEntity, String> {
  private static final EdgeImpl instance = new EdgeImpl();

  private List<EdgeEntity> edges;

  public EdgeImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EdgeEntity> criteria = builder.createQuery(EdgeEntity.class);
    criteria.from(EdgeEntity.class);
    List<EdgeEntity> records = session.createQuery(criteria).getResultList();
    edges = records;
    session.close();
  }

  public List<EdgeEntity> getAll() {
    return edges;
  }

  /**
   * Finds connecting edges (edges that go to a certain node and all edges that come from that
   * nodea)
   *
   * @param e
   * @return Hashmap of all edges that go to a node (node2 is the node) and a List of Edges that
   *     come from the node (node1 is the node)
   */
  public HashMap<EdgeEntity, List<EdgeEntity>> nodeVectors(NodeEntity e) {
    Session session = getSessionFactory().openSession();
    HashMap<EdgeEntity, List<EdgeEntity>> vectors = new HashMap<>();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EdgeEntity> criteria = builder.createQuery(EdgeEntity.class);
    Root<EdgeEntity> item = criteria.from(EdgeEntity.class);

    // Find edge that goes to node e
    criteria.select(item).where(builder.equal(item.get("node2"), e));
    List<EdgeEntity> records = session.createQuery(criteria).getResultList();
    for (EdgeEntity r : records) {
      vectors.put(
          r,
          session
              .createQuery( // Find all edges that leave node e
                  criteria.select(item).where(builder.equal(item.get("node1"), e)))
              .getResultList());
    }
    session.close();
    return vectors;
  }

  public void exportToCSV(String filename) throws IOException {
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File csvFile = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("edgeid,node1,node2\n");
    for (EdgeEntity edge : edges) {
      fileWriter.write(
          edge.getEdgeid()
              + ","
              + edge.getNode1().getNodeid()
              + ","
              + edge.getNode2().getNodeid()
              + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();
    String hql = "delete from EdgeEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    edges.clear();
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";
    File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(loc);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(
          new EdgeEntity(
              session.get(NodeEntity.class, b[1]), session.get(NodeEntity.class, b[2]), b[0]));
      edges.add(session.get(EdgeEntity.class, b[0]));

      count++;
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
    session.close();
  }

  public void add(EdgeEntity e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(e);
    edges.add(e);
    tx.commit();
    session.close();
  }

  public void delete(String e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    ListIterator<EdgeEntity> li = edges.listIterator();
    while (li.hasNext()) {
      if (li.next().getEdgeid().equals(e)) {
        li.remove();
      }
    }
    session.remove(session.get(EdgeEntity.class, e));
    tx.commit();
    session.close();
  }

  /**
   * Delete the node and link the edges involving the node back together. It functions by making new
   * edges from the node going to node e to the node going away from node e. Every edge that
   * connects to the node e's edge will be repaired like this.
   *
   * @param e NodeEntity that must be deleted.
   */
  public void collapseNode(NodeEntity e) {
    Session session = getSessionFactory().openSession();
    EdgeEntity newEdge;
    HashMap<EdgeEntity, List<EdgeEntity>> vec = nodeVectors(e);
    Transaction tx = session.beginTransaction();
    for (EdgeEntity n : vec.keySet()) { // n - > e
      List<EdgeEntity> edges = vec.get(n);
      for (EdgeEntity m : edges) { // e - > m
        newEdge = new EdgeEntity(n.getNode1(), m.getNode2());
        session.merge(newEdge);
        try {
          delete(m.getEdgeid());
        } catch (Exception exc) {
        }
        ;
      }
      delete(n.getEdgeid());
    }
    tx.commit();
    session.close();
  }

  public void update(String s, EdgeEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<EdgeEntity> li = edges.listIterator();
    while (li.hasNext()) {
      if (li.next().getEdgeid().equals(s)) {
        li.remove();
      }
    }

    EdgeEntity edg = session.get(EdgeEntity.class, s);

    edg.setNode1(obj.getNode1());
    edg.setNode2(obj.getNode2());

    edges.add(edg);

    tx.commit();
    session.close();
  }

  public EdgeEntity get(String ID) {

    for (EdgeEntity ser : edges) {
      if (ser.getEdgeid().equals(ID)) return ser;
    }
    return null;
  }

  public static EdgeImpl getInstance() {
    return instance;
  }
}
