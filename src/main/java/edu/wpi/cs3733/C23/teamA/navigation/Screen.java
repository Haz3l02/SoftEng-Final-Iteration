package edu.wpi.cs3733.C23.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/HomeFXML.fxml"),
  HOME_MAINTENANCE("views/HomeMaintenanceFXML.fxml"),
  HOME_ADMIN("views/HomeAdminFXML.fxml"),
  HOME_EMPLOYEE("views/HomeEmployeeFXML.fxml"),
  COMPUTER_CONFIRMATION("views/ComputerConfirmationFXML.fxml"),
  SANITATION_CONFIRMATION("views/SanitationConfirmationFXML.fxml"),
  SECURITY_CONFIRMATION("views/SecurityConfirmationFXML.fxml"),
  COMPUTER("views/ComputerFXML.fxml"),
  SANITATION("views/SanitationFXML.fxml"),
  SECURITY("views/SecurityFXML.fxml"),
  HELP("views/HelpFXML.fxml"),
  HOME_SERVICE_REQUEST("views/HomeServiceRequestSMALLFXML.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  DATABASE("views/DatabaseFXML.fxml"),
  NODE("views/NodeFXML.fxml"),
  EDGE("views/EdgeFXML.fxml"),
  PATHFINDING("views/PathfindingNewFXML.fxml"),
  PATHFINDING_MAP("views/PathfindingMapFXML.fxml"),
  SERVICE_REQUEST_STATUS("views/ServiceRequestStatusSMALLFXML.fxml"),
  ID_INPUT("views/IDInputFXML.fxml"),
  LOGIN("views/LoginFXML.fxml"),
  NODE_MAP("views/NodeMapFXML.fxml"),
  HOME_DATABASE("views/HomeDatabaseSMALLFXML.fxml"),
  MAP_DISPLAY("views/PathDisplayFXML.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
