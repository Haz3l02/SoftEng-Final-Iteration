package edu.wpi.cs3733.C23.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/HomeFXML.fxml"),
  HOME_ACTUAL("views/NewHomeMaintenanceFXML.fxml"),
  HOME_MAINTENANCE("views/NewHomeMaintenanceFXML.fxml"),
  HOME_ADMIN("views/HomeAdminFXML.fxml"),
  HOME_EMPLOYEE("views/HomeEmployeeFXML.fxml"),
  CONFIRMATION("views/ConfirmationFXML.fxml"),
  PATIENT_CONFIRMATION("views/PatientTransportConformationFXML.fxml"),
  COMPUTER("views/ComputerFXML.fxml"),
  SANITATION("views/SanitationFXML.fxml"),
  SECURITY("views/SecurityFXML.fxml"),
  AUDIOVISUAL("views/AudioVisualFXML.fxml"),
  ACCESSIBILITY("views/AccessibilityFXML.fxml"),
  HELP("views/HelpFXML.fxml"),
  HOME_SERVICE_REQUEST("views/HomeServiceRequestSMALLFXML.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  MOVE("views/MoveFXML.fxml"),
  NODE("views/NodeSMALLFXML.fxml"),
  NODEOLD("views/NodeFXML.fxml"),
  EDGE("views/EdgeFXML.fxml"),
  PATHFINDING("views/PathfindingNewFXML.fxml"),
  SERVICE_REQUEST_STATUS("views/ServiceRequestStatusSMALLFXML.fxml"),
  ID_INPUT("views/IDInputFXML.fxml"),
  LOGIN("views/LoginFXML.fxml"),
  NODE_MAP("views/MapEditorFXML.fxml"),
  EMPLOYEE("views/EmployeeFXML.fxml"),
  HOME_DATABASE("views/HomeDatabaseFXML.fxml"),
  MAP_DISPLAY("views/PathDisplayFXML.fxml"),
  PATIENT_TRANSPORT("views/PatientTransportationFXML.fxml"),
  IMPORT_CSV("views/ImportEmployeeCSVFXML.fxml"),
  EXPORT_CSV("views/ExportEmployeeCSVFXML.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
