package edu.wpi.cs3733.C23.teamA.Database.API;

import com.sun.javafx.geom.Edge;
import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.IOException;
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

  public void addNode(NodeEntity c) {
    node.add(c);
  }

  public void addSanitationRequest(SanitationRequestEntity c) {
    san.add(c);
  }

  public void addSecurityRequest(SecurityRequestEntity c) {
    sec.add(c);
  }

  public void addServiceRequest(ServiceRequestEntity c) {
    serv.add(c);
  }

  public void deleteComputerRequest(Integer id){
    comp.delete(id);
  }

  public void deleteEdge(String id){
    edge.delete(id);
  }

  public void deleteEmployee(String id){
    emp.delete(id);
  }

  public void deleteLocation(String id){
    loc.delete(id);
  }

  public void deleteMove(List<String> id){
    move.delete(id);
  }

  public void deleteNode(String id){
    node.delete(id);
  }

  public void deleteSanitationRequest(Integer id){
    san.delete(id);
  }

  public void deleteSecurityRequest(Integer id){
    sec.delete(id);
  }

  public void deleteServiceRequest(Integer id){
    serv.delete(id);
  }


  public void exportEdges(String filename) throws IOException {
    edge.exportToCSV(filename);
  }

  public void exportEmployee(String filename) throws IOException {
    emp.exportToCSV(filename);
  }

  public void exportLocation(String filename) throws IOException {
    loc.exportToCSV(filename);
  }

  public void exportMove(String filename) throws IOException {
    move.exportToCSV(filename);
  }

  public void exportNode(String filename) throws IOException {
    node.exportToCSV(filename);
  }


}
