package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

public class EdgeImpl implements IDatabaseAPI<EdgeEntity, String>  {
    public List<EdgeEntity> getAll() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<EdgeEntity> criteria = builder.createQuery(EdgeEntity.class);
        criteria.from(EdgeEntity.class);
        List<EdgeEntity> records = session.createQuery(criteria).getResultList();
        return records;
    }

    public void exportToCSV(String filename) throws IOException {
        Session session = getSessionFactory().openSession();
        List<EdgeEntity> edges = getAllRecords(EdgeEntity.class, session);
        //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
        //      filename+=".csv";
        //    }

        File csvFile = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);
        FileWriter fileWriter = new FileWriter(csvFile);
        fileWriter.write("edgeid, node1, node2\n");
        for (EdgeEntity edge : edges) {
            fileWriter.write(
                    edge.getEdgeid()
                            + ", "
                            + edge.getNode1()
                            + ", "
                            + edge.getNode2()
                            + "\n");
        }
        fileWriter.close();
    }

    public void importFromCSV(String filename) throws FileNotFoundException {
        Session session = getSessionFactory().openSession();

        //     String hql = "delete from EmployeeEntity";
        //     Query q = session.createQuery(hql);
        //     q.executeUpdate();

        File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

        Transaction tx = session.beginTransaction();
        Scanner read = new Scanner(loc);
        int count = 0;
        read.nextLine();

        while (read.hasNextLine()) {
            String[] b = read.nextLine().split(",");
            session.persist(
                    new EdgeEntity(
                            session.get(NodeEntity.class, b[1]), session.get(NodeEntity.class, b[2]), b[0]));
            count++;
            if (count % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
        tx.commit();
        session.close();
    }

    public void add(EdgeEntity e) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(e);

        tx.commit();
        session.close();
    }
    public void delete(EdgeEntity e) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(e);

        tx.commit();
        session.close();
    }
    public void update(String s, EdgeEntity e) {

    }
}
