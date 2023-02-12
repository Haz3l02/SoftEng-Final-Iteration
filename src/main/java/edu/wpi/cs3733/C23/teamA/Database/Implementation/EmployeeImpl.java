package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class EmployeeImpl implements IDatabaseAPI<EmployeeEntity, String> {

  private List<EmployeeEntity> employees;

  public EmployeeImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<EmployeeEntity> criteria = builder.createQuery(EmployeeEntity.class);
    criteria.from(EmployeeEntity.class);
    List<EmployeeEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    employees = records;
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
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<EmployeeEntity> li = employees.listIterator();
    while (li.hasNext()){
      if (li.next().getEmployeeid().equals(ID)){
        li.remove();
      }
    }

    EmployeeEntity emp = session.get(EmployeeEntity.class, ID);
    emp.setEmployeeid(obj.getEmployeeid());
    emp.setJob(obj.getJob());
    emp.setUsername(obj.getUsername());
    emp.setPassword(obj.getPassword());
    emp.setName(obj.getName());


    employees.add(emp);
    tx.commit();
    session.close();
  }

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
    employees.add(e);
    tx.commit();
    session.close();
  }

  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();

    String hql = "delete from EmployeeEntity";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
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
      //      count++;
      //      if (count % 20 == 0) {
      //        session.flush();
      //        session.clear();
      //      }
    }
    tx.commit();
    session.close();
  }

  public void delete(String e) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    ListIterator<EmployeeEntity> li = employees.listIterator();
    while (li.hasNext()){
      if (li.next().getEmployeeid().equals(e)){
        li.remove();
      }
    }

    session.delete(session.get(EmployeeEntity.class, e));


    tx.commit();
    session.close();
  }

  public EmployeeEntity get(String ID){

    for (EmployeeEntity ser : employees){
      if (ser.getEmployeeid().equals(ID)) return ser;
    }
    return null;
  }

}
