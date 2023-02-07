package edu.wpi.cs3733.C23.teamA.pathfinding;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Scanner;

/*
This class can read CSVs and puts the nodes or edges from
the csv into a graph. readEdges method should be run after the readNodes
method using the same node names and graph
 */
public class CSVReader {

  /**
   * This will add nodes to a specified "graph" (HashMap<String>, Node) from a .csv file.
   *
   * @param path The "Path from Repository Root" for a Node .csv file.
   * @param graph is the Graph with the HashMap<String, Node> we want to add Nodes too.
   * @throws IOException if the file cannot be found/read
   */
  public static void readNodes(String path, Graph graph) throws IOException {
    // create a scanner to import the CSV
    Scanner csvScanner = new Scanner(new File(path));
    csvScanner.useDelimiter(",");
    String row;

    if (csvScanner.hasNextLine()) {
      row = csvScanner.nextLine();
    }

    // go through the csv, print everything in it
    while (csvScanner.hasNextLine()) {
      row = csvScanner.nextLine();
      String[] entries = row.split(",");
      // Should have exactly 8 entries because there are 8 columns
      if (entries.length == 8) {
        // Creates a node with this row and adds it to the graph HashMap
        GraphNode node =
            new GraphNode(
                entries[0],
                Integer.parseInt(entries[1]),
                Integer.parseInt(entries[2]),
                entries[3],
                entries[4],
                entries[5],
                entries[6],
                entries[7]);
        graph.addNode(entries[0], node);
      } else {
        throw new InvalidPropertiesFormatException("Improper Number of Columns or Commas");
      }
    }

    // close the scanner
    csvScanner.close();
  }

  /**
   * This will connect nodes inside a specified "graph" (HashMap<String>, Node) from a .csv file.
   *
   * @param path is the path from repository root to an Edges csv file
   * @param graph is the HashMap<String, Node> with existing nodes in it that we want to add edges
   *     for
   * @throws IOException if the file cannot be found/read
   */
  public static void readEdges(String path, Graph graph) throws IOException {
    // create a scanner to import the CSV
    Scanner csvScanner = new Scanner(new File(path));
    csvScanner.useDelimiter(",");
    String row;

    // Ignore the first line since it is just titles
    if (csvScanner.hasNextLine()) {
      row = csvScanner.nextLine();
    }

    // go through the csv, add neighboring nodes to each other's neighbors attribute
    while (csvScanner.hasNextLine()) {
      row = csvScanner.nextLine();
      String[] entries = row.split(",");
      // Making sure the correct number of entries exist (should have 3 columns)
      if (entries.length == 3) {
        // node with value entries[1] adds entries[2] to neighbors and vice versa
        GraphNode node1 = graph.getGraph().get(entries[1]);
        GraphNode node2 = graph.getGraph().get(entries[2]);
        node1.addNeighbor(node2);
        node2.addNeighbor(node1);
      } else {
        throw new InvalidPropertiesFormatException("Improper Number of Columns or Commas");
      }
    }

    // close the scanner
    csvScanner.close();
  }

  /**
   * This will print out the rows of a .csv file; intended for testing/research purposes.
   *
   * @param path The "Path from Repository Root" for any .csv file.
   * @throws IOException if the file cannot be found/read
   */
  public static void readCSV(String path) throws IOException {
    // create a scanner to import the CSV
    Scanner csvScanner = new Scanner(new File(path));
    csvScanner.useDelimiter(",");

    while (csvScanner.hasNextLine()) {
      System.out.println(csvScanner.nextLine());
    }
  }
}
