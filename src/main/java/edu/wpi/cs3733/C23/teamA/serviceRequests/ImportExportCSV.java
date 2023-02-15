package edu.wpi.cs3733.C23.teamA.serviceRequests;

import lombok.Getter;
import lombok.Setter;

public class ImportExportCSV {

  @Getter @Setter public String tableType;

  public ImportExportCSV(String tableType) {
    this.tableType = tableType;
  }
}
