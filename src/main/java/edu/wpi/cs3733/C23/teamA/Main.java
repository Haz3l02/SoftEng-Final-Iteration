package edu.wpi.cs3733.C23.teamA;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    try {
      AApp.launch(AApp.class, args);
    } catch (Exception e) {
      System.out.println("EXCEPTION\n\n\n");
      e.printStackTrace();
    }
  }
}
