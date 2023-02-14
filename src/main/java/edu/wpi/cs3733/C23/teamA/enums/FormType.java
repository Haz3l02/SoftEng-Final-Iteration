package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum FormType {
  SANITATION("Sanitation Request"),
  COMPUTER("Computer Request"),
  TRANSPORT("Transportation Request"),
  SECURITY("Security Request");

  private final String formType;

  FormType(String formType) {
    this.formType = formType;
  }

  public String getFormType() {
    return formType;
  }

  public static ArrayList<String> typeList() {
    ArrayList<String> types = new ArrayList<String>();
    for (FormType type : values()) {
      types.add(type.getFormType());
    }
    return types;
  }
}
