package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class LocationNameImpl implements IDatabaseAPI<LocationNameEntity, String> {
    public List<LocationNameEntity> getAll() {
        return null;
    }

    public void add(LocationNameEntity obj) {

    }

    public void importFromCSV(String filename) throws FileNotFoundException {

    }

    public void exportToCSV(String filename) throws IOException {

    }

    public void update(String ID, LocationNameEntity obj) {

    }

    public void delete(LocationNameEntity obj) {

    }
}
