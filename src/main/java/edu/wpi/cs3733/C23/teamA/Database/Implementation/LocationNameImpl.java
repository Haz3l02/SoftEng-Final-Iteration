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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class LocationNameImpl implements IDatabaseAPI<LocationNameEntity, String> {
  // done

  private List<LocationNameEntity> locations;

  public LocationNameImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<LocationNameEntity> criteria = builder.createQuery(LocationNameEntity.class);
    criteria.from(LocationNameEntity.class);
    List<LocationNameEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    locations = records;
  }

  public List<LocationNameEntity> getAll() {
    return locations;
  }

  public void exportToCSV(String filename) throws IOException {
    List<LocationNameEntity> locs = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("longname, locationtype, shortname\n");
    for (LocationNameEntity loc : locs) {
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

    File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

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

  public void delete(LocationNameEntity l) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(l);
    locations.remove(l);

    tx.commit();
    session.close();
  }

  public void update(String ID, LocationNameEntity location) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    LocationNameEntity l = session.get(LocationNameEntity.class, ID);
    locations.remove(l);


    l.setLocationtype(location.getLocationtype());
    l.setShortname(location.getShortname());


    locations.add(l);
    tx.commit();
    session.close();
  }
}
