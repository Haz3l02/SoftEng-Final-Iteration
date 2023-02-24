package edu.wpi.cs3733.C23.teamA.Database.API;

import edu.wpi.cs3733.C23.teamA.Database.Entities.*;
import edu.wpi.cs3733.C23.teamA.Database.Entities.Implementation.*;
import edu.wpi.cs3733.C23.teamA.enums.Status;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
  private final AccessabilityImpl acc = AccessabilityImpl.getInstance();
  private final AudioVisualImpl av = AudioVisualImpl.getInstance();

  private final Observer nodeObv = new EntityObserver(node); // Notify for edge, move
  private final Observer edgeObv = new EntityObserver(edge);
  private final Observer moveObv = new EntityObserver(move);
  private final Observer locObv = new EntityObserver(loc); // Notify all requests, move
  private final Observer sanObv = new EntityObserver(san);
  private final Observer empObv = new EntityObserver(emp); // Notify all requests
  private final Observer secObv = new EntityObserver(sec);
  private final Observer servObv = new EntityObserver(serv);
  private final Observer patObv = new EntityObserver(pat);
  private final Observer compObv = new EntityObserver(serv);
  private final Observer accObv = new EntityObserver(acc);
  private final Observer avObv = new EntityObserver(av);

  private FacadeRepository() {
    loc.attach(sanObv);
    loc.attach(secObv);
    loc.attach(servObv);
    loc.attach(patObv);
    loc.attach(compObv);
    loc.attach(accObv);
    loc.attach(accObv);
    loc.attach(moveObv);
    emp.attach(secObv);
    emp.attach(servObv);
    emp.attach(patObv);
    emp.attach(compObv);
    emp.attach(accObv);
    node.attach(edgeObv);
    node.attach(moveObv);
  }

  public static FacadeRepository getInstance() {
    return instance;
  }

  // GETALL METHODS

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

  public List<EmployeeEntity> getEmployeeByJob(String job) {
    List<EmployeeEntity> temp = new ArrayList<EmployeeEntity>();
    for (EmployeeEntity employee : emp.getAll()) {
      if (employee.getJob().equalsIgnoreCase(job)) {
        temp.add(employee);
      }
    }
    return temp;
  }

  public List<LocationNameEntity> getAllLocation() {
    return loc.getAll();
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

  public List<SecurityRequestEntity> getAllSecurityRequest() {
    return sec.getAll();
  }

  public List<PatientTransportRequestEntity> getAllPatientTransport() {
    return pat.getAll();
  }

  public List<AccessibilityRequestEntity> getAllAccessabilityRequest() {
    return acc.getAll();
  }

  public List<AudioVisualRequestEntity> getAllAudioVisualRequest() {
    return av.getAll();
  }

  // ADD METHODS

  public void addComputerRequest(ComputerRequestEntity c) {
    comp.add(c);
  }

  public void addAudioVisualRequest(AudioVisualRequestEntity c) {
    av.add(c);
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

  public void addAccessability(AccessibilityRequestEntity c) {
    acc.add(c);
  }

  // DELETE METHODS

  public void deleteComputerRequest(Integer id) {
    comp.delete(id);
  }

  public void deleteEdge(String id) {
    edge.delete(id);
  }

  public void deleteEmployee(Integer id) {
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

  public void deleteAccessibilityRequest(Integer id) {
    acc.delete(id);
  }

  public void deleteAudioVisualRequest(Integer id) {
    av.delete(id);
  }

  // EXPORT METHODS

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

  public void exportAccessibility(String filename) throws IOException {
    acc.exportToCSV(filename);
  }

  public void exportAudioVisual(String filename) throws IOException {
    av.exportToCSV(filename);
  }

  // IMPORT METHODS

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

  public void importAccessability(String filename) throws IOException {
    acc.importFromCSV(filename);
  }

  // GET ID METHODS

  public ComputerRequestEntity getComputerRequest(Integer id) {
    return comp.get(id);
  }

  public AudioVisualRequestEntity getAVRequest(Integer id) {
    return av.get(id);
  }

  public EdgeEntity getEdge(String id) {
    return edge.get(id);
  }

  public EmployeeEntity getEmployee(Integer id) {
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

  public AccessibilityRequestEntity getAccessabilityRequest(Integer id) {
    return acc.get(id);
  }

  // UPDATE METHODS

  public void updateComputerRequest(Integer id, ComputerRequestEntity c) {
    comp.update(id, c);
  }

  public void updateEdge(String id, EdgeEntity c) {
    edge.update(id, c);
  }

  public void updateEmployee(Integer id, EmployeeEntity c) {
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

  public void updateAccessibilityRequest(Integer id, AccessibilityRequestEntity c) {
    acc.update(id, c);
  }

  public void updateAudioVisualRequest(Integer id, AudioVisualRequestEntity c) {
    av.update(id, c);
  }

  // miscellaneous

  public HashMap<EdgeEntity, List<EdgeEntity>> getNodeVectors(NodeEntity e) {
    return edge.nodeVectors(e);
  }

  public void exportAll(String filename) throws IOException {
    comp.exportToCSV(filename);
    edge.exportToCSV(filename);
    emp.exportToCSV(filename);
    loc.exportToCSV(filename);
    move.exportToCSV(filename);
    node.exportToCSV(filename);
    pat.exportToCSV(filename);
    san.exportToCSV(filename);
    sec.exportToCSV(filename);
    serv.exportToCSV(filename);
  }

  public void collapseNode(NodeEntity e) {
    edge.collapseNode(e);
    node.delete(e.getNodeid());
  }

  public List<String> getListEmployeeOfByJob(String job) {
    return emp.getListOfByJob(job);
  }

  public EmployeeEntity getEmployeeByUser(String user) {
    return emp.getByUsername(user);
  }

  public ArrayList<String> employeeCheckPass(String user, String pass) {
    return emp.checkPass(user, pass);
  }

  public List<String> getAllLocationIDS() {
    return loc.getAllIDs();
  }

  public HashMap<MoveEntity, MoveEntity> moveLocationChanges(LocalDate minDate, LocalDate maxDate) {
    return move.locationChanges(minDate, maxDate);
  }

  public List<MoveEntity> moveAllMostRecent(LocalDate date) {
    return move.allMostRecent(date);
  }

  public List<MoveEntity> moveAllMostRecentFloor(LocalDate date, String floor) {
    return move.allMostRecentFloor(date, floor);
  }

  public List<MoveEntity> moveLocationRecord(String id, LocalDate date) {
    return move.locationRecord(id, date);
  }

  public List<MoveEntity> moveLocationRecordFloor(String id, LocalDate date, String floor) {
    return move.locationRecordFloor(id, date, floor);
  }

  public MoveEntity moveLocationOnOrBeforeDate(String id, LocalDate date) {
    return move.locationOnOrBeforeDate(id, date);
  }

  public LocationNameEntity moveMostRecentLoc(String id) {
    return move.mostRecentLoc(id);
  }

  //  public void newLocationOnNode(String nodeid, LocationNameEntity l) {
  //    loc.newLocationOnNode(nodeid, l);
  //  }

  public List<NodeEntity> getNodesOnFloor(String floor) {
    return node.getNodeOnFloor(floor);
  }

  public List<EdgeEntity> getEdgesOnFloor(String floor) {
    return edge.getEdgeOnFloor(floor);
  }

  public List<String> getAllNodeIDs() {
    return node.getAllIDs();
  }

  public ArrayList<ServiceRequestEntity> getAllServByEmployee(Integer id) {
    return serv.getAllByEmployee(id);
  }

  public ArrayList<ServiceRequestEntity> getServiceRequestByAssigned(int ID) {
    return serv.getServiceRequestByAssigned(ID);
  }

  public ArrayList<ServiceRequestEntity> getServiceRequestByUnassigned() {
    return serv.getServiceRequestByUnassigned();
  }

  public void updateStatusOfServ(Status status, Integer ID) {
    serv.updateStatus(status, ID);
  }

  public void updateServEmployee(EmployeeEntity employee, Integer ID) {
    serv.updateEmployee(employee, ID);
  }

  public HashMap<MoveEntity, MoveEntity> getLocationChanges(LocalDate minDate, LocalDate maxDate) {
    return move.locationChanges(minDate, maxDate);
  }

  public HashMap<MoveEntity, MoveEntity> getLocationChangesFloor(
      LocalDate minDate, LocalDate maxDate, String floor) {
    return move.locationChangesFloor(minDate, maxDate, floor);
  }

  public void newLocationOnNode(String nodeid, LocationNameEntity l) {
    loc.newLocationOnNode(nodeid, l);
  }

  public ArrayList<ServiceRequestEntity> getOutstandingServRequests() {
    return serv.getOutstandingRequests();
  }

  public ArrayList<ServiceRequestEntity> getRequestAtLocation(String longname) {
    return serv.getRequestAtLocation(longname);
  }

  public void removeAssociatedLocationsOnMove(String nodeid) {
    move.removeAssociatedLocations(nodeid);
  }

  public ArrayList<ServiceRequestEntity> getOutstandingRequestsByID(String user) {
    return serv.getOutstandingRequestsByID(user);
  }

  public boolean employeeUsernameExists(String user) {
    return emp.usernameExists(user);
  }

  public List<ServiceRequestEntity> getRequestAtCoordinate(int x, int y, String floor) {
    return serv.getRequestAtCoordinate(x, y, floor);
  }


  public void exportAlignedToCSV(String filename, ArrayList<NodeEntity> n) throws IOException {
    node.exportAlignedToCSV(filename,  n);
  }
}
