package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import edu.wpi.cs3733.C23.teamA.mapeditor.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;
import org.hibernate.Session;

public class NodeMapController extends MenuController {

  @FXML private Canvas nodeMapCanvas; // to display the generated path
  @FXML private ImageView nodeMapImage;
  @FXML private GesturePane gPane;
  @FXML StackPane sPane;

  @FXML Label loc;
  @FXML Label XCord;
  @FXML Label YCord;

  @FXML AnchorPane nodeAnchor;

  @Setter NodeEntity selectedNode = null;

  // Lists of Nodes and Node Data
  private List<NodeEntity> allNodes;
  private GraphicsContext gc;

  // scaling constant
  private double SCALE_FACTOR; // constant for map size/coordinate manipulation

  public void initialize() {
    Session session = getSessionFactory().openSession();
    allNodes = NodeEntity.getNodeOnFloor("L1", session); // get all nodes from Database
    session.close();
    //    // Add Image
    addFloorMapImage(
        "src/main/resources/edu/wpi/cs3733/C23/teamA/assets/unlabeledMaps/00_thelowerlevel1_unlabeled.png");

    SCALE_FACTOR = gPane.getCurrentScale();

    // Add nodes as circles
    // gc = nodeMapCanvas.getGraphicsContext2D();
    NodeDraw.drawNodes(allNodes, 0.15, nodeAnchor, this);

    if (selectedNode != null) {
      loc.setText(selectedNode.getNodeid());
    }

    // Pane

    nodeAnchor.setOnMouseClicked(
        e -> {
          double x = e.getX();
          double y = e.getY();

          if (e.getButton() == MouseButton.PRIMARY) {

          } else if (e.getButton() == MouseButton.SECONDARY) {
            final Pane nodeGraphic = new Pane();

            /* Set the style of the node */
            nodeGraphic.setPrefSize(4, 4);
            nodeGraphic.setLayoutX(x - 4);
            nodeGraphic.setLayoutY(y - 4);
            nodeGraphic.setStyle(
                "-fx-background-color: '#F6BD38'; "
                    + "-fx-background-radius: 12.5; "
                    + "-fx-border-color: '013A75'; "
                    + "-fx-border-width: 1;"
                    + "-fx-border-radius: 12.5");

            nodeAnchor.getChildren().add(nodeGraphic);
          }
        });
  }
  /**
   * Updates the mapImage asset to contain an image (which is supposed to be a floor map)
   *
   * @param pathName the path to the image to be added
   */
  private void addFloorMapImage(String pathName) {

    File file = new File(pathName);
    Image image = new Image(file.toURI().toString());
    nodeMapImage.setImage(image);

    Node node = sPane;

    this.gPane.setContent(node);
    this.gPane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    System.out.println("Assigned the stackPane to gesture");
  }

  @FXML
  public void switchToNodeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.NODE);
  }

  public void setLoc(String location) {
    this.loc.setText(location);
  }

  public void setXCord(String x) {
    this.XCord.setText(x);
  }

  public void setYCord(String y) {
    this.YCord.setText(y);
  }
}
