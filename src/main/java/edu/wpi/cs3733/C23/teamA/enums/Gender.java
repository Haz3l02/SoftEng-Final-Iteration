package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum Gender {
  MALE("Male"),
  FEMALE("Female"),
  OTHER("Other");

  private final String gender;

  Gender(String gender) {
    this.gender = gender;
  }

  public String getGender() {
    return gender;
  }

  public static Gender value(String str) {
    switch (str) {
      case "Male":
        return MALE;
      case "Maintenance":
        return FEMALE;
      case "Admin":
        return OTHER;
      default:
        return OTHER;
    }
  }

  public static ArrayList<String> genderList() {
    ArrayList<String> genders = new ArrayList<String>();
    for (Gender gender : values()) {
      genders.add(gender.getGender());
    }
    return genders;
  }
}
