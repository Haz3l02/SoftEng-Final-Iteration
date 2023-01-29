package edu.wpi.cs3733.C23.teamA.databases;

import java.sql.SQLException;
import lombok.*;

public class SanitationRequest {

  private static final String tableName = "SanitationRequest";

  @Getter @Setter int requestID;
  @Getter @Setter String name;
  @Getter @Setter String idNum;
  @Getter @Setter String location;
  @Getter @Setter String description;
  @Getter @Setter String category;
  @Getter @Setter String ul;

  public SanitationRequest(
      String name, String idNum, String location, String description, String category, String ul) {

    this.name = name;
    this.idNum = idNum;
    this.location = location;
    this.description = description;
    this.category = category;
    this.ul = ul;
  }

  public static void initTable() throws SQLException {
    String sql =
        String.join(
            " ",
            "CREATE TABLE SanitationRequest",
            "(requestID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ",
            "name text,",
            "idNum text,",
            "location text,",
            "description text,",
            "category text,",
            "ul text);");
    Adb.processUpdate(sql);
  }
}
