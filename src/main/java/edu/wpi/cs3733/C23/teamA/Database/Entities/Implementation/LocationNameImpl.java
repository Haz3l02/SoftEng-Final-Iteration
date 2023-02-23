package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class LocationNameImpl extends Observable
    implements IDatabaseAPI<LocationNameEntity, String> {
  private static final LocationNameImpl instance = new LocationNameImpl();

  private List<LocationNameEntity> locations;

  private LocationNameImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<LocationNameEntity> criteria = builder.createQuery(LocationNameEntity.class);
    criteria.from(LocationNameEntity.class);
    List<LocationNameEntity> records = session.createQuery(criteria).getResultList();
    locations = records;
    session.close();
  }

  @Override
  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<LocationNameEntity> criteria = builder.createQuery(LocationNameEntity.class);
    criteria.from(LocationNameEntity.class);
    List<LocationNameEntity> records = session.createQuery(criteria).getResultList();
    locations = records;
    session.close();
  }

  public List<LocationNameEntity> getAll() {
    return locations;
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/locationname.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("longname,locationtype,shortname\n");
    for (LocationNameEntity loc : locations) {
      fileWriter.write(
          loc.getLongname() + "," + loc.getLocationtype() + "," + loc.getShortname() + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    String hql = "delete from LocationNameEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    locations.clear();

    File loc = new File(filename);

    Scanner read = new Scanner(loc);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(new LocationNameEntity(b[0], b[2], b[1]));
      count++;
      locations.add(session.get(LocationNameEntity.class, b[0]));
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
    session.close();
  }

  public void add(LocationNameEntity l) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(l);
    locations.add(l);
    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void delete(String l) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(session.get(LocationNameEntity.class, l));

    ListIterator<LocationNameEntity> li = locations.listIterator();
    while (li.hasNext()) {
      if (li.next().getLongname().equals(l)) {
        li.remove();
      }
    }

    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void update(String ID, LocationNameEntity location) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<LocationNameEntity> li = locations.listIterator();
    while (li.hasNext()) {
      if (li.next().getLongname().equals(ID)) {
        li.remove();
      }
    }

    session
        .createMutationQuery(
            "UPDATE LocationNameEntity loc SET "
                + "loc.longname = '"
                + location.getLongname()
                + "', loc.shortname = '"
                + location.getShortname()
                + "', loc.locationtype = '"
                + location.getLocationtype()
                + "' WHERE loc.longname = '"
                + ID
                + "'")
        .executeUpdate();

    locations.add(session.get(LocationNameEntity.class, location.getLongname()));
    tx.commit();
    session.close();
  }

  public String getType(String ID) {
    for (LocationNameEntity ser : locations) {
      if (ser.getLongname().equals(ID)) return ser.getLocationtype();
    }
    return null;
  }

  public LocationNameEntity get(String ID) {

    for (LocationNameEntity ser : locations) {
      if (ser.getLongname().equals(ID)) return ser;
    }
    return null;
  }

  public List<String> getAllIDs() {
    return getAll().stream().map(locationNameEntity -> locationNameEntity.getLongname()).toList();
  }

  public void newLocationOnNode(String nodeid, LocationNameEntity loc) {
    LocationNameImpl.getInstance().add(loc);
    NodeEntity n = NodeImpl.getInstance().get(nodeid);
    MoveImpl.getInstance().add(new MoveEntity(n, loc, LocalDate.now()));
  }

  public static LocationNameImpl getInstance() {
    return instance;
  }

  public List<LocationNameEntity> getLocationAtCoordinate(int x, int y, String floor) {
    List<LocationNameEntity> fin = new ArrayList<LocationNameEntity>();
    for (NodeEntity n : NodeImpl.getInstance().getAll()) {
      if (n.getXcoord() == x && n.getYcoord() == y && n.getFloor().equals(floor)) {
        fin.add(MoveImpl.getInstance().mostRecentLoc(n.getNodeid()));
      }
    }
    return fin;
  }
}
