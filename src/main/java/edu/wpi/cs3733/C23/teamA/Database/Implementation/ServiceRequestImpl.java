package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ServiceRequestImpl implements IDatabaseAPI<ServiceRequestEntity, Integer> {
  private List<ServiceRequestEntity> services;

  public ServiceRequestImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ServiceRequestEntity> criteria = builder.createQuery(ServiceRequestEntity.class);
    criteria.from(ServiceRequestEntity.class);
    services = session.createQuery(criteria).getResultList();
  }

  public List<ServiceRequestEntity> getAll() {
    return services;
  }

  public void exportToCSV(String filename) throws IOException {
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/servicerequest.csv");
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write(
        "requestid,date,description,employeeassigned,name,requestype,status,urgency,employeeid,location\n");
    for (ServiceRequestEntity ser : services) {
      fileWriter.write(
          ser.getRequestid()
              + ","
              + ser.getDate()
              + ","
              + ser.getDescription()
              + ","
              + ser.getEmployeeAssigned()
              + ","
              + ser.getName()
              + ","
              + ser.getRequestType()
              + ","
              + ser.getStatus()
              + ","
              + ser.getUrgency()
              + ","
              + ser.getEmployee().getEmployeeid()
              + ","
              + ser.getLocation().getLongname()
              + "\n");
    }
    fileWriter.close();
    new ComputerRequestImpl().exportToCSV("computerrequest.csv");
    new SecurityRequestImpl().exportToCSV("securityrequest.csv");
    new SanitationRequestImpl().exportToCSV("sanitationrequest.csv");

  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void add(ServiceRequestEntity s) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(s);
    services.add(s);
    tx.commit();
    session.close();
  }

  public void delete(ServiceRequestEntity s) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.remove(s);
    for (ServiceRequestEntity ser : services) {
      if (ser.getRequestid()==s.getRequestid()) {
        services.remove(ser);
      }
    }
    tx.commit();
    session.close();
  }

  public void update(Integer ID, ServiceRequestEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    ServiceRequestEntity ser = session.get(ServiceRequestEntity.class, ID);
    ser.setName(obj.getName());
    ser.setDate(obj.getDate());
    ser.setDescription(obj.getDescription());
    ser.setLocation(obj.getLocation());
    ser.setEmployee(obj.getEmployee());
    ser.setEmployeeAssigned(obj.getEmployeeAssigned());
    ser.setRequestType(obj.getRequestType());
    ser.setUrgency(obj.getUrgency());
    ser.setStatus(obj.getStatus());

    for (ServiceRequestEntity se : services) {
      if (se.getRequestid()==obj.getRequestid()) {
        services.remove(se);
      }
    }
    services.add(ser);
    tx.commit();
    session.close();
  }
}
