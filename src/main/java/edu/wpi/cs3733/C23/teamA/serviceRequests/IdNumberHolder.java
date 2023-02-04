package edu.wpi.cs3733.C23.teamA.serviceRequests;

public final class IdNumberHolder {

  private String idNumber;
  private static final IdNumberHolder INSTANCE = new IdNumberHolder();

  private IdNumberHolder() {}

  public static IdNumberHolder getInstance() {
    return INSTANCE;
  }

  public void setId(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getId() {
    return this.idNumber;
  }
}
