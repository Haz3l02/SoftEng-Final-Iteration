package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SecurityRequestImpl implements IDatabaseAPI<SecurityException, Integer> {

    public List<SecurityException> getAll() {
        return null;
    }

    public void add(SecurityException obj) {

    }

    public void importFromCSV(String filename) throws FileNotFoundException {

    }

    public void exportToCSV(String filename) throws IOException {

    }

    public void update(Integer ID, SecurityException obj) {

    }

    public void delete(SecurityException obj) {

    }
}
