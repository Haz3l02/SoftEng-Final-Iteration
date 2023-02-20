package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum Subject {
    PATIENT("Patient"),
    VISITOR("Visitor"),
    STAFF("Staff");

    private final String subject;

    Subject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public static ArrayList<String> subjectList() {
        ArrayList<String> subjects = new ArrayList<String>();
        for (Subject subject : values()) {
            subjects.add(subject.getSubject());
        }
        return subjects;
    }
}