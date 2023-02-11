package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SecurityRequestEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ServiceRequestImpl implements IDatabaseAPI<SecurityRequestEntity, Integer> {
    public List<SecurityRequestEntity> getAll() {
        return null;
    }

    public void add(SecurityRequestEntity obj) {

    }

    public void importFromCSV(String filename) throws FileNotFoundException {

    }

    public void exportToCSV(String filename) throws IOException {

    }

    public void update(Integer ID, SecurityRequestEntity obj) {

    }

    public void delete(SecurityRequestEntity obj) {

    }
}
