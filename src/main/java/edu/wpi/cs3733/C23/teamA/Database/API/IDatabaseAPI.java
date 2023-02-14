package edu.wpi.cs3733.C23.teamA.Database.API;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IDatabaseAPI<T, G> {
  List<T> getAll();

  void add(T obj);

  void importFromCSV(String filename) throws FileNotFoundException;

  void exportToCSV(String filename) throws IOException;

  void update(G ID, T obj);

  void delete(G obj);

  T get(G ID);

  // void closeSession();
}
