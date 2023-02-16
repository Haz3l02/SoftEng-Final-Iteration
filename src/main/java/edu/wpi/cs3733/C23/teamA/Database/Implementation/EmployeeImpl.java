package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class EmployeeImpl implements IDatabaseAPI<EmployeeEntity, String> {
  private static final EmployeeImpl instance = new EmployeeImpl();

  private List<EmployeeEntity> employees;

  private EmployeeImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EmployeeEntity> criteria = builder.createQuery(EmployeeEntity.class);
    criteria.from(EmployeeEntity.class);
    employees = session.createQuery(criteria).getResultList();
    session.close();
  }



  public List<EmployeeEntity> getAll() {
    return employees;
  }

  public void exportToCSV(String filename) throws IOException {
    filename += "/employee.csv";

    File csvFile = new File(filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("employeeid,job,name,password,username\n");
    for (EmployeeEntity emp : employees) {
      fileWriter.write(
          emp.getEmployeeid()
              + ","
              + emp.getJob()
              + ","
              + emp.getName()
              + ","
              + emp.getPassword()
              + ","
              + emp.getUsername()
              + "\n");
    }
    fileWriter.close();
  }

  public void update(String ID, EmployeeEntity obj) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    employees.stream()
        .filter(employee -> employee.getEmployeeid().equals(ID))
        .toList()
        .forEach(
            employee -> {
              employees.remove(employee);
            });

    //    session
    //        .createMutationQuery(
    //            "UPDATE EmployeeEntity SET "
    //                + "employeeid = '"
    //                + obj.getEmployeeid()
    //                + "', job = '"
    //                + obj.getJob()
    //                + "', name = '"
    //                + obj.getName()
    //                + "', password = '"
    //                + obj.getPassword()
    //                + "', username = '"
    //                + obj.getUsername()
    //                + "' WHERE employeeid = '"
    //                + ID
    //                + "'")
    //        .executeUpdate();

    EmployeeEntity emp = session.get(EmployeeEntity.class, obj);
    emp.setName(obj.getName());
    emp.setPassword(obj.getPassword());
    emp.setEmployeeid(obj.getEmployeeid());
    emp.setUsername(obj.getUsername());
    emp.setJob(obj.getJob());


    ComputerRequestImpl.getInstance().refresh();
    PatientTransportimpl.getInstance().refresh();
    SecurityRequestImpl.getInstance().refresh();
    SanitationRequestImpl.getInstance().refresh();
    ServiceRequestImpl.getInstance().refresh();


    // employees.add(session.get(EmployeeEntity.class, obj.getEmployeeid()));
    tx.commit();
    session.close();
  }

  public ArrayList<String> checkPass(String user, String pass) {
    ArrayList<String> info = new ArrayList<>();
    for (EmployeeEntity emp : employees) {
      if (emp.getUsername().equals(user) && emp.getPassword().equals(pass)) {
        info.add(emp.getEmployeeid());
        info.add(emp.getJob());
        info.add(emp.getName());
        return info;
      }
    }
    return info;
  }

  public void add(EmployeeEntity e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(e);
    employees.add(e);
    tx.commit();
    session.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();
    if (filename.length() > 4) {
      if (!filename.substring(filename.length() - 4).equals(".csv")) {
        filename += ".csv";
      }
    } else filename += ".csv";

    String hql = "delete from SecurityRequestEntity";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();

    hql = "delete from SanitationRequestEntity ";
    q = session.createMutationQuery(hql);
    q.executeUpdate();

    hql = "delete from ComputerRequestEntity ";
    q = session.createMutationQuery(hql);
    q.executeUpdate();

    hql = "delete from ServiceRequestEntity ";
    q = session.createMutationQuery(hql);
    q.executeUpdate();

    employees.forEach(
        employee -> session.remove(session.get(EmployeeEntity.class, employee.getEmployeeid())));
    employees.clear();

    File emps = new File(filename);

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(emps);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(new EmployeeEntity(b[0], b[4], b[3], b[1], b[2]));
      employees.add(session.get(EmployeeEntity.class, b[0]));
    }
    tx.commit();
    session.close();
  }

  /**
   * Deletes employees matchine id e
   *
   * @param e EmployeeId
   */
  public void delete(String e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    employees.stream()
        .filter(employee -> employee.getEmployeeid().equals(e))
        .toList()
        .forEach(
            employee -> {
              session.remove(session.get(EmployeeEntity.class, employee.getEmployeeid()));
              employees.remove(employee);
            });
    tx.commit();
    session.close();
  }

  public EmployeeEntity getByUsername(String user) {
    return employees.stream()
        .filter(employee -> employee.getUsername().equals(user))
        .findFirst()
        .orElseThrow();
  }

  public List<String> getListOfByJob(String job) {
    ArrayList<String> theList = new ArrayList<>();
    for (EmployeeEntity emp : employees) {
      if (emp.getJob().equals(job)) {
        theList.add(emp.getName());
      }
    }
    return theList;
  }

  public EmployeeEntity get(String ID) {
    return employees.stream()
        .filter(employee -> employee.getEmployeeid().equals(ID))
        .findFirst()
        .orElseThrow();
  }

  public static EmployeeImpl getInstance() {
    return instance;
  }
}
