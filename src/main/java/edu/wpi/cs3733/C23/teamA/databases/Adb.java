package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.*;

@Deprecated
public class Adb {
  private Connection connection;
  private static Adb db;

  static {
    try {
      db = new Adb();
      db.init();
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Adb() throws ClassNotFoundException, SQLException {
    Class.forName("org.postgresql.Driver");
    connection =
        DriverManager.getConnection(
            "jdbc:postgresql://wpi-softeng-postgres-db.coyfss2f91ba.us-east-1.rds.amazonaws.com:2112/dba",
            "teama",
            "3oAm3GERjEWGeKUfjj1WHs6KjcItEI75");
  }

  public static PreparedStatement prepareStatement(String s) throws SQLException {
    return getInstance().connection.prepareStatement(s);
  }

  public static PreparedStatement prepareKeyGeneratingStatement(String s) throws SQLException {
    return getInstance().connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
  }

  public static int processUpdate(String s) throws SQLException {
    Statement stmt = getInstance().connection.createStatement();
    int numUpdated = stmt.executeUpdate(s);
    return numUpdated;
  }

  public void init() throws SQLException {
    if (!tableExists("edge")) {
      Edge.initTable();
    }
    if (!tableExists("node")) {
      Node.initTable();
    }
    if (!tableExists("locationname")) {
      LocationName.initTable();
    }
    if (!tableExists("move")) {
      Move.initTable();
    }
  }

  public static boolean tableExists(String tableName) throws SQLException {
    String query =
        "SELECT EXISTS (\n"
            + "SELECT FROM\n"
            + "   pg_tables\n"
            + "WHERE\n"
            + "   schemaname = 'public' AND\n"
            + "   tablename  = ?\n"
            + ");\n";

    PreparedStatement stmt = getInstance().connection.prepareStatement(query);
    stmt.setString(1, tableName);
    ResultSet rs = stmt.executeQuery();
    rs.next();

    return rs.getBoolean(1);
  }

  public static ResultSet processQuery(String s) throws SQLException {
    Statement stmt = getInstance().connection.createStatement();
    ResultSet rs = stmt.executeQuery(s);
    return rs;
  }

  public static void closeConnection() throws SQLException {
    getInstance().connection.close();
    db = null;
  }

  public static Adb getInstance() {
    return db;
  }
}
