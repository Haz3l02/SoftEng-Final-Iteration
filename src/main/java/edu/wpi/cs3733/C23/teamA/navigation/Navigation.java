package edu.wpi.cs3733.C23.teamA.navigation;

import edu.wpi.cs3733.C23.teamA.AApp;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

public class Navigation {

  public static void navigate(final Screen screen) {
    final String filename = screen.getFilename();

    try {
      final var resource = AApp.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);

      final var sourse = AApp.class.getResource("views/SecondaryMenuBarFXML.fxml");
      final FXMLLoader please = new FXMLLoader(sourse);
      AApp.getRootPane().setTop(please.load());
      AApp.getRootPane().setCenter(loader.load());

    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public static void navigateHome(final Screen screen) {
    final String filename = screen.getFilename();

    try {
      final var resource = AApp.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);

      AApp.getRootPane().setTop(null);
      AApp.getRootPane().setCenter(loader.load());

    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public static void close() {
    Platform.exit();
  }
}
