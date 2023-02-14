package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class MoveImpl implements IDatabaseAPI<MoveEntity, List<String>> {
  private List<MoveEntity> moves;
  private static final MoveImpl instance = new MoveImpl();

  // done except importCSV
  public MoveImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    criteria.from(MoveEntity.class);
    List<MoveEntity> records = session.createQuery(criteria).getResultList();
    moves = records;
    session.close();
  }

  public List<MoveEntity> getAll() {
    return moves;
  }

  public void exportToCSV(String filename) throws IOException {
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("movedate,longname,nodeid\n");
    for (MoveEntity mov : moves) {
      fileWriter.write(
          mov.getMovedate()
              + ","
              + mov.getLocationName().getLongname()
              + ","
              + mov.getNode().getNodeid()
              + "\n");
    }
    fileWriter.close();
  }

  // does not work
  // different node types
  // can't recognize class as string
  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();
    String hql = "delete from MoveEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    moves.clear();

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
      MoveEntity mov =
          new MoveEntity(
              session.get(NodeEntity.class, b[0]),
              session.get(LocationNameEntity.class, b[1]),
              LocalDate.parse(b[2]));
      session.persist(mov);

      count++;
      moves.add(mov);
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
    session.close();
  }

  public void add(MoveEntity m) {
    Session session = getSessionFactory().openSession();
    List<LocalDate> tracking = new ArrayList<>();

    for (MoveEntity n :
        moves.stream().filter(moveEntity -> moveEntity.getNode().equals(m.getNode())).toList()) {
      tracking.add(n.getMovedate());
    }
    System.out.println(tracking);
    if (Collections.frequency(tracking, m.getMovedate()) < 2) {
      Transaction tx = session.beginTransaction();
      session.persist(m);
      moves.add(m);
      tx.commit();
    } else {
      throw new PersistenceException();
    }
    session.close();
  }

  public void delete(List<String> m) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    ListIterator<MoveEntity> li = moves.listIterator();
    while (li.hasNext()) {
      if (li.next().equals(get(m))) {
        li.remove();
      }
    }

    session.delete(
        session.find(
            MoveEntity.class,
            new MoveEntity(
                session.get(NodeEntity.class, m.get(0)),
                session.get(LocationNameEntity.class, m.get(1)),
                LocalDate.parse(m.get(2)))));

    // session.delete()
    tx.commit();
    session.close();
  }

  /**
   * Find the location of the node on or immediately before a specific date
   *
   * @param id ID of Node
   * @param date Date for finding the location
   */
  public MoveEntity locationOnDate(String id, LocalDate date) {
    List<MoveEntity> ids =
        moves.stream()
            .filter(
                moveEntity ->
                    moveEntity.getNode().getNodeid().equals(id)
                        && (moveEntity.getMovedate().compareTo(date) == 0
                            || moveEntity.getMovedate().compareTo(date) == 1))
            .toList();
    return ids.get(0);
  }

  /**
   * Find the last assigned location of this node by its id. This will get the move with the
   * furthest in the future date.
   *
   * @param id Node ID as String
   * @return LocationNameEntity representing the Location that this node will be at far in the
   *     future.
   */
  public LocationNameEntity mostRecentLoc(String id) {
    List<MoveEntity> ids =
        new ArrayList<>(
            moves.stream()
                .filter(moveEntity -> moveEntity.getNode().getNodeid().equals(id))
                .toList());
    ids.sort(Comparator.comparing(MoveEntity::getMovedate));
    return ids.isEmpty() ? null : ids.get(0).getLocationName();
  }

  public void update(List<String> ID, MoveEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    MoveEntity mov =
        session.find(
            MoveEntity.class,
            new MoveEntity(
                session.get(NodeEntity.class, ID.get(0)),
                session.get(LocationNameEntity.class, ID.get(1)),
                LocalDate.parse(ID.get(2))));
    mov.setLocationName(obj.getLocationName());
    mov.setNode(obj.getNode());
    mov.setMovedate(obj.getMovedate());

    ListIterator<MoveEntity> li = moves.listIterator();
    while (li.hasNext()) {
      if (li.next().getNode().equals(ID.get(0))
          && li.next().getLocationName().equals(ID.get(1))
          && li.next().getMovedate().equals(ID.get(2))) {
        li.remove();
      }
    }
    moves.add(mov);

    tx.commit();
    session.close();
  }

  public MoveEntity get(List<String> ID) {
    for (MoveEntity m : moves) {
      if (m.getNode().equals(ID.get(0))
          && m.getLocationName().equals(ID.get(1))
          && m.getMovedate().toString().equals(ID.get(2))) return m;
    }
    return null;
  }



  public static MoveImpl getInstance() {
    return instance;
  }

  public List<String> getNodeID() {
    ArrayList<String> nodeID = new ArrayList<>();
    for (MoveEntity m : moves) {
      nodeID.add(m.getNode().getNodeid());
    }
    return nodeID;
  }

  public List<String> getLocationName() {
    ArrayList<String> nodeID = new ArrayList<>();
    for (MoveEntity m : moves) {
      nodeID.add(m.getLocationName().getLongname());
    }
    return nodeID;
  }
}
