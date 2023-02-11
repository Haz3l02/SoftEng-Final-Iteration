package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class EdgeImpl implements IDatabaseAPI<EdgeEntity, String> {


    public List<EdgeEntity> getAll() {
        return null;
    }


    public void add(EdgeEntity obj) {

    }


    public void importFromCSV(String filename) throws FileNotFoundException {

    }


    public void exportToCSV(String filename) throws IOException {

    }


    public void update(String ID, EdgeEntity obj) {

    }


    public void delete(EdgeEntity obj) {

    }
}
