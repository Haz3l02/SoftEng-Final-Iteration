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
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LocationNameImpl implements IDatabaseAPI<LocationNameEntity, String> {

  // done
  public List<LocationNameEntity> getAll() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<LocationNameEntity> criteria = builder.createQuery(LocationNameEntity.class);
    criteria.from(LocationNameEntity.class);
    List<LocationNameEntity> records = session.createQuery(criteria).getResultList();
    return records;
  }

  public void exportToCSV(String filename) throws IOException {
    List<LocationNameEntity> locs = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("longname, locationtype, shortname\n");
    for (LocationNameEntity loc : locs) {
      fileWriter.write(
          loc.getLongname() + "," + loc.getLocationtype() + "," + loc.getShortname() + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV() throws FileNotFoundException {
    Session session = getSessionFactory().openSession();

    //     String hql = "delete from EmployeeEntity";
    //     Query q = session.createQuery(hql);
    //     q.executeUpdate();

    File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/locationname.csv");

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(loc);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(new LocationNameEntity(b[1], b[2], b[0]));
      count++;
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

    tx.commit();
    session.close();
  }

  public void delete(LocationNameEntity l) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(l);

    tx.commit();
    session.close();
  }

  public void update(String ID, LocationNameEntity obj) {}
}
