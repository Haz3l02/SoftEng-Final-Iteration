package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ComputerRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;
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

public class SanitationRequestImpl implements IDatabaseAPI<SanitationRequestEntity, Integer> {

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



  public void exportToCSV(String filename) throws IOException {}

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void add(SanitationRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    sanrequests.add(c);
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
    session.delete(session.get(SanitationRequestEntity.class, c));

    ListIterator<SanitationRequestEntity> li = sanrequests.listIterator();
    while (li.hasNext()){
      if (li.next().getRequestid()==c){
        li.remove();
      }
    }
    new ServiceRequestImpl().removeFromList(c);
    tx.commit();
    session.close();
  }

  public void update(Integer ID, SanitationRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<SanitationRequestEntity> li = sanrequests.listIterator();
    while (li.hasNext()){
      if (li.next().getRequestid()==ID){
        li.remove();
      }
    }

    SanitationRequestEntity c = session.get(SanitationRequestEntity.class, ID);

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
    sanrequests.add(c);

    tx.commit();
    session.close();
  }
}
