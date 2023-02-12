package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ComputerRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class SecurityRequestImpl implements IDatabaseAPI<SecurityRequestEntity, Integer> {
  private List<SecurityRequestEntity> secrequests;

  public SecurityRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<SecurityRequestEntity> criteria = builder.createQuery(SecurityRequestEntity.class);
    criteria.from(SecurityRequestEntity.class);
    List<SecurityRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    secrequests = records;
  }

  public List<SecurityRequestEntity> getAll() {
    return secrequests;
  }

  public void exportToCSV(String filename) throws IOException {}

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void add(SecurityRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    secrequests.add(c);
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
    new ServiceRequestImpl().addToList(ser);
    tx.commit();
    session.close();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(session.get(SecurityRequestImpl.class, c));

    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()){
      if (li.next().getRequestid()==c){
        li.remove();
      }
    }
    new ServiceRequestImpl().removeFromList(c);
    tx.commit();
    session.close();
  }

  public void update(Integer ID, SecurityRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()){
      if (li.next().getRequestid()==ID){
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
    new ServiceRequestImpl().updateList(ID, ser);
    secrequests.add(c);

    tx.commit();
    session.close();
  }




  public void removeFromList(Integer s){
    ListIterator<SecurityRequestEntity> li = secrequests.listIterator();
    while (li.hasNext()){
      if (li.next().getRequestid()==s){
        li.remove();
      }
    }
  }


  public SecurityRequestEntity get(Integer ID){

    for (SecurityRequestEntity ser : secrequests){
      if (ser.getRequestid()==ID) return ser;
    }
    return null;
  }
}
