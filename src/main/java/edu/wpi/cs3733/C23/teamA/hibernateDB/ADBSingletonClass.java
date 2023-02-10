package edu.wpi.cs3733.C23.teamA.hibernateDB;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class ADBSingletonClass {

  public static SessionFactory factory;

  private ADBSingletonClass() {}

  public static synchronized SessionFactory getSessionFactory() {
    StandardServiceRegistry registry;
    if (factory == null) {
      registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
      factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    return factory;
  }

  public static <T> List<T> getAllRecords(Class<T> type, Session session) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<T> criteria = builder.createQuery(type);
    criteria.from(type);
    List<T> records = session.createQuery(criteria).getResultList();
    return records;
  }

  public static void readFromCSV(Session session) throws SQLException, IOException {
    //    SessionImplementor sessImpl = (SessionImplementor) session;
    //    Connection conn = null;
    //    conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
    //    CopyManager copyManager = new CopyManager((BaseConnection) conn);
    //    File tf =File.createTempFile("employee", ".csv");
    //    String tempPath =tf.getParent();
    //    File tempFile = new File(tempPath + File.separator + "employee.csv");
    //    FileReader fileReader = new FileReader(tempFile);
    //    copyManager.copyIn("copy testdata (col1, col2, col3) from  STDIN with csv", fileReader );

  }

  //  public static void writeToCSV(List<EmployeeEntity> items, String filename) throws IOException
  // {
  //    PrintWriter writer =
  //        new PrintWriter(new BufferedWriter(new FileWriter("D:/Users/" + filename)));
  //    Iterator it = ((java.util.List) items).iterator();
  //    while (it.hasNext()) {
  //      Object o = (Object) it.next();
  //      EmployeeEntity e = (EmployeeEntity) o;
  //      System.out.println("Employee ID : " + e.getEmployeeid());
  //      System.out.println("Employee Job : " + e.getJob());
  //      System.out.println("Employee Name : " + e.getName());
  //      System.out.println("Employee Password : " + e.getPassword());
  //      System.out.println("Employee Username : " + e.getUsername());
  //      System.out.println("----------------------");
  //      writer.println(e.getEmployeeid());
  //    }
  //
  //    writer.close();
  //  }

}
