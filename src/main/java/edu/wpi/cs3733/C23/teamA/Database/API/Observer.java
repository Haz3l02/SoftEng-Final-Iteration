package edu.wpi.cs3733.C23.teamA.Database.API;

public abstract class Observer {
  protected IDatabaseAPI api;

  // public Observer(IDatabaseAPI api) {
  // }

  public abstract void update();
}
