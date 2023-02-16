package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
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

public class ComputerRequestImpl extends Observable
    implements IDatabaseAPI<ComputerRequestEntity, Integer> {
  private List<ComputerRequestEntity> comprequests;
  private static final ComputerRequestImpl instance = new ComputerRequestImpl();

  private ComputerRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ComputerRequestEntity> criteria =
        builder.createQuery(ComputerRequestEntity.class);
    criteria.from(ComputerRequestEntity.class);
    List<ComputerRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    comprequests = records;
  }

  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ComputerRequestEntity> criteria =
        builder.createQuery(ComputerRequestEntity.class);
    criteria.from(ComputerRequestEntity.class);
    List<ComputerRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    comprequests = records;
  }

  public List<ComputerRequestEntity> getAll() {
    return comprequests;
  }

  public void add(ComputerRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    tx.commit();
    comprequests.add(c);
    ServiceRequestImpl.getInstance().addToList(c);
    session.close();
    notifyAllObservers();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void exportToCSV(String filename) throws IOException {
    Session session = getSessionFactory().openSession();
    filename += "/computerrequest.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("device,deviceid,requestid\n");
    for (ComputerRequestEntity comp : comprequests) {
      fileWriter.write(
          comp.getDevice() + "," + comp.getDeviceid() + "," + comp.getRequestid() + "\n");
    }
    fileWriter.close();
    session.close();
  }

  public void update(Integer ID, ComputerRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<ComputerRequestEntity> li = comprequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == ID) {
        li.remove();
      }
    }

    ComputerRequestEntity c = get(ID);

    c.setDevice(obj.getDevice());
    c.setDeviceid(obj.getDeviceid());
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
    comprequests.add(c);

    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();

    Transaction tx = session.beginTransaction();
    session.remove(get(c));

    ListIterator<ComputerRequestEntity> li = comprequests.listIterator();
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
    ListIterator<ComputerRequestEntity> li = comprequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  public ComputerRequestEntity get(Integer ID) {
    return comprequests.stream()
        .filter(computerRequestEntity -> computerRequestEntity.getRequestid() == ID)
        .findFirst()
        .orElseThrow();
  }

  public void updateStatus(Integer ID, Status status) {
    ListIterator<ComputerRequestEntity> li = comprequests.listIterator();
    while (li.hasNext()) {
      ComputerRequestEntity san = li.next();
      if (san.getRequestid() == ID) {
        san.setStatus(status);
        li.remove();
        comprequests.add(san);
        break;
      }
    }
  }

  public void updateEmployee(Integer ID, String employee) {
    ListIterator<ComputerRequestEntity> li = comprequests.listIterator();
    while (li.hasNext()) {
      ComputerRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setEmployeeAssigned(employee);
        li.remove();
        comprequests.add(sec);
        break;
      }
    }
  }

  public static ComputerRequestImpl getInstance() {
    return instance;
  }
}
