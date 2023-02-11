package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class NodeImpl implements IDatabaseAPI<NodeEntity, String> {

    public List<NodeEntity> getAll() {
        return null;
    }

    public void add(NodeEntity obj) {

    }

    public void importFromCSV(String filename) throws FileNotFoundException {

    }

    public void exportToCSV(String filename) throws IOException {

    }

    public void update(String ID, NodeEntity obj) {

    }

    public void delete(NodeEntity obj) {

    }
}
