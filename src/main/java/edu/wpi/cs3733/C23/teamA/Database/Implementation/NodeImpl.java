package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.*;
import java.lang.reflect.GenericArrayType;
import java.util.List;
import java.util.Scanner;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;
import static java.lang.Integer.parseInt;

public class NodeImpl implements IDatabaseAPI<NodeEntity, String > {
    //done

    public List<NodeEntity> getAll() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<NodeEntity> criteria = builder.createQuery(NodeEntity.class);
        criteria.from(NodeEntity.class);
        List<NodeEntity> records = session.createQuery(criteria).getResultList();
        return records;
    }

    public void exportToCSV(String filename) throws IOException {
        List<NodeEntity> nods = getAll();
        //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
        //      filename+=".csv";
        //    }

        File csvFile = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);
        FileWriter fileWriter = new FileWriter(csvFile);
        fileWriter.write("node,xcoord,ycoord,building,floor\n");
        for (NodeEntity nod : nods) {
            fileWriter.write(
                    nod.getNodeid()
                            + ","
                            + nod.getXcoord()
                            + ","
                            + nod.getYcoord()
                            + ","
                            + nod.getFloor()
                            + ","
                            + nod.getBuilding()
                            + "\n");
        }
        fileWriter.close();
    }

    public void importFromCSV(String filename) throws FileNotFoundException {
        Session session = getSessionFactory().openSession();

        //     String hql = "delete from EmployeeEntity";
        //     Query q = session.createQuery(hql);
        //     q.executeUpdate();

        File nod = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

        Transaction tx = session.beginTransaction();
        Scanner read = new Scanner(nod);
        int count = 0;
        read.nextLine();

        while (read.hasNextLine()) {
            String[] b = read.nextLine().split(",");
            session.persist(new NodeEntity(b[0], parseInt(b[1]), parseInt(b[2]), b[3], b[4]));
                  count++;
                  if (count % 20 == 0) {
                    session.flush();
                    session.clear();
                  }
        }
        tx.commit();
        session.close();
    }




    public void add(NodeEntity n) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(n);

        tx.commit();
        session.close();
    }
    public void delete(NodeEntity n) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(n);

        tx.commit();
        session.close();
    }
     public void update(String ID, NodeEntity obj) {

    }
}
