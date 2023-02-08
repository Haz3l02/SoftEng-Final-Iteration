package edu.wpi.cs3733.C23.teamA.serviceRequests;

import lombok.Getter;
import lombok.Setter;

public class EditTheForm {
  @Getter @Setter int requestID;
  @Getter @Setter public String requestType;
  @Getter @Setter public boolean needEdits = false;

  public EditTheForm(int requestID, String requestType, boolean needEdits) {
    this.requestID = requestID;
    this.requestType = requestType;
    this.needEdits = needEdits;
  }
}
