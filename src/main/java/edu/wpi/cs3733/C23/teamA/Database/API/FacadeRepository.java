package edu.wpi.cs3733.C23.teamA.Database.API;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Implementation.*;
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
  private final PatientTransportimpl pat = PatientTransportimpl.getInstance();
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

  public List<PatientTransportRequestEntity> getPatientTransport() {
    return pat.getAll();
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

  public void addPatientTransport(PatientTransportRequestEntity c) {
    pat.add(c);
  }

  public void addServiceRequest(ServiceRequestEntity c) {
    serv.add(c);
  }

  public void deleteComputerRequest(Integer id) {
    comp.delete(id);
  }

  public void deleteEdge(String id) {
    edge.delete(id);
  }

  public void deleteEmployee(String id) {
    emp.delete(id);
  }

  public void deleteLocation(String id) {
    loc.delete(id);
  }

  public void deleteMove(List<String> id) {
    move.delete(id);
  }

  public void deleteNode(String id) {
    node.delete(id);
  }

  public void deleteSanitationRequest(Integer id) {
    san.delete(id);
  }

  public void deleteSecurityRequest(Integer id) {
    sec.delete(id);
  }
  public void deletePatientTransport(Integer id) {
    pat.delete(id);
  }

  public void deleteServiceRequest(Integer id) {
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

  public void exportService(String filename) throws IOException {
    serv.exportToCSV(filename);
  }

  public void importEdge(String filename) throws IOException {
    edge.importFromCSV(filename);
  }

  public void importNode(String filename) throws IOException {
    node.importFromCSV(filename);
  }

  public void importMove(String filename) throws IOException {
    move.importFromCSV(filename);
  }

  public void importEmployee(String filename) throws IOException {
    emp.importFromCSV(filename);
  }

  public ComputerRequestEntity getComputerRequest(Integer id) {
    return comp.get(id);
  }

  public EdgeEntity getEdge(String id) {
    return edge.get(id);
  }

  public EmployeeEntity getEmployee(String id) {
    return emp.get(id);
  }

  public LocationNameEntity getLocation(String id) {
    return loc.get(id);
  }

  public MoveEntity getMove(List<String> id) {
    return move.get(id);
  }

  public NodeEntity getNode(String id) {
    return node.get(id);
  }

  public SanitationRequestEntity getSanitationRequest(Integer id) {
    return san.get(id);
  }

  public SecurityRequestEntity getSecurityRequest(Integer id) {
    return sec.get(id);
  }
  public PatientTransportRequestEntity getPatientTransport(Integer id) {
    return pat.get(id);
  }

  public ServiceRequestEntity getServiceRequest(Integer id) {
    return serv.get(id);
  }

  public void updateComputerRequest(Integer id, ComputerRequestEntity c) {
    comp.update(id, c);
  }


  public void updateEdge(String id, EdgeEntity c) {
    edge.update(id, c);
  }

  public void updateEmployee(String id, EmployeeEntity c) {
    emp.update(id, c);
  }

  public void updateLocation(String id, LocationNameEntity c) {
    loc.update(id, c);
  }

  public void updateMove(List<String> id, MoveEntity c) {
    move.update(id, c);
  }

  public void updateNode(String id, NodeEntity c) {
    node.update(id, c);
  }

  public void updateSanitationRequest(Integer id, SanitationRequestEntity c) {
    san.update(id, c);
  }

  public void updateSecurityRequest(Integer id, SecurityRequestEntity c) {
    sec.update(id, c);
  }
  public void updatePatientTransport(Integer id, PatientTransportRequestEntity c) {
    pat.update(id, c);
  }

  public void updateServiceRequest(Integer id, ServiceRequestEntity c) {
    serv.update(id, c);
  }
}
