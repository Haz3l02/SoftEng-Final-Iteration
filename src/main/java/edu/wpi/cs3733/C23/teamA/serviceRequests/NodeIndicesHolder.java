package edu.wpi.cs3733.C23.teamA.serviceRequests;

import oracle.ucp.common.waitfreepool.Tuple;

public final class NodeIndicesHolder {

  private Tuple<Integer, Integer> nodes;
  private static final NodeIndicesHolder INSTANCE = new NodeIndicesHolder();

  private NodeIndicesHolder() {}

  public static NodeIndicesHolder getInstance() {
    return INSTANCE;
  }

  public void setNodes(Tuple<Integer, Integer> nodes) {
    this.nodes = nodes;
  }

  public Tuple<Integer, Integer> getNodes() {
    return this.nodes;
  }
}
