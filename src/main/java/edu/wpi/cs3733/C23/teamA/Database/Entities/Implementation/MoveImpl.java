package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.*;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

public class MoveImpl extends Observable implements IDatabaseAPI<MoveEntity, List<String>> {
  private List<MoveEntity> moves;
  private static final MoveImpl instance = new MoveImpl();

  // done except importCSV
  private MoveImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    criteria.from(MoveEntity.class);
    List<MoveEntity> records = session.createQuery(criteria).getResultList();
    moves = records;
    //    HashMap<MoveEntity, MoveEntity> loc =
    //        locationChanges(LocalDate.parse("2023-02-18"), LocalDate.parse("2023-02-20"));
    //    for (MoveEntity m : loc.keySet()) {
    //      System.out.println(
    //          String.format(
    //              "%s -> %s %s %s",
    //              loc.get(m).getLocationName().getLongname(),
    //              m.getLocationName().getLongname(),
    //              m.getNode().getNodeid(),
    //              m.getMovedate().toString()));
    //    }
    session.close();
  }

  public List<MoveEntity> getAll() {
    return moves;
  }

  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    criteria.from(MoveEntity.class);
    List<MoveEntity> records = session.createQuery(criteria).getResultList();
    moves = records;
    session.close();
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/move.csv";

    File csvFile = new File(filename);
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
    Transaction tx = session.beginTransaction();
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    moves.clear();

    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File loc = new File(filename);

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
    MoveEntity me;

    ListIterator<MoveEntity> li = moves.listIterator();
    while (li.hasNext()) {
      me = li.next();
      if (me.getNode().getNodeid().equals(m.get(0))
          && me.getLocationName().getLongname().equals(m.get(1))
          && me.getMovedate().toString().equals(m.get(2))) {
        li.remove();
        session.remove(me);
      }
    }

    tx.commit();
    session.close();
  }

  /**
   * Find all moves happening on or after a date
   *
   * @param minDate Minimum date for a Move to become a key in the return
   * @param maxDate Minimum date for a Move to become a key in the return
   * @return Hashmap representing mapping of (latest location, location immediately before)
   */
  public HashMap<MoveEntity, MoveEntity> locationChanges(LocalDate minDate, LocalDate maxDate) {
    HashMap<MoveEntity, MoveEntity> changes = new HashMap<>();

    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    Root<MoveEntity> location = criteria.from(MoveEntity.class);
    criteria.select(location).where(builder.between(location.get("movedate"), minDate, maxDate));
    List<MoveEntity> ids = session.createQuery(criteria).getResultList();
    for (MoveEntity id : ids) {
      List<MoveEntity> loc = locationRecord(id.getLocationName().getLongname(), id.getMovedate());
      changes.put(id, loc.stream().findFirst().orElse(id));
    }
    return changes;
  }

  public HashMap<MoveEntity, MoveEntity> locationChangesFloor(
      LocalDate minDate, LocalDate maxDate, String floor) {
    HashMap<MoveEntity, MoveEntity> changes = new HashMap<>();

    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    Root<MoveEntity> location = criteria.from(MoveEntity.class);
    Predicate dateRange = builder.between(location.get("movedate"), minDate, maxDate);
    Predicate floorFilter = builder.equal(location.get("node").get("floor"), floor);
    criteria.select(location).where(builder.and(dateRange, floorFilter));
    List<MoveEntity> ids = session.createQuery(criteria).getResultList();
    for (MoveEntity id : ids) {
      List<MoveEntity> loc =
          locationRecordFloor(id.getLocationName().getLongname(), id.getMovedate(), floor);
      changes.put(id, loc.stream().findFirst().orElse(id));
    }
    return changes;
  }

  /**
   * Finds a list of moves matching node id that happened on or before certain date
   *
   * @param longname
   * @param date
   * @return
   */
  public List<MoveEntity> locationRecord(String longname, LocalDate date) {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    Query q =
        session.createQuery(
            "from MoveEntity mov where mov.locationName.longname ='"
                + longname
                + "' and mov.movedate <= '"
                + date
                + "' order by mov.movedate desc",
            MoveEntity.class);
    List<MoveEntity> records = q.getResultList();
    session.close();
    return records;
  }

  public List<MoveEntity> locationRecordFloor(String longname, LocalDate date, String floor) {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    Query q =
        session.createQuery(
            "select mov from MoveEntity mov where mov.locationName.longname ='"
                + longname
                + "' and mov.movedate <= '"
                + date
                + "' and mov.node.floor ='"
                + floor
                + "' order by mov.movedate desc",
            MoveEntity.class);
    List<MoveEntity> records = q.getResultList();
    session.close();
    return records;
  }

  /*
  // todo I want to use this but might not be able to
  public List<MoveEntity> locationRecordPathfinding(String longname, LocalDate date) {
    return moves.stream()
            .filter(
                    moveEntity ->
                            (moveEntity.getLocationName().getLongname().equalsIgnoreCase(longname)
                                    && moveEntity.getMovedate().compareTo(date) <= 0))
            .sorted((move1, move2) -> move2.getMovedate().compareTo(move1.getMovedate()))
            .toList();
  }
   */

  public List<MoveEntity> allMostRecent(LocalDate date) {
    List<MoveEntity> m = new ArrayList<>();
    List<LocationNameEntity> locations = FacadeRepository.getInstance().getAllLocation();
    for (LocationNameEntity loc : locations) {
      try {
        m.add(locationRecord(loc.getLongname(), date).get(0));
      } catch (Exception e) {
      }
    }
    return m;
  }

  public List<MoveEntity> allMostRecentFloor(LocalDate date, String floor) {
    List<MoveEntity> m = new ArrayList<>();
    List<LocationNameEntity> locations = FacadeRepository.getInstance().getAllLocation();
    for (LocationNameEntity loc : locations) {
      try {
        m.add(locationRecordFloor(loc.getLongname(), date, floor).get(0));
      } catch (Exception e) {
      }
    }
    return m;
  }

  public MoveEntity locationOnOrBeforeDate(String id, LocalDate date) {
    MoveEntity mov = new MoveEntity();
    List<MoveEntity> ids =
        moves.stream()
            .filter(
                moveEntity ->
                    moveEntity.getNode().getNodeid().equals(id)
                        && (date.compareTo(moveEntity.getMovedate()) >= 0))
            .toList();
    LocalDate dt1 = LocalDate.parse("2023-01-01");
    for (MoveEntity mo : ids) {
      if (mo.getMovedate().compareTo(dt1) >= 0) {
        mov = mo;
        dt1 = mo.getMovedate();
      }
    }
    return mov;
  }

  public MoveEntity nodeOnOrBeforeDate(String id, LocalDate date) {
    MoveEntity mov = new MoveEntity();
    List<MoveEntity> ids =
        moves.stream()
            .filter(
                moveEntity ->
                    moveEntity.getLocationName().getLongname().equals(id)
                        && (date.compareTo(moveEntity.getMovedate()) >= 0))
            .toList();
    LocalDate dt1 = LocalDate.parse("2023-01-01");
    for (MoveEntity mo : ids) {
      if (mo.getMovedate().compareTo(dt1) >= 0) {
        mov = mo;
        dt1 = mo.getMovedate();
      }
    }
    return mov;
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

  public NodeEntity mostRecentNode(String longname) {
    List<MoveEntity> ids =
        new ArrayList<>(
            moves.stream()
                .filter(moveEntity -> moveEntity.getLocationName().getLongname().equals(longname))
                .toList());
    ids.sort(Comparator.comparing(MoveEntity::getMovedate));
    return ids.isEmpty() ? null : ids.get(0).getNode();
  }

  public void update(List<String> ID, MoveEntity obj) {
    //    Session session = getSessionFactory().openSession();
    //    Transaction tx = session.beginTransaction();
    //
    //    ListIterator<MoveEntity> li = moves.listIterator();
    //    while (li.hasNext()) {
    //      MoveEntity me = li.next();
    //      if (me.getNode().getNodeid().equals(ID.get(0))
    //          && me.getLocationName().getLongname().equals(ID.get(1))
    //          && me.getMovedate().toString().equals(ID.get(2))) {
    //        li.remove();
    //      }
    //    }
    //
    //
    //    session
    //        .createMutationQuery(
    //            "UPDATE MoveEntity mov SET "
    //                + "mov.node = :newnodeid, mov.locationName = :newlongname, mov.movedate =
    // :newmovedate "
    //                + "WHERE mov.node = :oldnodeid and mov.locationName = :oldlongname and
    // mov.movedate = :oldmovedate")
    //        .setParameter("newnodeid", session.get(NodeEntity.class, obj.getNode().getNodeid()))
    //        .setParameter("newlongname", session.get(LocationNameEntity.class,
    // obj.getLocationName().getLongname()))
    //        .setParameter("newmovedate", obj.getMovedate())
    //        .setParameter("oldnodeid", session.get(NodeEntity.class, ID.get(0)))
    //        .setParameter("oldlongname", session.get(LocationNameEntity.class, ID.get(1)))
    //        .setParameter("oldmovedate", LocalDate.parse(ID.get(2)))
    //        .executeUpdate();
    //
    //    moves.add(
    //        new MoveEntity(
    //            session.get(NodeEntity.class, obj.getNode().getNodeid()),
    //            session.get(LocationNameEntity.class, obj.getLocationName().getLongname()),
    //            obj.getMovedate()));
    //    tx.commit();
    //    session.close();

    delete(ID);
    add(obj);
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

  public void removeAssociatedLocations(String nodeid) {
    List<MoveEntity> matching = new ArrayList<>();
    for (MoveEntity m : moves) {
      if (m.getNode().getNodeid().equals(nodeid)) {
        matching.add((m));
      }
    }
    for (MoveEntity m : matching) {
      List<String> mov = new ArrayList();
      mov.add(m.getNode().getNodeid());
      mov.add(m.getLocationName().getLongname());
      mov.add(m.getMovedate().toString());
      MoveImpl.getInstance().delete(mov);
    }
  }



  public void updateMessage(String message, List<String> m){
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    for (MoveEntity me : moves){
      if (me.getNode().getNodeid().equals(m.get(0))
              && me.getLocationName().getLongname().equals(m.get(1))
              && me.getMovedate().toString().equals(m.get(2))) {
        me.setMessage(message);
        session.persist(me);
        tx.commit();
        break;
      }
    }


    session.close();


  }


  public ArrayList<NodeEntity> newAndOldNode(String longName, LocalDate date) {
    ArrayList<NodeEntity> fin = new ArrayList<>();
    for (MoveEntity m : moves) {
      if (m.getLocationName().getLongname().equals(longName) && m.getMovedate().equals(date)) {
        fin.add(m.getNode());
      }
    }

    date.minusDays(1);
    fin.add(nodeOnOrBeforeDate(longName, date).getNode());
    return fin;
  }

}
