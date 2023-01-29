package edu.wpi.cs3733.C23.teamA.databases;

import edu.wpi.cs3733.C23.teamA.databases.Adb;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalTime;

public class Move {


    private static final String tableName = "Move";

    @Setter @Getter
    private String nodeID;
    @Setter @Getter
    private String longName;
    @Setter @Getter
    private LocalTime moveDate;

    public Move(String nodeID, String longName, LocalTime moveDate) {
        this.nodeID = nodeID;
        this.longName = longName;
        this.moveDate = moveDate;
    }

    public static void initTable() throws SQLException {
        String sql =
                String.join(
                        " ",
                        "CREATE TABLE Move",
                        "(nodeID text,",
                        "longName text,",
                        "moveDate TIMESTAMP);");
        Adb.processUpdate(sql);
    }


}
