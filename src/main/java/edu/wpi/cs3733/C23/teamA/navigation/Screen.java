package edu.wpi.cs3733.C23.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/HomeFXML.fxml"),
  COMPUTER_CONFIRMATION("views/ComputerConfirmationFXML.fxml"),
  SANITATION_CONFIRMATION("views/SanitationConfirmationFXML.fxml"),
  SECURITY_CONFIRMATION("views/SecurityConfirmationFXML.fxml"),
  COMPUTER("views/ComputerFXML.fxml"),
  SANITATION("views/SanitationFXML.fxml"),
  SECURITY("views/SecurityFXML.fxml"),
  HELP("views/HelpFXML.fxml"),
  HOME_SERVICE_REQUEST("views/HomeServiceRequestFXML.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  DATABASE("views/DatabaseFXML.fxml"),
  NODE("views/NodeFXML.fxml"),
  EDGE("views/EdgeFXML.fxml"),
  PATHFINDING("views/PathfindingFXML.fxml"),
  PATHFINDING_MAP("views/PathfindingMapFXML.fxml"),
  SERVICE_REQUEST_STATUS("views/ServiceRequestStatusNewFXML.fxml"),
  ID_INPUT("views/IDInputFXML.fxml"),
<<<<<<< HEAD
  LOGIN("views/LoginFXML.fxml");
=======
  NODE_MAP("views/NodeMapFXML.fxml"),
  MAP_DISPLAY("views/PathDisplayFXML.fxml");
>>>>>>> 961e483c4bc70f6c6c0c7d4e10d00d74abc8a8bc

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
