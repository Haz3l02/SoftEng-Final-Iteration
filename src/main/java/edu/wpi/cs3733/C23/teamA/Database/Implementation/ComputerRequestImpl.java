package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ComputerRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComputerRequestImpl implements IDatabaseAPI<ComputerRequestEntity, Integer> {
  private List<ComputerRequestEntity> comprequests;

  public ComputerRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ComputerRequestEntity> criteria = builder.createQuery(ComputerRequestEntity.class);
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
    comprequests.add(c);
    ServiceRequestEntity ser = new ServiceRequestEntity(c.getRequestid(),
            c.getName(),
            c.getEmployee(),
            c.getLocation(),
            c.getDescription(),
            c.getUrgency(),
            c.getRequestType(),
            c.getStatus(),
            c.getEmployeeAssigned(),
            c.getDate()
            );
    new ServiceRequestImpl().add(ser);
    tx.commit();
    session.close();
  }


  public void importFromCSV(String filename) throws FileNotFoundException {

  }


  public void exportToCSV(String filename) throws IOException {

  }


  public void update(Integer ID, ComputerRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ComputerRequestEntity c = session.get(ComputerRequestEntity.class, ID);
    comprequests.remove(c);

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

    ServiceRequestEntity ser = new ServiceRequestEntity(ID,
            obj.getName(),
            obj.getEmployee(),
            obj.getLocation(),
            obj.getDescription(),
            obj.getUrgency(),
            obj.getRequestType(),
            obj.getStatus(),
            obj.getEmployeeAssigned(),
            obj.getDate());
    new ServiceRequestImpl().update(ID, ser);
    comprequests.add(c);

    tx.commit();
    session.close();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(session.get(ComputerRequestEntity.class, c));
    new ServiceRequestImpl().delete(session.get(ServiceRequestEntity.class, c));

    for (ComputerRequestEntity comp : comprequests){
      if (comp.getRequestid()==c){
        comprequests.remove(comp);
      }
    }

    tx.commit();
    session.close();
  }
}
