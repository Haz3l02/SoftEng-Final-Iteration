package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MoveImpl implements IDatabaseAPI<MoveEntity, List<String>> {
    public List<MoveEntity> getAll() {
        return null;
    }


    public void add(MoveEntity obj) {

    }


    public void importFromCSV(String filename) throws FileNotFoundException {

    }


    public void exportToCSV(String filename) throws IOException {

    }


    public void update(List<String> ID, MoveEntity obj) {

    }


    public void delete(MoveEntity obj) {

    }
}
