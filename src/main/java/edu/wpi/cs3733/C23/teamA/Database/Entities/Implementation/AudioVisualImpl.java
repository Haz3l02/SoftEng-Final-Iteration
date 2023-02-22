package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

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

public class AudioVisualImpl extends Observable
    implements IDatabaseAPI<AudioVisualRequestEntity, Integer> {
  private List<AudioVisualRequestEntity> audiovisualrequests;
  private static final AudioVisualImpl instance = new AudioVisualImpl();

  private AudioVisualImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AudioVisualRequestEntity> criteria =
        builder.createQuery(AudioVisualRequestEntity.class);
    criteria.from(AudioVisualRequestEntity.class);
    List<AudioVisualRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    audiovisualrequests = records;
  }

  public void refresh() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AudioVisualRequestEntity> criteria =
        builder.createQuery(AudioVisualRequestEntity.class);
    criteria.from(AudioVisualRequestEntity.class);
    List<AudioVisualRequestEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    audiovisualrequests = records;
  }

  public List<AudioVisualRequestEntity> getAll() {
    return audiovisualrequests;
  }

  public void add(AudioVisualRequestEntity c) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(c);
    tx.commit();
    audiovisualrequests.add(c);
    ServiceRequestImpl.getInstance().addToList(c);
    session.close();
    notifyAllObservers();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void exportToCSV(String filename) throws IOException {
    Session session = getSessionFactory().openSession();
    filename += "/audiovisualrequest.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write(
        "additionalequipment,avdevice,installationrequired,numdevices,returndate,subject,requestid\n");
    for (AudioVisualRequestEntity av : audiovisualrequests) {
      fileWriter.write(
          av.getAdditionalequipment()
              + ","
              + av.getAvdevice().getDevice()
              + ","
              + av.isInstallationrequired()
              + ","
              + av.getNumdevices()
              + ","
              + av.getReturndate().toString()
              + ","
              + av.getSubject().getSubject()
              + ","
              + av.getRequestid()
              + "\n");
    }
    fileWriter.close();
    session.close();
  }

  public void update(Integer ID, AudioVisualRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<AudioVisualRequestEntity> li = audiovisualrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == ID) {
        li.remove();
      }
    }

    AudioVisualRequestEntity c = get(ID);

    c.setAdditionalequipment(obj.getAdditionalequipment());
    c.setSubject(obj.getSubject());
    c.setAvdevice(obj.getAvdevice());
    c.setReturndate(obj.getReturndate());
    c.setNumdevices(obj.getNumdevices());
    c.setInstallationrequired(obj.isInstallationrequired());
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
    audiovisualrequests.add(c);

    tx.commit();
    session.close();
    notifyAllObservers();
  }

  public void delete(Integer c) {
    Session session = getSessionFactory().openSession();

    Transaction tx = session.beginTransaction();
    session.remove(get(c));

    ListIterator<AudioVisualRequestEntity> li = audiovisualrequests.listIterator();
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
    ListIterator<AudioVisualRequestEntity> li = audiovisualrequests.listIterator();
    while (li.hasNext()) {
      if (li.next().getRequestid() == s) {
        li.remove();
      }
    }
  }

  public AudioVisualRequestEntity get(Integer ID) {
    return audiovisualrequests.stream()
        .filter(audioVisualRequestEntity -> audioVisualRequestEntity.getRequestid() == ID)
        .findFirst()
        .orElseThrow();
  }

  public void updateStatus(Integer ID, Status status) {
    ListIterator<AudioVisualRequestEntity> li = audiovisualrequests.listIterator();
    while (li.hasNext()) {
      AudioVisualRequestEntity san = li.next();
      if (san.getRequestid() == ID) {
        san.setStatus(status);
        li.remove();
        audiovisualrequests.add(san);
        break;
      }
    }
  }

  public void updateEmployee(Integer ID, EmployeeEntity employee) {
    ListIterator<AudioVisualRequestEntity> li = audiovisualrequests.listIterator();
    while (li.hasNext()) {
      AudioVisualRequestEntity sec = li.next();
      if (sec.getRequestid() == ID) {
        sec.setEmployeeAssigned(employee);
        li.remove();
        audiovisualrequests.add(sec);
        break;
      }
    }
  }

  public static AudioVisualImpl getInstance() {
    return instance;
  }
}
