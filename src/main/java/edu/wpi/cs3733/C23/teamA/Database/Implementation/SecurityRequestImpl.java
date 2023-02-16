package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import edu.wpi.cs3733.C23.teamA.enums.Status;
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

public class SecurityRequestImpl implements IDatabaseAPI<SecurityRequestEntity, Integer> {
  private static final SecurityRequestImpl instance = new SecurityRequestImpl();

  private List<SecurityRequestEntity> secrequests;

  private SecurityRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<SecurityRequestEntity> criteria =
        builder.createQuery(SecurityRequestEntity.class);
    criteria.from(SecurityRequestEntity.class);
    secrequests = session.createQuery(criteria).getResultList();
    session.close();
  }

  public void refresh(){
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<SecurityRequestEntity> criteria =
            builder.createQuery(SecurityRequestEntity.class);
    criteria.from(SecurityRequestEntity.class);
    secrequests = session.createQuery(criteria).getResultList();
    session.close();
  }

  public List<SecurityRequestEntity> getAll() {
    return secrequests;
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/securityrequest.csv";
    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("assistance,secphone,requestid\n");
    for (SecurityRequestEntity ser : secrequests) {
      fileWriter.write(
          ser.getAssistance() + "," + ser.getSecphone() + "," + ser.getRequestid() + "\n");
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

  public void add(SecurityRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    secrequests.add(c);
    ServiceRequestEntity ser =
        new ServiceRequestEntity(
            c.getRequestid(),
            c.getName(),
            c.getEmployee(),
            c.getLocation(),
            c.getDescription(),
            c.getUrgency(),
            c.getRequestType(),
            c.getStatus(),
            c.getEmployeeAssigned(),
            c.getDate());
    ServiceRequestImpl.getInstance().addToList(ser);
    tx.commit();
    session.close();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.remove(get(c));

    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == c) {
        li.remove();
      }
    }
    ServiceRequestImpl.getInstance().removeFromList(c);
    tx.commit();
    session.close();
  }

  public void update(Integer ID, SecurityRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == ID) {
        li.remove();
      }
    }
    SecurityRequestEntity c = session.get(SecurityRequestEntity.class, ID);

    c.setSecphone(obj.getSecphone());
    c.setAssistance(obj.getAssistance());
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
    ServiceRequestImpl.getInstance().updateList(ID, ser);
    secrequests.add(c);

    tx.commit();
    session.close();
  }

  public void removeFromList(Integer s) {
    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  public SecurityRequestEntity get(Integer ID) {

    for (SecurityRequestEntity ser : secrequests) {
      if (ser.getRequestid() == ID) return ser;
    }
    return null;
  }

  public void updateStatus(Integer ID, Status status) {
    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()) {
      SecurityRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setStatus(status);
        li.remove();
        secrequests.add(sec);
        break;
      }
    }
  }

  public void updateEmployee(Integer ID, String employee) {
    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()) {
      SecurityRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setEmployeeAssigned(employee);
        li.remove();
        secrequests.add(sec);
        break;
      }
    }
  }

  public static SecurityRequestImpl getInstance() {
    return instance;
  }
}
