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

public class EmployeeImpl implements IDatabaseAPI<EmployeeEntity, String> {

  private List<EmployeeEntity> employees;
  private Session session;

  public EmployeeImpl() {
    session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EmployeeEntity> criteria = builder.createQuery(EmployeeEntity.class);
    criteria.from(EmployeeEntity.class);
    employees = session.createQuery(criteria).getResultList();
  }

  public List<EmployeeEntity> getAll() {
    return employees;
  }

  public void exportToCSV(String filename) throws IOException {
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
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
    Transaction tx = session.beginTransaction();

    employees.stream()
        .filter(employee -> employee.getEmployeeid().equals(ID))
        .toList()
        .forEach(
            employee -> {
              employees.remove(employee);
            });

    EmployeeEntity emp = session.get(EmployeeEntity.class, ID);
    emp.setEmployeeid(obj.getEmployeeid());
    emp.setUsername(obj.getUsername());
    emp.setPassword(obj.getPassword());
    emp.setJob(obj.getJob());
    emp.setName(obj.getName());

    tx.commit();
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
    Transaction tx = session.beginTransaction();
    session.persist(e);
    employees.add(e);
    tx.commit();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {

    employees.forEach(
        employee -> session.remove(session.get(EmployeeEntity.class, employee.getEmployeeid())));
    employees.clear();

    File emps = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

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
  }

  /**
   * Deletes employees matchine id e
   *
   * @param e EmployeeId
   */
  public void delete(String e) {
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
  }

  public EmployeeEntity getByUsername(String user) {
    return employees.stream()
        .filter(employee -> employee.getUsername().equals(user))
        .findFirst()
        .orElseThrow();
  }

  public EmployeeEntity get(String ID) {
    return employees.stream()
        .filter(employee -> employee.getEmployeeid().equals(ID))
        .findFirst()
        .orElseThrow();
  }

  public void closeSession() {
    session.close();
  }
}
