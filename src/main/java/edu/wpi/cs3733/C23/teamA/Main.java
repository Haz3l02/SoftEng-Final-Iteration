package edu.wpi.cs3733.C23.teamA;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass;
import edu.wpi.cs3733.C23.teamA.hibernateDB.ComputerrequestEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.EmployeeEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.ServicerequestEntity;
import jakarta.persistence.*;
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
  }
  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    Session session = ADBSingletonClass.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    // EmployeeEntity person = new EmployeeEntity("123", "staff", "staff", "Medical", "Wilson
    // Wong");
    // session.persist(person);
    EmployeeEntity person = session.get(EmployeeEntity.class, "123");
    ComputerrequestEntity com =
        new ComputerrequestEntity(
            "PC help",
            person,
            "There",
            "Need help",
            ServicerequestEntity.Urgency.EXTREMELY_URGENT,
            ServicerequestEntity.RequestType.COMPUTER,
            ServicerequestEntity.Status.BLANK,
            "Harrison",
            "365",
            ComputerrequestEntity.Device.DESKTOP);
    session.persist(com);

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
