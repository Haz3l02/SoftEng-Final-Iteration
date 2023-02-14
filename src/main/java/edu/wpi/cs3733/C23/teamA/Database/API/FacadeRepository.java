package edu.wpi.cs3733.C23.teamA.Database.API;

import com.sun.javafx.geom.Edge;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.*;
import java.util.List;

public class FacadeRepository {
  private static final FacadeRepository instance = new FacadeRepository();

  private final ComputerRequestImpl comp = ComputerRequestImpl.getInstance();
  private final EdgeImpl edge = EdgeImpl.getInstance();
  private final EmployeeImpl emp = EmployeeImpl.getInstance();
  private final LocationNameImpl loc = LocationNameImpl.getInstance();
  private final NodeImpl node = NodeImpl.getInstance();
  private final MoveImpl move = MoveImpl.getInstance();
  private final SanitationRequestImpl san = SanitationRequestImpl.getInstance();
  private final SecurityRequestImpl sec = SecurityRequestImpl.getInstance();
  private final ServiceRequestImpl serv = ServiceRequestImpl.getInstance();

  public static FacadeRepository getInstance() {
    return instance;
  }

  public List<ServiceRequestEntity> getAllServiceRequest() {
    return serv.getAll();
  }

  public List<ComputerRequestEntity> getAllComputerRequest() {
    return comp.getAll();
  }

  public List<EdgeEntity> getAllEdge() {
    return edge.getAll();
  }

  public List<EmployeeEntity> getAllEmployee() {
    return emp.getAll();
  }

  public List<MoveEntity> getAllMove() {
    return move.getAll();
  }

  public List<NodeEntity> getAllNode() {
    return node.getAll();
  }

  public List<SanitationRequestEntity> getAllSanitationRequest() {
    return san.getAll();
  }

  public List<SecurityRequestEntity> getSecurityRequest() {
    return sec.getAll();
  }

  public void addComputerRequest(ComputerRequestEntity c) {
    comp.add(c);
  }

  public void addEdge(EdgeEntity c) {
    edge.add(c);
  }

  public void addEmployee(EmployeeEntity c) {
    emp.add(c);
  }

  public void addLocation(LocationNameEntity c) {
    loc.add(c);
  }

  public void addMove(MoveEntity c) {
    move.add(c);
  }

}
