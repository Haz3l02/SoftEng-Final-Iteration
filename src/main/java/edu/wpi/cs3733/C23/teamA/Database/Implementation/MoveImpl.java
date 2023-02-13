package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class MoveImpl implements IDatabaseAPI<MoveEntity, List<String>> {
  private Session session;
  private List<MoveEntity> moves;
  // done except importCSV
  public MoveImpl() {
    session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    criteria.from(MoveEntity.class);
    List<MoveEntity> records = session.createQuery(criteria).getResultList();
    moves = records;
  }

  public List<MoveEntity> getAll() {
    return moves;
  }

  public void exportToCSV(String filename) throws IOException {
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

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
    String hql = "delete from MoveEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    moves.clear();
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
              Timestamp.valueOf(b[2]));
      session.persist(mov);

      count++;
      moves.add(mov);
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
  }

  public void add(MoveEntity m) {
    Transaction tx = session.beginTransaction();
    session.persist(m);
    moves.add(m);
    tx.commit();
  }

  public void delete(List<String> m) {
    Transaction tx = session.beginTransaction();
    ListIterator<MoveEntity> li = moves.listIterator();
    while (li.hasNext()) {
      if (li.next().getNode().equals(m.get(0))
          && li.next().getLocationName().equals(m.get(1))
          && li.next().getMovedate().equals(m.get(2))) {
        li.remove();
      }
    }

    // session.delete()
    tx.commit();
  }

  /**
   * Find the location of the node on or immediately before a specific date
   *
   * @param id ID of Node
   * @param date Date for finding the location
   */
  public MoveEntity locationOnDate(String id, LocalDate date) {
    Timestamp convertDate = Timestamp.valueOf(date.atStartOfDay());
    List<MoveEntity> ids =
        moves.stream()
            .filter(
                moveEntity ->
                    moveEntity.getNode().getNodeid().equals(id)
                        && (moveEntity.getMovedate().compareTo(convertDate) == 0
                            || moveEntity.getMovedate().compareTo(convertDate) == 1))
            .toList();
    return ids.get(0);
  }

  public LocationNameEntity mostRecentLoc(String id) {
    List<MoveEntity> ids =
        new ArrayList<>(
            moves.stream()
                .filter(moveEntity -> moveEntity.getNode().getNodeid().equals(id))
                .toList());
    ids.sort(Comparator.comparing(MoveEntity::getMovedate));
    return ids.isEmpty() ? null : ids.get(0).getLocationName();
  }

  public void update(List<String> ID, MoveEntity obj) {}

  public MoveEntity get(List<String> ID) {

    for (MoveEntity m : moves) {
      if (m.getNode().equals(ID.get(0))
          && m.getLocationName().equals(ID.get(1))
          && m.getMovedate().equals(ID.get(2))) return m;
    }
    return null;
  }

  public void closeSession() {
    session.close();
  }
}
