package edu.wpi.cs3733.C23.teamA.pathfinding.readers;

import edu.wpi.cs3733.C23.teamA.Database.API.FacadeRepository;
import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.LocationNameEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.MoveEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.pathfinding.Graph;
import edu.wpi.cs3733.C23.teamA.pathfinding.GraphNode;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.LocationType;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DBReader {

  /**
   * Reads the database using a Hibernate session to populate it with locational nodes and edges
   *
   * @param graph a Graph object
   */
  public static void readDB(Graph graph, LocalDate navDate) throws SQLException {
    // Nodes
    LocationNameEntity locNameEnt;
    MoveEntity moveEntity;
    List<NodeEntity> allNodes =
        FacadeRepository.getInstance().getAllNode(); // gets all the nodes in db's node table

    // loop through all the nodes, adding them to the graph specified
    for (NodeEntity n : allNodes) {
      // THIS WILL NEED TO CHANGE IN ITERATION 3
      moveEntity =
          FacadeRepository.getInstance().moveLocationOnOrBeforeDate(n.getNodeid(), navDate);
      locNameEnt = moveEntity.getLocationName();
      GraphNode g;
      // create the nodes; if there's no LocationNameEntity, it's a node w/ no location attached
      if (locNameEnt != null) {
        g =
            new GraphNode(
                n.getNodeid(),
                n.getXcoord(),
                n.getYcoord(),
                locNameEnt.getLongname(),
                locNameEnt.getLocationtype(),
                n.getFloor());
      } else {
        g =
            new GraphNode(
                n.getNodeid(),
                n.getXcoord(),
                n.getYcoord(),
                "UNNAMED NODE",
                LocationType.UNKN.getTableString(), // what to do here?
                n.getFloor());
      }
      // create the graph and add the nodes (id, xcoord, ycoord, longName, locationType)
      graph.addNode(n.getNodeid(), g);
    }

    // Edges

    /* read through edge columns and add edges to correct node (bidirectional) */
    List<EdgeEntity> allEdges =
        FacadeRepository.getInstance()
            .getAllEdge(); // Gets list of all edges from database's edge table
    for (EdgeEntity e : allEdges) {
      GraphNode node1 = graph.getNode(e.getNode1().getNodeid());
      GraphNode node2 = graph.getNode(e.getNode2().getNodeid());
      node1.addNeighbor(node2);
      node2.addNeighbor(node1);
    }
  }
}
