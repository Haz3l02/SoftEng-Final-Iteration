package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.pathfinding.enums.Floor;
import edu.wpi.cs3733.C23.teamA.pathfinding.enums.HospitalMaps;
import javafx.scene.image.Image;

public class ImageLoader {

  private static Image[] images = new Image[5];

  public void loadImages() {
    // images in the jar ψ(｀∇´)ψ
    images[0] = new Image(this.getClass().getResourceAsStream(HospitalMaps.L1.getFilename()));
    images[1] = new Image(this.getClass().getResourceAsStream(HospitalMaps.L2.getFilename()));
    images[2] = new Image(this.getClass().getResourceAsStream(HospitalMaps.F1.getFilename()));
    images[3] = new Image(this.getClass().getResourceAsStream(HospitalMaps.F2.getFilename()));
    images[4] = new Image(this.getClass().getResourceAsStream(HospitalMaps.F3.getFilename()));

    // old version of stuff
    //    File fileL1 = new File(HospitalMaps.L1.getFilename());
    //    images[0] = new Image(fileL1.toURI().toString());
    //    File fileL2 = new File(HospitalMaps.L2.getFilename());
    //    images[1] = new Image(fileL2.toURI().toString());
    //    File fileF1 = new File(HospitalMaps.F1.getFilename());
    //    images[2] = new Image(fileF1.toURI().toString());
    //    File fileF2 = new File(HospitalMaps.F2.getFilename());
    //    images[3] = new Image(fileF2.toURI().toString());
    //    File fileF3 = new File(HospitalMaps.F3.getFilename());
    //    images[4] = new Image(fileF3.toURI().toString());
  }

  public static Image getImage(String floor) {
    int index = Floor.indexFromTableString(floor);
    if (index != -1) {
      return images[index];
    }
    return null;
  }
}
