package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.SanitationRequestEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SanitationRequestImpl implements IDatabaseAPI<SanitationRequestEntity, Integer> {

    public List<SanitationRequestEntity> getAll() {
        return null;
    }


    public void add(SanitationRequestEntity obj) {

    }


    public void importFromCSV(String filename) throws FileNotFoundException {

    }


    public void exportToCSV(String filename) throws IOException {

    }


    public void update(Integer ID, SanitationRequestEntity obj) {

    }


    public void delete(SanitationRequestEntity obj) {

    }
}
