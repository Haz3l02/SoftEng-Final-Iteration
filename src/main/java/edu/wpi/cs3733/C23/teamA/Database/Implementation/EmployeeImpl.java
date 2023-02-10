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

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

public class EmployeeImpl {

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

  public static ArrayList<String> checkPass(String username, String password, Session session) {
    ArrayList<String> info = new ArrayList<String>();
    Transaction tx = session.beginTransaction();
    String hql = "select emp from EmployeeEntity emp where emp.username = '" + username + "'";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    final List<EmployeeEntity> emps = query.getResultList();
    for (EmployeeEntity emp : emps) {
      if (emp.getUsername().equals(username) && emp.getPassword().equals(password)) {
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

  public static void add(EmployeeEntity emp) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    String hql = "insert into EmployeeEntity (username, password, job, name) values ('"
            + emp.getUsername()
            + "', '"
            + emp.getPassword()
            + "', '"
            + emp.getJob()
            + "', '"
            + emp.getName()
            + "')";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.executeUpdate();
    tx.commit();
    session.close();
  }

  public void update(EmployeeEntity emp) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    String hql = "update EmployeeEntity set username=:u, password=:p, job=:j, name=:n " +
            "where employeeid=:e";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.setParameter("u",emp.getUsername());
    query.setParameter("p",emp.getPassword());
    query.setParameter("j", emp.getJob());
    query.setParameter("n", emp.getName());
    query.setParameter("e", emp.getEmployeeid());
    query.executeUpdate();
    tx.commit();
    session.close();
  }

  public void delete(EmployeeEntity emp) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    String hql = "DELETE FROM EmployeeEntity WHERE employeeid = '"+ emp.getEmployeeid() + "'";
    Query query = session.createQuery(hql, EmployeeEntity.class);
    query.executeUpdate();
    session.persist(EmployeeEntity.class);
    tx.commit();
    session.close();
  }
}
