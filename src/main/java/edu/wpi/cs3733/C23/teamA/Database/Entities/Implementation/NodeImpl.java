package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;
import static java.lang.Integer.parseInt;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class NodeImpl extends Observable implements IDatabaseAPI<NodeEntity, String> {
  // done
  private static final NodeImpl instance = new NodeImpl();

  private ArrayList<NodeEntity> nodes;

  private NodeImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<NodeEntity> criteria = builder.createQuery(NodeEntity.class);
    criteria.from(NodeEntity.class);
    List<NodeEntity> records = session.createQuery(criteria).getResultList();
    nodes = (ArrayList) records;
    session.close();
  }

  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<NodeEntity> criteria = builder.createQuery(NodeEntity.class);
    criteria.from(NodeEntity.class);
    List<NodeEntity> records = session.createQuery(criteria).getResultList();
    nodes = (ArrayList) records;
    session.close();
  }

  public List<NodeEntity> getAll() {
    return nodes;
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/node.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("node,xcoord,ycoord,building,floor\n");
    for (NodeEntity nod : nodes) {
      fileWriter.write(
          nod.getNodeid()
              + ","
              + nod.getXcoord()
              + ","
              + nod.getYcoord()
              + ","
              + nod.getBuilding()
              + ","
              + nod.getFloor()
              + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();
    String hql = "delete from NodeEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    nodes.clear();

    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File node = new File(filename);

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(node);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(new NodeEntity(b[0], parseInt(b[1]), parseInt(b[2]), b[3], b[4]));
      count++;
      nodes.add(session.get(NodeEntity.class, b[0]));
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
    session.close();
  }

  public void add(NodeEntity n) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(n);
    nodes.add(n);
    tx.commit();
    session.close();
  }

  public void delete(String n) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    ListIterator<NodeEntity> li = nodes.listIterator();
    while (li.hasNext()) {
      if (li.next().getNodeid().equals(n)) {
        li.remove();
      }
    }
    session.remove(session.get(NodeEntity.class, n));
    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void update(String ID, NodeEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<NodeEntity> li = nodes.listIterator();
    while (li.hasNext()) {
      if (li.next().getNodeid().equals(ID)) {
        li.remove();
      }
    }

    session
        .createMutationQuery(
            "UPDATE NodeEntity nod SET "
                + "nod.nodeid = '"
                + obj.getNodeid()
                + "', nod.floor = '"
                + obj.getFloor()
                + "', nod.ycoord = "
                + obj.getYcoord()
                + ", nod.xcoord = "
                + obj.getXcoord()
                + ", nod.building = '"
                + obj.getBuilding()
                + "' WHERE nod.nodeid = '"
                + ID
                + "'")
        .executeUpdate();

    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public List<NodeEntity> getNodeOnFloor(String floor) {
    // changed == to .equals() - audrey
    return nodes.stream().filter(nodeEntity -> nodeEntity.getFloor().equals(floor)).toList();
  }

  public List<String> getAllIDs() {
    return getAll().stream().map(nodeEntity -> nodeEntity.getNodeid()).toList();
  }

  public NodeEntity get(String ID) {
    for (NodeEntity ser : nodes) {
      if (ser.getNodeid().equals(ID)) return ser;
    }
    return null;
  }

  public static NodeImpl getInstance() {
    return instance;
  }
}
