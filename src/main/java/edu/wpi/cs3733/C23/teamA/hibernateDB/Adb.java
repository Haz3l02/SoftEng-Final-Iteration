package edu.wpi.cs3733.C23.teamA.hibernateDB;

import java.sql.*;
import org.hibernate.Session;

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
