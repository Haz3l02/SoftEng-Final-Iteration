package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

public class MoveImpl implements IDatabaseAPI<MoveEntity, List<String>> {

  private ArrayList<MoveEntity> moves;
  // done except importCSV

  public MoveImpl() {
    Session session = getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<MoveEntity> criteria = builder.createQuery(MoveEntity.class);
    criteria.from(MoveEntity.class);
    List<MoveEntity> records = session.createQuery(criteria).getResultList();
    session.close();
    moves = (ArrayList) records;
  }

  public List<MoveEntity> getAll() {
    return moves;
  }

  public void exportToCSV(String filename) throws IOException {
    List<MoveEntity> movs = getAll();
    //    if (!filename[filename.length()-3, filename.length()].equals(".csv")){
    //      filename+=".csv";
    //    }

    File csvFile =
        new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSVBackup/" + filename);
    FileWriter fileWriter = new FileWriter(csvFile);
    fileWriter.write("movedate,longname,nodeid\n");
    for (MoveEntity mov : movs) {
      fileWriter.write(
          mov.getMovedate()
              + ","
              + mov.getLocationName().getLongname()
              + ","
              + mov.getNode().getNodeid()
              + "\n");
    }
    fileWriter.close();
  }

  // does not work
  // different node types
  // can't recognize class as string
  public void importFromCSV(String filename) throws FileNotFoundException {
    Session session = getSessionFactory().openSession();

    String hql = "delete from MoveEntity ";
    MutationQuery q = session.createMutationQuery(hql);
    q.executeUpdate();
    moves.clear();
    File loc = new File("src/main/java/edu/wpi/cs3733/C23/teamA/Database/CSV/" + filename);

    Transaction tx = session.beginTransaction();
    Scanner read = new Scanner(loc);
    int count = 0;
    read.nextLine();

    while (read.hasNextLine()) {
      String[] b = read.nextLine().split(",");
      MoveEntity mov =
          new MoveEntity(
              session.get(NodeEntity.class, b[0]),
              session.get(LocationNameEntity.class, b[1]),
              Timestamp.valueOf(b[2]));
      session.persist(mov);

      count++;
      moves.add(mov);
      if (count % 20 == 0) {
        session.flush();
        session.clear();
      }
    }
    tx.commit();
    session.close();
  }

  public void add(MoveEntity m) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.persist(m);
    moves.add(m);
    tx.commit();
    session.close();
  }

  public void delete(MoveEntity m) {
    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    session.delete(m);
    for (MoveEntity mov : moves) {
      if (mov.equals(m)) {
        moves.remove(mov);
      }
    }

    tx.commit();
    session.close();
  }

  public void update(List<String> ID, MoveEntity obj) {}

  @Override
  public void delete(List<String> obj) {}
}
