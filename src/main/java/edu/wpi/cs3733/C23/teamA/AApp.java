package edu.wpi.cs3733.C23.teamA;

import static edu.wpi.cs3733.C23.teamA.Database.API.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AApp extends Application {

  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    AApp.primaryStage = primaryStage;

    // the following is the ROOT: !!!!! NEVER EVER CHANGE THIS TO ANYTHING OTHER THAN
    // "views/Root.fxml" !!!!!
    final FXMLLoader loader = new FXMLLoader(AApp.class.getResource("views/Root.fxml"));
    final BorderPane root = loader.load();

    AApp.rootPane = root;

    final Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setMaximized(true);
    Navigation.navigateHome(Screen.PATHFINDING);
    primaryStage.setMinWidth(615);
    primaryStage.setMinHeight(450);
  }

  @Override
  public void stop() {
    getSessionFactory().close();
    log.info("Shutting Down");
    System.exit(0);
  }
}
