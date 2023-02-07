package edu.wpi.cs3733.C23.teamA.pathfinding;

import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getAllRecords;
import static edu.wpi.cs3733.C23.teamA.hibernateDB.ADBSingletonClass.getSessionFactory;

import edu.wpi.cs3733.C23.teamA.databases.Edge;
import edu.wpi.cs3733.C23.teamA.databases.Move;
import edu.wpi.cs3733.C23.teamA.databases.Node;
import edu.wpi.cs3733.C23.teamA.hibernateDB.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.hibernateDB.NodeEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

public class DBReader {

  /**
   * Reads the database using a Hibernate session to populate it with locational nodes and edges
   *
   * @param graph a Graph object
   */
  public static void readDB(Graph graph) throws SQLException {
    Session session = getSessionFactory().openSession();

    // Nodes
    List<NodeEntity> allNodes =
        getAllRecords(NodeEntity.class, session); // gets all the nodes in db's node table
    for (NodeEntity n : allNodes) {
      // create the graph and add the nodes (id, xcoord, ycoord, longName)
      GraphNode g =
          new GraphNode(
              n.getNodeid(), n.getXcoord(), n.getYcoord(), Move.mostRecentLoc(n.getNodeid()));
      graph.addNode(n.getNodeid(), g);
    }

    // Edges
    /* read through edge columns and add edges to correct node (bidirectional) */
    List<EdgeEntity> allEdges =
        getAllRecords(
            EdgeEntity.class, session); // Gets list of all edges from database's edge table
    for (EdgeEntity e : allEdges) {
      GraphNode node1 = graph.getNode(e.getNode1().getNodeid());
      GraphNode node2 = graph.getNode(e.getNode2().getNodeid());
      node1.addNeighbor(node2);
      node2.addNeighbor(node1);
    }
    session.close();
  }

  /**
   * Reads the database using AWS to populate the given graph with location nodes and edges
   *
   * @param graph a Graph object
   * @throws SQLException
   */
  public static void readDBOld(Graph graph) throws SQLException {

    // Nodes
    ArrayList<Node> allNodes = Node.getAll();
    for (Node n : allNodes) {
      // create the graph and add the nodes (id, xcoord, ycoord, longName)
      GraphNode g =
          new GraphNode(
              n.getNodeID(), n.getXcoord(), n.getYcoord(), Move.mostRecentLoc(n.getNodeID()));
      graph.addNode(n.getNodeID(), g);
    }

    // Edges
    /* read through edge columns and add edges to correct node (bidirectional) */
    ArrayList<Edge> allEdges = Edge.getAll(); // Gets list of all edges from database's edge table
    for (Edge e : allEdges) {
      GraphNode node1 = graph.getNode(e.getNode1());
      GraphNode node2 = graph.getNode(e.getNode2());
      node1.addNeighbor(node2);
      node2.addNeighbor(node1);
    }
  }
}
