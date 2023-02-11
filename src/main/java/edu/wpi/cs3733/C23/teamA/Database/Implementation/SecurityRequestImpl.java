package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.ServiceRequestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

public class SecurityRequestImpl implements IDatabaseAPI<SecurityRequestEntity, String>  {
    public List<SecurityRequestEntity> getAll() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SecurityRequestEntity> criteria = builder.createQuery(SecurityRequestEntity.class);
        criteria.from(SecurityRequestEntity.class);
        List<SecurityRequestEntity> records = session.createQuery(criteria).getResultList();
        return records;
    }

    public void exportToCSV(String filename) throws IOException {
    }

    public void importFromCSV(String filename) throws FileNotFoundException {
    }


    public void add(SecurityRequestEntity s) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(s);

        tx.commit();
        session.close();
    }
    public void delete(SecurityRequestEntity s) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(s);

        tx.commit();
        session.close();
    }
    public void update(String ID, SecurityRequestEntity obj) {}
}
