package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
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

public class ComputerRequestImpl implements IDatabaseAPI<ComputerRequestEntity, Integer> {
  private List<ComputerRequestEntity> comprequests;
  private static final ComputerRequestImpl instance = new ComputerRequestImpl();

  public ComputerRequestImpl() {
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
    ServiceRequestImpl serv = new ServiceRequestImpl();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    tx.commit();
    comprequests.add(c);
    serv.addToList(c);
    session.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void exportToCSV(String filename) throws IOException {
    Session session = getSessionFactory().openSession();
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
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
    ServiceRequestImpl serv = new ServiceRequestImpl();
    serv.update(ID, ser);
    comprequests.add(c);

    tx.commit();
    session.close();
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
    tx.commit();
    session.close();
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


  public static ComputerRequestImpl getInstance() {
    return instance;
  }
}
