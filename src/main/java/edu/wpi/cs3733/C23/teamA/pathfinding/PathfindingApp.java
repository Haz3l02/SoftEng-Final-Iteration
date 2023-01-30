package edu.wpi.cs3733.C23.teamA.pathfinding;

import edu.wpi.cs3733.C23.teamA.controllers.PathfindingController;
import java.io.IOException;
import java.util.*;

// The main class to run a pathfinding search using terminal line input
public class PathfindingApp {

  /*
  This main method allows a user to use the terminal and will print out
  the path between the two inputted nodes using A* search
   */
  public static void main(String[] args) {
    // create a HashMap to act as the hospital graph
    HashMap<String, GraphNode> hospitalL1;

    // try to initialize the graph
    try {
      hospitalL1 = PathfindingController.prepGraph();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Calls helper function to get input
    String[] nodes = getInput(hospitalL1);

    // run A*
    ArrayList<GraphNode> L1path = PathfindingController.callAStar(hospitalL1, nodes[0], nodes[1]);

    // print the final path, if found
    if (L1path == null) {
      System.out.println("Sorry, no path between these locations exists.");
    } else if (L1path.size() == 1) {
      System.out.println(
          "The starting and ending locations are the same, so there is no path between them.");
    } else {
      System.out.println(PathInterpreter.generatePathString(L1path));
    }
  }

  /**
   * Helper method to get input from the command line Gets the nodeID of the start node and the end
   * node
   *
   * @param hospitalL1 a HashMap of the graph
   * @return a pair of Strings (startNodeID, endNodeID)
   */
  private static String[] getInput(HashMap<String, GraphNode> hospitalL1) {
    // once the graph is successfully initialized, create a Scanner for user input and begin asking
    // the user for Node IDs
    Scanner userInput = new Scanner(System.in);

    boolean startDone = false;
    String startNodeID = "";
    boolean endDone = false;
    String endNodeID = "";

    // starting node
    while (!startDone) {
      System.out.print("Input the Node ID for your Starting Location: ");
      startNodeID = userInput.nextLine();

      if (hospitalL1.containsKey(startNodeID)) {
        startDone = true;
      } else {
        System.out.println("ERROR: This Node ID does not exist. Please input a valid ID. \n");
      }
    }

    // ending node
    while (!endDone) {
      System.out.print("Input the Node ID for your Ending Location: ");
      endNodeID = userInput.nextLine();

      if (hospitalL1.containsKey(endNodeID)) {
        endDone = true;
      } else {
        System.out.println("ERROR: This Node ID does not exist. Please input a valid ID. \n");
      }
    }
    System.out.println(); // formatting

    // close the scanner
    userInput.close();

    // return the nodes
    String[] nodes = new String[2];
    nodes[0] = startNodeID;
    nodes[1] = endNodeID;

    return nodes;
  }
}
