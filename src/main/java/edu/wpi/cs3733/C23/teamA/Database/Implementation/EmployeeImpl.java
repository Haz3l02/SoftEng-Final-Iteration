package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import jakarta.persistence.Query;
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

  public List<EmployeeEntity> getAll() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EmployeeEntity> criteria = builder.createQuery(EmployeeEntity.class);
    criteria.from(EmployeeEntity.class);
    List<EmployeeEntity> records = session.createQuery(criteria).getResultList();
    return records;
  }

  public void exportToCSV(String filename) throws IOException {
    List<EmployeeEntity> emps = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("employeeid,job,name,password,username\n");
    for (EmployeeEntity emp : emps) {
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

  public void update(String ID, EmployeeEntity obj) {}

  public static ArrayList<String> checkPass(String user, String pass) {
    Session session = getSessionFactory().openSession();
    ArrayList<String> info = new ArrayList<>();
    Transaction tx = session.beginTransaction();
    String hql = "select emp from EmployeeEntity emp where emp.username = '" + user + "'";
    Query query = session.createQuery(hql);
    final List<EmployeeEntity> emps = query.getResultList();
    for (EmployeeEntity emp : emps) {
      if (emp.getUsername().equals(user) && emp.getPassword().equals(pass)) {
        info.add(emp.getEmployeeid());
        info.add(emp.getJob());
        info.add(emp.getName());

        tx.commit();
        session.close();

        return info;
      }
    }
    info.add("");
    info.add("");
    info.add("");
    tx.commit();
    session.close();

    return info;
  }

  public void add(EmployeeEntity e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(e);

    tx.commit();
    session.close();
  }

  public void importFromCSV() throws FileNotFoundException {
    Session session = getSessionFactory().openSession();

    String hql = "delete from EmployeeEntity";
    Query q = session.createQuery(hql);
    q.executeUpdate();

    File emps = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/employee.csv");

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(emps);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      session.persist(new EmployeeEntity(b[0], b[4], b[3], b[1], b[2]));
      //      count++;
      //      if (count % 20 == 0) {
      //        session.flush();
      //        session.clear();
      //      }
    }
    tx.commit();
    session.close();
  }

  /*public void update(String employeeid, String username, String password,
                             String job, String name, Session session) {
    Transaction tx = session.beginTransaction();
    String hql = "update EmployeeEntity set username=:u, password=:p, job=:j, name=:n " +
            "where employeeid=:e";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.setParameter("u",username);
    query.setParameter("p",password);
    query.setParameter("j", job);
    query.setParameter("n", name);
    query.setParameter("e", employeeid);
    query.executeUpdate();
    tx.commit();
  }*/

  public void delete(EmployeeEntity e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(e);

    tx.commit();
    session.close();
  }
}
