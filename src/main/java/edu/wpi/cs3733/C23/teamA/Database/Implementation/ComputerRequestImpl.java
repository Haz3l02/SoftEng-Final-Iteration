package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ComputerRequestImpl implements IDatabaseAPI<ComputerRequestImpl, Integer> {

    public List<ComputerRequestImpl> getAll() {
        return null;
    }

    public void add(ComputerRequestImpl obj) {

    }

    public void importFromCSV(String filename) throws FileNotFoundException {

    }

    public void exportToCSV(String filename) throws IOException {

    }

    public void update(Integer ID, ComputerRequestImpl obj) {

    }

    public void delete(ComputerRequestImpl obj) {

    }
}
