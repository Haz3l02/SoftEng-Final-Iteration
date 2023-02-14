package edu.wpi.cs3733.C23.teamA.serviceRequests;

import lombok.Getter;
import lombok.Setter;

public class AcceptTheForm {
    @Getter @Setter int requestID;
    @Getter @Setter public String requestType;
    @Getter @Setter public boolean acceptance = false;

    public AcceptTheForm(int requestID, String requestType, boolean acceptance) {
        this.requestID = requestID;
        this.requestType = requestType;
        this.acceptance = acceptance;
    }
}
