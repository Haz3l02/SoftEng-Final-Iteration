package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class LocationNameImpl implements IDatabaseAPI<LocationNameEntity, String> {
  // done
  private static final LocationNameImpl instance = new LocationNameImpl();

  private List<LocationNameEntity> locations;

  public LocationNameImpl() {
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
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
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
    String hql = "delete from LocationNameEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    locations.clear();
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);

    Transaction tx = session.beginTransaction();
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

    LocationNameEntity l = session.get(LocationNameEntity.class, ID);

    l.setLocationtype(location.getLocationtype());
    l.setShortname(location.getShortname());

    locations.add(l);
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

  public static LocationNameImpl getInstance() {
    return instance;
  }
}
