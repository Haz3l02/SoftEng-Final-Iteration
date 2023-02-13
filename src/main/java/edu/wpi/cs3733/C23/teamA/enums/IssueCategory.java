package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum IssueCategory {
  STANDARD("Standard"),
  BIOHAZARD("Biohazard"),
  CHEMICAL("Chemical"),
  WONG("Wong");

  private final String issue;

  IssueCategory(String issue) {
    this.issue = issue;
  }

  public String getIssue() {
    return issue;
  }

  public static ArrayList<String> issueList() {
    ArrayList<String> issues = new ArrayList<String>();
    for (IssueCategory issue : values()) {
      issues.add(issue.getIssue());
    }
    return issues;
  }
}
