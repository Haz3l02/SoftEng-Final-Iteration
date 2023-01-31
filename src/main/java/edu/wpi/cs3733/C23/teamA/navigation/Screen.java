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
  PATHFINDING("views/PathfindingFXML.fxml"),
  SERVICE_REQUEST_STATUS("views/ServiceRequestStatusNewFXML.fxml"),
  ID_INPUT("views/IDInputFXML.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
