package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;

public class LocationName {
  private static final String tableName = "locationname";

  @Setter @Getter private String longName;
  @Setter @Getter private String shortName;
  @Setter @Getter private String locationType;

  public LocationName(String longName, String shortName, String locationType) {
    this.longName = longName;
    this.shortName = shortName;
    this.locationType = locationType;
  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "CREATE TABLE LocationName",
            "(longName text,",
            "shortName text,",
            "locationType text);");
    Adb.processUpdate(sql);
  }

  public static String getTableName() {
    return tableName;
  }

  public String toString() {
    return String.format("%s %s %s", longName, shortName, locationType);
  }
}
