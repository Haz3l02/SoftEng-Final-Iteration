package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
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

public class ServiceRequestImpl implements IDatabaseAPI<ServiceRequestEntity, String> {

  public List<ServiceRequestEntity> getAll() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ServiceRequestEntity> criteria = builder.createQuery(ServiceRequestEntity.class);
    criteria.from(ServiceRequestEntity.class);
    List<ServiceRequestEntity> records = session.createQuery(criteria).getResultList();
    return records;
  }

  public void exportToCSV(String filename) throws IOException {
    List<ServiceRequestEntity> sers = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/servicerequest.csv");
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write(
        "requestid,date,description,employeeassigned,name,requestype,status,urgency,employeeid,location\n");
    for (ServiceRequestEntity ser : sers) {
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
              + "\n");
    }
    fileWriter.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {}

  public void add(ServiceRequestEntity s) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(s);

    tx.commit();
    session.close();
  }

  public void delete(ServiceRequestEntity s) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.remove(s);

    tx.commit();
    session.close();
  }

  public void update(String ID, ServiceRequestEntity obj) {}
}
