package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;
import static java.lang.Integer.parseInt;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
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

public class NodeImpl implements IDatabaseAPI<NodeEntity, String> {
  // done

  private ArrayList<NodeEntity> nodes;

  public NodeImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<NodeEntity> criteria = builder.createQuery(NodeEntity.class);
    criteria.from(NodeEntity.class);
    List<NodeEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    nodes = (ArrayList) records;
  }

  public List<NodeEntity> getAll() {
    return nodes;
  }

  public void exportToCSV(String filename) throws IOException {
    List<NodeEntity> nods = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("node,xcoord,ycoord,building,floor\n");
    for (NodeEntity nod : nods) {
      fileWriter.write(
          nod.getNodeid()
              + ","
              + nod.getXcoord()
              + ","
              + nod.getYcoord()
              + ","
              + nod.getFloor()
              + ","
              + nod.getBuilding()
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

    File node = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

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
    session.remove(n);
    tx.commit();
    session.close();
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

    NodeEntity n = session.get(NodeEntity.class, ID);

    n.setBuilding(obj.getBuilding());
    n.setXcoord(obj.getXcoord());
    n.setYcoord(obj.getYcoord());
    n.setFloor(obj.getFloor());

    nodes.add(n);
    tx.commit();
    session.close();
  }

  public NodeEntity get(String ID) {

    for (NodeEntity ser : nodes) {
      if (ser.getNodeid().equals(ID)) return ser;
    }
    return null;
  }
}
