package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.AccessibilityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
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

public class AccessabilityImpl extends Observable
    implements IDatabaseAPI<AccessibilityRequestEntity, Integer> {
  private List<AccessibilityRequestEntity> accrequests;
  private static final AccessabilityImpl instance = new AccessabilityImpl();

  private AccessabilityImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AccessibilityRequestEntity> criteria =
        builder.createQuery(AccessibilityRequestEntity.class);
    criteria.from(AccessibilityRequestEntity.class);
    List<AccessibilityRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    accrequests = records;
  }

  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AccessibilityRequestEntity> criteria =
        builder.createQuery(AccessibilityRequestEntity.class);
    criteria.from(AccessibilityRequestEntity.class);
    List<AccessibilityRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    accrequests = records;
  }

  public List<AccessibilityRequestEntity> getAll() {
    return accrequests;
  }

  public void add(AccessibilityRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    tx.commit();
    accrequests.add(c);
    ServiceRequestImpl.getInstance().addToList(c);
    session.close();
    notifyAllObservers();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void exportToCSV(String filename) throws IOException {
    Session session = getSessionFactory().openSession();
    filename += "/accessabilityrequest.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("subject,disability,accommodation,requestid\n");
    for (AccessibilityRequestEntity acc : accrequests) {
      fileWriter.write(
          acc.getSubject()
              + ","
              + acc.getDisability()
              + ","
              + acc.getAccommodation()
              + ","
              + acc.getRequestid()
              + "\n");
    }
    fileWriter.close();
    session.close();
  }

  public void update(Integer ID, AccessibilityRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<AccessibilityRequestEntity> li = accrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == ID) {
        li.remove();
      }
    }

    AccessibilityRequestEntity c = get(ID);

    c.setSubject(obj.getSubject());
    c.setDisability(obj.getDisability());
    c.setAccommodation(obj.getAccommodation());
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

    ServiceRequestImpl.getInstance().update(ID, ser);
    accrequests.add(c);

    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();

    Transaction tx = session.beginTransaction();
    session.remove(get(c));

    ListIterator<AccessibilityRequestEntity> li = accrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == c) {
        li.remove();
      }
    }
    removeFromList(c);
    ServiceRequestImpl.getInstance().removeFromList(c);
    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void removeFromList(Integer s) {
    ListIterator<AccessibilityRequestEntity> li = accrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  public AccessibilityRequestEntity get(Integer ID) {
    return accrequests.stream()
        .filter(AccessibilityRequestEntity -> AccessibilityRequestEntity.getRequestid() == ID)
        .findFirst()
        .orElseThrow();
  }

  public void updateStatus(Integer ID, Status status) {
    ListIterator<AccessibilityRequestEntity> li = accrequests.listIterator();
    while (li.hasNext()) {
      AccessibilityRequestEntity san = li.next();
      if (san.getRequestid() == ID) {
        san.setStatus(status);
        li.remove();
        accrequests.add(san);
        break;
      }
    }
  }

  public void updateEmployee(Integer ID, EmployeeEntity employee) {
    ListIterator<AccessibilityRequestEntity> li = accrequests.listIterator();
    while (li.hasNext()) {
      AccessibilityRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setEmployeeAssigned(employee);
        li.remove();
        accrequests.add(sec);
        break;
      }
    }
  }

  public static AccessabilityImpl getInstance() {
    return instance;
  }
}
