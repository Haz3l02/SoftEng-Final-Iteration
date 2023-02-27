package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MessageBoardEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class MessageBoardImpl extends Observable
        implements IDatabaseAPI<MessageBoardEntity, Integer> {

    private List<MessageBoardEntity> messages;
    private static final MessageBoardImpl instance = new MessageBoardImpl();

    private MessageBoardImpl() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MessageBoardEntity> criteria =
                builder.createQuery(MessageBoardEntity.class);
        criteria.from(MessageBoardEntity.class);
        messages = session.createQuery(criteria).getResultList();
        session.close();
    }

    public void refresh() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MessageBoardEntity> criteria =
                builder.createQuery(MessageBoardEntity.class);
        criteria.from(MessageBoardEntity.class);
        messages = session.createQuery(criteria).getResultList();
        session.close();
    }

    @Override
    public List<MessageBoardEntity> getAll() {
        return messages;
    }

    @Override
    public void add(MessageBoardEntity obj) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(obj);
        tx.commit();
        messages.add(obj);
        session.close();
    }

    @Override
    public void importFromCSV(String filename) throws FileNotFoundException {}

    @Override
    public void exportToCSV(String filename) throws IOException {
        filename += "/patienttransportrequest.csv";
        File csvFile = new File(filename);
        FileWriter fileWriter = new FileWriter(csvFile);
        fileWriter.write(
                "sender, receiver, title, message, time_sent\n");
        for (MessageBoardEntity mes : messages) {
            fileWriter.write(
                    mes.getSender().getUsername()
                            + ","
                            + mes.getReceiver().getUsername()
                            + ", "
                            + mes.getTitle()
                            + ", "
                            + mes.getMessage()
                            + ", "
                            + mes.getTimeSent()
                            + "\n");
        }
        fileWriter.close();
    }

    @Override
    public void update(Integer ID, MessageBoardEntity obj) {}

    @Override
    public void delete(Integer obj) {
    }

    @Override
    public MessageBoardEntity get(Integer ID) {
        return null;
    }

    public ArrayList<MessageBoardEntity> getconversion(String sender, String receiver) {
        ArrayList<MessageBoardEntity> conversation = new ArrayList<>();
        for (MessageBoardEntity mes : messages) {
            if (mes.getSender().equals(sender)
            && mes.getReceiver().equals(receiver)) {
                conversation.add(mes);
            }
        }
        return conversation;
    }

    public static MessageBoardImpl getInstance() {
        return instance;
    }
}
