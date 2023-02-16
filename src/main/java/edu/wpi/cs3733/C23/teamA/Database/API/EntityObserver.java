package edu.wpi.cs3733.C23.teamA.Database.API;

/** Observer subclass for the observation of database implementations */
public class EntityObserver extends Observer {
  /** Thing to observe */
  private Observable api;

  EntityObserver(Observable api) {
    api.attach(this);
    this.api = api;
  }

  /** Refresh the tables of this implementation */
  public void update() {
    api.refresh();
  }
}
