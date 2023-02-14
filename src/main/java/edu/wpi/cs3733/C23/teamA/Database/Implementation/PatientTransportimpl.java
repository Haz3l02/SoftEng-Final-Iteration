package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.PatientTransportRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PatientTransportimpl implements IDatabaseAPI<PatientTransportRequestEntity, Integer> {

  private List<PatientTransportRequestEntity> patrequests;
  private static final PatientTransportimpl instance = new PatientTransportimpl();

  public PatientTransportimpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<PatientTransportRequestEntity> criteria =
        builder.createQuery(PatientTransportRequestEntity.class);
    criteria.from(PatientTransportRequestEntity.class);
    patrequests = session.createQuery(criteria).getResultList();
    session.close();
  }

  @Override
  public List<PatientTransportRequestEntity> getAll() {
    return patrequests;
  }

  @Override
  public void add(PatientTransportRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    ServiceRequestImpl serv = new ServiceRequestImpl();
    Transaction tx = session.beginTransaction();
    session.persist(obj);
    tx.commit();
    patrequests.add(obj);
    serv.addToList(obj);
    session.close();
  }

  @Override
  public void importFromCSV(String filename) throws FileNotFoundException {}

  @Override
  public void exportToCSV(String filename) throws IOException {}

  @Override
  public void update(Integer ID, PatientTransportRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<PatientTransportRequestEntity> pr = patrequests.listIterator();
    while (pr.hasNext()) {
      if (pr.next().getRequestid() == ID) {
        pr.remove();
      }
    }

    PatientTransportRequestEntity p = get(ID);

    // supers
    p.setName(obj.getName());
    p.setDate(obj.getDate());
    p.setDescription(obj.getDescription());
    p.setLocation(obj.getLocation());
    p.setEmployee(obj.getEmployee());
    p.setEmployeeAssigned(obj.getEmployeeAssigned());
    p.setRequestType(obj.getRequestType());
    p.setUrgency(obj.getUrgency());
    p.setStatus(obj.getStatus());
    // subs
    p.setPatientName(obj.getPatientName());
    p.setPatientID(obj.getPatientID());
    p.setMoveTo(obj.getMoveTo());
    p.setEquipment(obj.getEquipment());

    PatientTransportRequestEntity pat =
        new PatientTransportRequestEntity(
            ID,
            obj.getName(),
            obj.getEmployee(),
            obj.getLocation(),
            obj.getDescription(),
            obj.getUrgency(),
            obj.getRequestType(),
            obj.getStatus(),
            obj.getEmployeeAssigned(),
            obj.getPatientName(),
            obj.getPatientID(),
            obj.getMoveTo(),
            obj.getEquipment(),
            obj.getDate());
    PatientTransportimpl patI = new PatientTransportimpl();
    patI.update(ID, pat);
    patrequests.add(obj);

    tx.commit();
    session.close();
  }

  @Override
  public void delete(Integer obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.remove(get(obj));
    ListIterator<PatientTransportRequestEntity> pr = patrequests.listIterator();
    while (pr.hasNext()) {
      if (pr.next().getRequestid() == obj) {
        pr.remove();
      }
    }
    ServiceRequestImpl servI = new ServiceRequestImpl();
    servI.removeFromList(obj);
    tx.commit();
    session.close();
  }

  public void removeFromList(Integer s) {
    ListIterator<PatientTransportRequestEntity> li = patrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  @Override
  public PatientTransportRequestEntity get(Integer ID) {
    for (PatientTransportRequestEntity pat : patrequests) {
      if (pat.getRequestid() == ID) return pat;
    }
    return null;
  }

  public static PatientTransportimpl getInstance() {
    return instance;
  }
}
