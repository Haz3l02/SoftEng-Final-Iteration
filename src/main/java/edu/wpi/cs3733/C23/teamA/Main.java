package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import java.io.IOException;
import org.hibernate.Session;

public class Main {

  public static void main(String[] args) throws IOException {
    try {
      AApp.launch(AApp.class, args);
    } catch (Exception e) {
      System.out.println("EXCEPTION\n\n\n");
      e.printStackTrace();
    }
  }
}
