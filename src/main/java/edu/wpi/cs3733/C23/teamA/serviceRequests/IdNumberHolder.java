package edu.wpi.cs3733.C23.teamA.serviceRequests;

public final class IdNumberHolder {

  private String username;
  private String password;
  private String idNumber;
  private String job;
  private String name;
  private static final IdNumberHolder INSTANCE = new IdNumberHolder();

  public IdNumberHolder() {}

  public static IdNumberHolder getInstance() {
    return INSTANCE;
  }

  public void setId(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getId() {
    return this.idNumber;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getJob() {
    return this.job;
  }
}
