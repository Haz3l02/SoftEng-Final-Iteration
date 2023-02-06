package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.hibernateDB.ComputerrequestEntity;

public class Main {

  public static void main(String[] args) {

    // AApp.launch(AApp.class, args);

    ComputerrequestEntity com = new ComputerrequestEntity();

    com.editComputerRequest(
        3, "jay", "bruh", "bruh", "burhbruh", "HIGH", "bruh", "bruh", "john", "bruh", "bruh");
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
