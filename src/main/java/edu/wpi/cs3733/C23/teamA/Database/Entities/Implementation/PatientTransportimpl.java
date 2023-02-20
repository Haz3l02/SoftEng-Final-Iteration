package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.PatientTransportRequestEntity;
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

public class PatientTransportimpl extends Observable
    implements IDatabaseAPI<PatientTransportRequestEntity, Integer> {
  private List<PatientTransportRequestEntity> patrequests;
  private static final PatientTransportimpl instance = new PatientTransportimpl();

  private PatientTransportimpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<PatientTransportRequestEntity> criteria =
        builder.createQuery(PatientTransportRequestEntity.class);
    criteria.from(PatientTransportRequestEntity.class);
    patrequests = session.createQuery(criteria).getResultList();
    session.close();
  }

  public void refresh() {
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
    Transaction tx = session.beginTransaction();
    session.persist(obj);
    tx.commit();
    patrequests.add(obj);
    ServiceRequestImpl.getInstance().addToList(obj);
    session.close();
  }

  @Override
  public void importFromCSV(String filename) throws FileNotFoundException {}

  @Override
  public void exportToCSV(String filename) throws IOException {
    filename += "/patienttransportrequest.csv";
    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("patientname, patientid, moveto, equipment\n");
    for (PatientTransportRequestEntity pat : patrequests) {
      fileWriter.write(
          pat.getPatientName()
              + ","
              + pat.getPatientID()
              + ", "
              + pat.getMoveTo()
              + ", "
              + pat.getEquipment()
              + "\n");
    }
    fileWriter.close();
  }

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

    ServiceRequestImpl.getInstance().removeFromList(obj);
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

  public void updateStatus(Integer ID, Status status) {
    ListIterator<PatientTransportRequestEntity> li = patrequests.listIterator();
    while (li.hasNext()) {
      PatientTransportRequestEntity san = li.next();
      if (san.getRequestid() == ID) {
        san.setStatus(status);
        li.remove();
        patrequests.add(san);
        break;
      }
    }
  }

  public void updateEmployee(Integer ID, String employee) {
    ListIterator<PatientTransportRequestEntity> li = patrequests.listIterator();
    while (li.hasNext()) {
      PatientTransportRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setEmployeeAssigned(employee);
        li.remove();
        patrequests.add(sec);
        break;
      }
    }
  }

  public static PatientTransportimpl getInstance() {
    return instance;
  }
}
