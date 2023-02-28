package edu.wpi.cs3733.C23.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/HomeFXML.fxml"),
  HOME_ACTUAL("views/FinalHomeFXML.fxml"),
  HOME_MAINTENANCE("views/NewHomeMaintenanceFXML.fxml"),
  HOME_ADMIN("views/HomeAdminFXML.fxml"),
  HOME_EMPLOYEE("views/HomeEmployeeFXML.fxml"),
  CONFIRMATION("views/serviceRequests/ConfirmationFXML.fxml"),
  COMPUTER("views/serviceRequests/ComputerFXML.fxml"),
  SANITATION("views/serviceRequests/SanitationFXML.fxml"),
  SECURITY("views/serviceRequests/SecurityFXML.fxml"),
  AUDIOVISUAL("views/serviceRequests/AudioVisualFXML.fxml"),
  ACCESSIBILITY("views/serviceRequests/AccessibilityFXML.fxml"),
  HELP("views/HelpFXML.fxml"),
  HOME_SERVICE_REQUEST("views/HomeServiceRequestSMALLFXML.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  MOVE("views/InteractiveMoveFXML.fxml"),
  NODE("views/NodeSMALLFXML.fxml"),
  EDGE("views/[OLD_FXMLS]/EdgeFXML.fxml"),
  PATHFINDING("views/pathfinding/PathfindingNewFXML.fxml"),
  SERVICE_REQUEST_STATUS("views/ServiceRequestStatusSMALLFXML.fxml"),
  LOGIN("views/LoginFXML.fxml"),
  NODE_MAP("views/MapEditorFXML.fxml"),
  EMPLOYEE("views/EmployeeFXML.fxml"),
  HOME_DATABASE("views/HomeDatabaseFXML.fxml"),
  PATIENT_TRANSPORT("views/serviceRequests/PatientTransportationFXML.fxml"),
  IMPORT_CSV("views/ImportCSVFXML.fxml"),
  EXPORT_CSV("views/ExportCSVFXML.fxml"),
  KIOSK_SETUP("views/KioskSetupFXML.fxml"),
  KIOSK("views/KioskFXML.fxml"),
  MESSAGE_INBOX("views/MessagesInboxFXML.fxml"),
  SRCHART("views/SRChartFXML.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
