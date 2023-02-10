package edu.wpi.cs3733.C23.teamA.Database.API;

import java.util.List;

public interface IDatabaseAPI<T, G> {
    public List<T> getAll();

    public void add(Object obj);

    public void importFromCSV(String filename);

    public void expertToCSV(String filename);

    //public void update(String ID, T obj, G attributes);

    public void delete(T obj);






}
