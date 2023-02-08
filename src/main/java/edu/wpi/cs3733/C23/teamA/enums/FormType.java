package edu.wpi.cs3733.C23.teamA.enums;

public enum FormType {
  SANITATION("Sanitation Request"),
  COMPUTER("Computer Request"),
  SECURITY("Security Request");

  private final String formType;

  FormType(String formType) {
    this.formType = formType;
  }

  public String getFormType() {
    return formType;
  }
}
