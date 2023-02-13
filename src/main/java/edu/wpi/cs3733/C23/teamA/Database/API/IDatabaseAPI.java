package edu.wpi.cs3733.C23.teamA.Database.API;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IDatabaseAPI<T, G> {
  public List<T> getAll();

  public void add(T obj);

  public void importFromCSV(String filename) throws FileNotFoundException;

  public void exportToCSV(String filename) throws IOException;

  public void update(G ID, T obj);

  public void delete(G obj);

  public T get(G ID);
}
