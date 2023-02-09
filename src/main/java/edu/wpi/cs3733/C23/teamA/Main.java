package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.*;

import edu.wpi.cs3733.C23.teamA.hibernateDB.*;
import jakarta.persistence.*;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    AApp.launch(AApp.class, args);
  }
}
