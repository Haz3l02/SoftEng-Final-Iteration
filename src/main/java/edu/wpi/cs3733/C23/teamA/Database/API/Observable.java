package edu.wpi.cs3733.C23.teamA.Database.API;

import java.util.ArrayList;
import java.util.List;

/** Observable Pattern */
public abstract class Observable {
  private List<Observer> observers = new ArrayList<>();

  public abstract void refresh();

  /** Call when Implementation makes a database change to pull tables */
  public void notifyAllObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  /** */
  public void attach(Observer e) {
    observers.add(e);
  }
}
