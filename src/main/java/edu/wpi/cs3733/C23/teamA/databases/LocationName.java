package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class LocationName {
  @Getter private static final String tableName = "locationname";
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
            "create table locationname (\n"
                + "  longname text primary key not null,\n"
                + "  shortname text,\n"
                + "  locationtype text);");
    Adb.processUpdate(sql);
  }

  public static ArrayList<LocationName> getAll() throws SQLException {
    ArrayList<LocationName> locations = new ArrayList<>();
    String sql = "SELECT * FROM locationname;";
    ResultSet rs = Adb.processQuery(sql);
    while (rs.next()) {
      locations.add(
          new LocationName(
              rs.getString("longname"), rs.getString("shortname"), rs.getString("locationtype")));
    }
    return locations;
  }

  public void update() throws SQLException {
    String sql =
        String.format(
            "insert into locationname values('%s','%s','%s') on conflict do update;",
            longName, shortName, locationType);
    Adb.processUpdate(sql);
  }

  public String toString() {
    return String.format("%s %s %s", longName, shortName, locationType);
  }
}
