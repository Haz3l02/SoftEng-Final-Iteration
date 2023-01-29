package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import edu.wpi.cs3733.C23.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class PathfindingController {

  // javaFX items
  @FXML private MFXTextField startNodeID;
  @FXML private MFXTextField endNodeID;
  // @FXML private Button findPathButton; // to generate & print the path
  @FXML private Text pathDisplay; // to display the generated path
  @FXML private Text reminder;

  @FXML
  public void generatePath() {
    if (startNodeID.getText().equals("") || endNodeID.getText().equals("")) {
      reminder.setText("Please fill out all fields in the form!");
    } else {
      // create the graph hashMap
      HashMap<String, Node> hospitalL1;
      System.out.println("Prininfjgdndjkf");

      // try to initialize the graph. If it fails, throw an error
      try {
        hospitalL1 = prepGraph();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // get the nodeIDs from the application
      String sID = startNodeID.getText();
      String eID = endNodeID.getText();

      // run A*
      ArrayList<Node> path = callAStar(hospitalL1, sID, eID);

      // print the path to the textField
      pathDisplay.setText(PathInterpreter.generatePathString(path));
    }
  }

  /**
   * @return a HashMap<String, Node> representing a graph, read from .csv files
   * @throws IOException if the files cannot be read
   */
  public static HashMap<String, Node> prepGraph() throws IOException {
    // create a graph to hold the L1 information
    HashMap<String, Node> graph = new HashMap<>();

    // add the L1 CSV information to the graph (TO BE UPDATED LATER)
    CSVReader.readNodes("src/main/resources/edu/wpi/cs3733/C23/teamA/mapCSV/L1Nodes.csv", graph);
    CSVReader.readEdges("src/main/resources/edu/wpi/cs3733/C23/teamA/mapCSV/L1Edges.csv", graph);

    return graph;
  }

  /**
   * @param graph a HashMap<String, Node> (normally) generated by prepDFS() from reading .csv files
   * @param startNodeID the ID of the node where the user wants to start from
   * @param endNodeID the ID of the node that the user wants to get to
   * @return the path between startNode and endNode, if it exists. Otherwise, null.
   */
  public static ArrayList<Node> callDFS(
      HashMap<String, Node> graph, String startNodeID, String endNodeID) {
    return DFS.traverse(graph.get(startNodeID), graph.get(endNodeID));
  }

  /**
   * @param graph a HashMap<String, Node> (normally) generated by prepDFS() from reading .csv files
   * @param startNodeID the ID of the node where the user wants to start from
   * @param endNodeID the ID of the node that the user wants to get to
   * @return the path between startNode and endNode, if it exists. Otherwise, null.
   */
  public static ArrayList<Node> callAStar(
      HashMap<String, Node> graph, String startNodeID, String endNodeID) {
    return AStar.traverse(graph.get(startNodeID), graph.get(endNodeID));
  }

  @FXML
  public void switchToHomeScene(ActionEvent event) throws IOException {
    Navigation.navigate(Screen.HOME);
  }

  public void logout(ActionEvent actionEvent) {}

  @FXML
  void clearForm() {
    startNodeID.clear();
    endNodeID.clear();
  }

  @FXML
  public void switchToHomeServiceRequest(ActionEvent event) throws IOException {
    //Navigation.navigate(Screen.HOME_SERVICE_REQUEST);
  }

  @FXML
  public void switchToHelpScene(ActionEvent event) throws IOException {
    //Navigation.navigate(Screen.HELP);
  }
}
