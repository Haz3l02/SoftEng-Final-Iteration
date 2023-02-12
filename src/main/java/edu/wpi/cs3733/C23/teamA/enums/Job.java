package edu.wpi.cs3733.C23.teamA.enums;

import java.util.ArrayList;

public enum Job {
    MEDICAL("Medical"),
    MAINTENANCE("Maintenance"),
    ADMIN("Admin");

    private final String job;

    Job(String job) {
        this.job = job;
    }

    public static Job value(String str) {
        switch (str) {
            case "Medical":
                return MEDICAL;
            case "Potential Threat":
                return MAINTENANCE;
            case "ADMIN":
                return ADMIN;
            default:
                return MEDICAL;
        }
    }

    public String getJob() {
        return job;
    }

    public static ArrayList<String> jobList() {
        ArrayList<String> jobs = new ArrayList<String>();
        for (Job job : values()) {
            jobs.add(job.getJob());
        }
        return jobs;
    }
}
