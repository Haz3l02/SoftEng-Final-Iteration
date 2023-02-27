package edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.API.Observable;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MessageBoardEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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

    public void update(Integer ID, MessageBoardEntity obj) {}
    public void delete(Integer obj) {}

    public void update(MessageBoardEntity obj) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        MessageBoardEntity me;

        ListIterator<MessageBoardEntity> li = messages.listIterator();
        while (li.hasNext()) {
            me = li.next();
            if (me.getSender().getUsername().equals(obj.getSender().getUsername())
                    && me.getReceiver().getUsername().equals(obj.getReceiver().getUsername())
                    && me.getTimeSent().toString().equals(obj.getTimeSent().toString())) {
                li.remove();
            }

            session
                    .createMutationQuery(
                            "UPDATE MessageBoardEntity mes SET "
                                    + "mes.sender = '"
                                    + obj.getSender()
                                    + "', mes.receiver = '"
                                    + obj.getReceiver()
                                    + "', mes.title = "
                                    + obj.getTitle()
                                    + ", mes.message = "
                                    + obj.getMessage()
                                    + ", mes.timeSent = '"
                                    + obj.getTimeSent()
                                    + "' WHERE mes.sender = '"
                                    + obj.getSender() + "' and mes.receiver = '"
                                    + obj.getReceiver() + "' and mes.timeSent = '"
                                    + obj.getMessage() + "'")
                    .executeUpdate();
        }

    }

    public void delete(List<String> m) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        MessageBoardEntity me;

        ListIterator<MessageBoardEntity> li = messages.listIterator();
        while (li.hasNext()) {
            me = li.next();
            if (me.getSender().getUsername().equals(m.get(0))
                    && me.getReceiver().getUsername().equals(m.get(1))
                    && me.getTimeSent().toString().equals(m.get(2))) {
                li.remove();
                if (session.find(MessageBoardEntity.class, me) == null) {
                    System.out.println("Message is null");
                }
                session.remove(me);
            }
        }
        tx.commit();
        session.close();

    }

    @Override
    public MessageBoardEntity get(Integer ID) {
        return null;
    }

    public ArrayList<MessageBoardEntity> getconversion(EmployeeEntity sender, EmployeeEntity receiver) {
        ArrayList<MessageBoardEntity> conversation = new ArrayList<>();
        for (MessageBoardEntity mes : messages) {
            if (mes.getSender().getUsername().equals(sender.getUsername())
            && mes.getReceiver().getUsername().equals(receiver.getUsername())) {
                conversation.add(mes);
            }
        }
        return conversation;
    }

    public static MessageBoardImpl getInstance() {
        return instance;
    }
}
