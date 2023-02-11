package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.Database.Implementation.EmployeeImpl;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {

    // AApp.launch(AApp.class, args);

    new EmployeeImpl().exportToCSV("employee.csv");
    // new EmployeeImpl().importFromCSV("employee.csv");
  }
}
