package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SanitationRequestImpl implements IDatabaseAPI<SanitationRequestEntity, Integer> {
  private static final SanitationRequestImpl instance = new SanitationRequestImpl();

  private List<SanitationRequestEntity> sanrequests;

  public SanitationRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<SanitationRequestEntity> criteria =
        builder.createQuery(SanitationRequestEntity.class);
    criteria.from(SanitationRequestEntity.class);
    sanrequests = session.createQuery(criteria).getResultList();
    session.close();
  }

  public List<SanitationRequestEntity> getAll() {
    return sanrequests;
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/sanitationrequest.csv";
    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("category,requestid\n");
    for (SanitationRequestEntity ser : sanrequests) {
      fileWriter.write(ser.getCategory() + "," + ser.getRequestid() + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {

    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";
  }

  public void add(SanitationRequestEntity c) {
    Session session = getSessionFactory().openSession();
    ServiceRequestImpl serv = new ServiceRequestImpl();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    tx.commit();
    sanrequests.add(c);
    serv.addToList(c);
    session.close();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.remove(get(c));
    ListIterator<SanitationRequestEntity> li = sanrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == c) {
        li.remove();
      }
    }
    ServiceRequestImpl servI = new ServiceRequestImpl();
    servI.removeFromList(c);
    tx.commit();
    session.close();
  }

  public void update(Integer ID, SanitationRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<SanitationRequestEntity> li = sanrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == ID) {
        li.remove();
      }
    }

    SanitationRequestEntity c = get(ID);

    c.setCategory(obj.getCategory());
    c.setName(obj.getName());
    c.setDate(obj.getDate());
    c.setDescription(obj.getDescription());
    c.setLocation(obj.getLocation());
    c.setEmployee(obj.getEmployee());
    c.setEmployeeAssigned(obj.getEmployeeAssigned());
    c.setRequestType(obj.getRequestType());
    c.setUrgency(obj.getUrgency());
    c.setStatus(obj.getStatus());

    ServiceRequestEntity ser =
        new ServiceRequestEntity(
            ID,
            obj.getName(),
            obj.getEmployee(),
            obj.getLocation(),
            obj.getDescription(),
            obj.getUrgency(),
            obj.getRequestType(),
            obj.getStatus(),
            obj.getEmployeeAssigned(),
            obj.getDate());
    ServiceRequestImpl serv = new ServiceRequestImpl();
    serv.update(ID, ser);
    sanrequests.add(c);

    tx.commit();
    session.close();
  }

  public void removeFromList(Integer s) {
    ListIterator<SanitationRequestEntity> li = sanrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  public SanitationRequestEntity get(Integer ID) {

    for (SanitationRequestEntity ser : sanrequests) {
      if (ser.getRequestid() == ID) return ser;
    }
    return null;
  }

  public static SanitationRequestImpl getInstance() {
    return instance;
  }
}
