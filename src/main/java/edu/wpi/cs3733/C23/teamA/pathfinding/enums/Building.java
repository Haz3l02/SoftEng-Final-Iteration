package edu.wpi.cs3733.C23.teamA.pathfinding.enums;

public enum Building {
    // WIP
    FR45 ("45 Francis", "???"),
    TOWR ("Tower", "???"),
    SHPR ("Shapiro", "Shapiro Cardiovascular Building");
    // may need more

    private final String tableString;
    private final String extendedString;

    Building(String tableString, String extendedString) {
        this.tableString = tableString;
        this.extendedString = extendedString;
    }

    public String getTableString() {
        return tableString;
    }
    public String getExtendedString() {
        return extendedString;
    }
}
