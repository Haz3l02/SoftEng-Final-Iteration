package edu.wpi.cs3733.C23.teamA.controllers;

import static edu.wpi.cs3733.C23.teamA.controllers.MapEditorController.mapEditor;

import edu.wpi.cs3733.C23.teamA.Database.Entities.EdgeEntity;
import edu.wpi.cs3733.C23.teamA.Database.Entities.NodeEntity;
import edu.wpi.cs3733.C23.teamA.mapdrawing.NodeDraw;
import edu.wpi.cs3733.C23.teamA.navigation.Navigation;
import edu.wpi.cs3733.C23.teamA.navigation.Screen;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

public class StraightConfirmPopupController {

  public void initialize() {}

  public static boolean h = false;
  public static boolean v = false;

  public static NodeEntity referenceNode;
  public static List<NodeEntity> selectedNodeList;

  // for nodeDraw
  @Setter public static List<NodeEntity> allNodes;
  @Setter public static double SCALE_FACTOR;
  @Setter public static AnchorPane mainAnchorPane;
  @Setter public static MapEditorController MEC;
  @Setter public static List<EdgeEntity> allEdges;

  public static void setReferenceNode(NodeEntity n) {
    referenceNode = n;
  }

  public static void setNodeList(List<NodeEntity> l) {
    selectedNodeList = l;
  }

  public void horizontal(javafx.event.ActionEvent event) {
    // h = true;
    System.out.println("Hoz");
    mapEditor.closePopup("straightening");
    NodeDraw.straightenNodesHorizontal(referenceNode, selectedNodeList);
    Navigation.navigate(Screen.NODE_MAP);

    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, MEC);
    NodeDraw.drawEdges(allEdges, SCALE_FACTOR, mainAnchorPane);
  }

  public void vertical(javafx.event.ActionEvent event) {

    NodeDraw.straightenNodesVertical(referenceNode, selectedNodeList);
    // v = true;
    mapEditor.closePopup("straightening");
    Navigation.navigate(Screen.NODE_MAP);
    NodeDraw.drawNodes(allNodes, SCALE_FACTOR, mainAnchorPane, MEC);
  }

  public static boolean getH() {
    return h;
  }

  public static boolean getV() {
    return v;
  }

  public static void setH() {
    h = false;
  }

  public static void setV() {
    v = false;
  }
}
