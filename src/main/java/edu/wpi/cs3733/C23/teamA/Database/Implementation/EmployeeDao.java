package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

  public static void writeToCSV(List<EmployeeEntity> emps) throws IOException {
    File csvFile = new File("employees.csv");
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

  public static ArrayList<String> checkPass(String user, String pass, Session session) {
    ArrayList<String> info = new ArrayList<String>();
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

  public static void addEmployee(String username, String password,
                                 String job, String name, Session session) {
    Transaction tx = session.beginTransaction();
    String hql = "insert into EmployeeEntity (username, password, job, name) values ('"
            + username
            + "', '"
            + password
            + "', '"
            + job
            + "', '"
            + name
            + "')";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.executeUpdate();
    tx.commit();
  }

  public void updateEmployee(String employeeid, String username, String password,
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
  }

  public void deleteEmployee(String employeeid, Session session) {
    Transaction tx = session.beginTransaction();
    String hql = "DELETE FROM EmployeeEntity WHERE employeeid = "+ employeeid;
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.executeUpdate();
    session.persist(EmployeeEntity.class);
    tx.commit();

  }
}
