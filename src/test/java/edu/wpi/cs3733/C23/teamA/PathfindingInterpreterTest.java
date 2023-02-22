package edu.wpi.cs3733.C23.teamA;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.PathInterpreter;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class PathfindingInterpreterTest {
  // test nodes
  final GraphNode a1 = new GraphNode("a1", 1, 1, "a1");
  final GraphNode a2 = new GraphNode("a2", 3, 3, "a2");
  final GraphNode a3 = new GraphNode("a3", 5, 3, "a3");
  final GraphNode a4 = new GraphNode("a4", -1, -1, "a4");
  final GraphNode a5 = new GraphNode("a5", -4, 2, "a5");
  final GraphNode a6 = new GraphNode("a6", 3, -2, "a6");
  final GraphNode a7 = new GraphNode("a7", -5, -1, "a7");
  final GraphNode a8 = new GraphNode("a8", 0, 0, "a8");
  final GraphNode a9 = new GraphNode("a9", -2, 5, "a9");
  static HashMap<String, GraphNode> testGraph = new HashMap<>();

  @Test
  public void simpleTest_one() {
    String dir = PathInterpreter.getDirection(a1, a2, a3);
    boolean containsRight = dir.contains("right"); // maybe not needed
    // String expected = "Turn right and go __ feet to " + a3.getLongName();
    // assertEquals(dir, expected);
    assertTrue(containsRight);
  }

  @Test
  public void simpleTest_two() {
    String dir = PathInterpreter.getDirection(a1, a8, a4);
    boolean containsStraight = dir.contains("straight");
    assertTrue(containsStraight);
  }

  @Test
  public void simpleTest_three() {
    String dir = PathInterpreter.getDirection(a1, a9, a5);
    boolean containsLeft = dir.contains("left");
    assertTrue(containsLeft);
  }

  @Test
  public void simpleTest_four() {
    String dir = PathInterpreter.getDirection(a1, a9, a2);
    boolean containsRight = dir.contains("right");
    assertTrue(containsRight);
  }

  @Test
  public void simpleTest_five() {
    String dir = PathInterpreter.getDirection(a6, a1, a2);
    boolean containsRight = dir.contains("right");
    assertTrue(containsRight);
  }

  @Test
  public void simpleTest_six() {
    String dir = PathInterpreter.getDirection(a1, a9, a6);
    boolean contains180 = dir.contains("way around");
    assertTrue(contains180);
  }

  /*
  @Test
  public void fullPath_one() {
    ArrayList<GraphNode> path = new ArrayList<>();
    path.add(a1);
    path.add(a2);
    path.add(a3);
    path.add(a9);

    String pathString = PathInterpreter.generatePathString(path);
    StringBuilder output = new StringBuilder();
    output.append("Path from " + "a1" + " to " + "a9" + ":\n\n");
    output.append("Start at " + "a1" + ".\n");
    output.append("Go toward " + "a2" + ".\n");
    output.append("Turn right and go to a3.\n");
    output.append("Turn left and go to a9.\n");
    output.append("You have reached " + "a9" + ".\n");

    assertEquals(pathString, output.toString());
  }

  @Test
  public void fullPath_two() {
    ArrayList<GraphNode> path = new ArrayList<>();
    path.add(a8);
    path.add(a1);
    path.add(a2);
    path.add(a6);
    path.add(a3);

    String pathString = PathInterpreter.generatePathString(path);
    StringBuilder output = new StringBuilder();
    output.append("Path from " + "a8" + " to " + "a3" + ":\n\n");
    output.append("Start at " + "a8" + ".\n");
    output.append("Go toward " + "a1" + ".\n");
    output.append("Continue straight and go to a2.\n");
    output.append("Turn right and go to a6.\n");
    output.append("Turn left and go to a3.\n");
    output.append("You have reached " + "a3" + ".\n");
    assertEquals(pathString, output.toString());
  }
   */
}
