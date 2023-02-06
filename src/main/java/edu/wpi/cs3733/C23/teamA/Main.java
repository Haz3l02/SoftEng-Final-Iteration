package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.loadAllData;

import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import jakarta.persistence.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://quotes15.p.rapidapi.com/quotes/random/"))
            .header("X-RapidAPI-Key", "d4914e733dmshdd1ec11f2fb2c05p1452d7jsn0a66c1b7ff90")
            .header("X-RapidAPI-Host", "quotes15.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
    HttpResponse<String> response =
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    String quote =
        response
            .body()
            .substring(
                response.body().indexOf("\"content\":\"", 0) + 11,
                response.body().indexOf("\"", response.body().indexOf("\"content\":\"") + 15));
    String author =
        response
            .body()
            .substring(
                response.body().indexOf("\"name\":\"", 0) + 8,
                response.body().indexOf("\"", response.body().indexOf("\"name\":\"") + 9));
    System.out.println("\"" + quote + "\" -" + author);
    AApp.launch(AApp.class, args);

    // AApp.launch(AApp.class, args);

    Session session = getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // EmployeeEntity person = new EmployeeEntity("123", "staff", "staff", "Medical", "Wilson
    // Wong");
    // session.persist(person);

    // LocationnameEntity loc =
    //    new LocationnameEntity("Anesthesia Conf Floor L1", "Conf C001L1", "CONF");
    // session.persist(loc);
    /*EmployeeEntity person = session.get(EmployeeEntity.class, "123");
    LocationnameEntity loc = session.get(LocationnameEntity.class, "Anesthesia Conf Floor L1");
    ComputerrequestEntity com =
        new ComputerrequestEntity(
            "PC help",
            person,
            loc,
            "Need help",
            ServicerequestEntity.Urgency.EXTREMELY_URGENT,
            ServicerequestEntity.RequestType.COMPUTER,
            ServicerequestEntity.Status.BLANK,
            "Harrison",
            "365",
            ComputerrequestEntity.Device.DESKTOP);
    session.persist(com);*/

    for (ServicerequestEntity ser : loadAllData(ServicerequestEntity.class, session)) {
      System.out.println(ser.getRequestid());
    }

    tx.commit();
    session.close();

    //    com.editComputerRequest(
    //       3, "jay", "bruh", "bruh", "burhbruh", "HIGH", "bruh", "bruh", "john", "bruh", "bruh");
    //    SanitationrequestEntity sanreq =
    //        new SanitationrequestEntity(
    //            "Clean",
    //            "123",
    //            "there",
    //            "stuff",
    //            ServicerequestEntity.Urgency.HIGH,
    //            ServicerequestEntity.RequestType.SANITATION,
    //            ServicerequestEntity.Status.BLANK,
    //            "John",
    //            SanitationrequestEntity.Category.BIOHAZARD);
    //
    //    session.persist(sanreq);
    //    tx.commit();
    //    session.close();

  }
}
