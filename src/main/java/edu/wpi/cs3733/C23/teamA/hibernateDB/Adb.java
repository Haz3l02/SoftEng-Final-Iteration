package edu.wpi.cs3733.C23.teamA.hibernateDB;

import edu.wpi.cs3733.C23.teamA.databases.Edge;
import edu.wpi.cs3733.C23.teamA.databases.LocationName;
import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.databases.Node;
import org.hibernate.Session;

import java.sql.*;

public class Adb {

  private static Session db = ADBSingleton._ADB.getSession();

  public static void closeConnection() throws SQLException {
    getInstance().close();
    db = null;
  }

  public static Session getInstance() {
    return db;
  }
}
