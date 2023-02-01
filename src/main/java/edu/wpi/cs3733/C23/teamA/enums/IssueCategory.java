package edu.wpi.cs3733.C23.teamA.enums;

public enum IssueCategory {
  STANDARD("Standard"),
  BIOHAZARD("Biohazard"),
  WONG("Wong");

  private final String issue;

  IssueCategory(String issue) {
    this.issue = issue;
  }

  public String getIssue() {
    return issue;
  }
}
