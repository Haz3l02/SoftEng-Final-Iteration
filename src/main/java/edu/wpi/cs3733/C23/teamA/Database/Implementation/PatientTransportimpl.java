package edu.wpi.cs3733.C23.teamA.Database.Implementation;

import edu.wpi.cs3733.C23.teamA.Database.API.IDatabaseAPI;
import edu.wpi.cs3733.C23.teamA.Database.Entities.PatientTransportRequestEntity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PatientTransportimpl implements IDatabaseAPI<PatientTransportRequestEntity, Integer> {

  private List<PatientTransportRequestEntity> patrequests;

  @Override
  public List<PatientTransportRequestEntity> getAll() {
    return null;
  }

  @Override
  public void add(PatientTransportRequestEntity obj) {}

  @Override
  public void importFromCSV(String filename) throws FileNotFoundException {}

  @Override
  public void exportToCSV(String filename) throws IOException {}

  @Override
  public void update(Integer ID, PatientTransportRequestEntity obj) {}

  @Override
  public void delete(Integer obj) {}

  @Override
  public PatientTransportRequestEntity get(Integer ID) {
    for (PatientTransportRequestEntity pat : patrequests) {
      if (pat.getRequestid() == ID) return pat;
    }
    return null;
  }
}
