package edu.wpi.cs3733.C23.teamA.controllers;

import edu.wpi.cs3733.C23.teamA.AApp;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AboutController {
  public Text FunFact;
  public Text Role;
  public Text name;
  public Text major;
  public Text memberA;
  public Text memberB;
  public Text memberC;
  public Text memberD;
  public Text memberE;
  public Text memberF;
  public Text memberG;
  public Text memberH;
  public Text memberI;
  public Text memberJ;
  @FXML ImageView ImageOne;

  private final Image ImageA =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_A.JPG"));
  private final Image ImageB =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_B.jpg"));

  private final Image ImageC =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_C.jpg"));

  private final Image ImageD =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_D.jpg"));

  private final Image ImageE =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_E.jpeg"));
  private final Image ImageF =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_F.jpg"));
  private final Image ImageG =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_G.jpg"));

  private final Image ImageH =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_H.jpg"));

  private final Image ImageI =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_I.jpg"));

  private final Image ImageJ =
      new Image(AApp.class.getResourceAsStream("assets/TeamPhotos/IMG_J.jpg"));

  @FXML
  public void initialize() throws IOException, InterruptedException, SQLException {
    memberA.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageA);
          name.setText(memberA.getText());
          FunFact.setText("Fun Fact: I can walk across a room on only my hands.");
          Role.setText("Role: Lead Software Developer");
          major.setText("Major: Computer Science and Math");
        });
    memberB.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageB);
          name.setText(memberB.getText());
          FunFact.setText("Fun Fact: I can speak Russian.");
          Role.setText("Role: Assistant Lead Software Developer (Back-End)");
          major.setText("Major: Computer Science");
        });
    memberC.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageC);
          name.setText(memberC.getText());
          FunFact.setText("Fun Fact: I am the first American citizen in my entire bloodline.");
          Role.setText("Role: Assistant Lead Software Developer (Front-End)");
          major.setText("Major: Computer Science and Robotics Engineering");
        });
    memberD.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageD);
          name.setText(memberD.getText());
          FunFact.setText("Fun Fact: I am traveling to Armenia this spring.");
          Role.setText("Role: Database Engineer (Back-End)");
          major.setText("Major: Computer Science");
        });
    memberE.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageE);
          name.setText(memberE.getText());
          FunFact.setText("Fun Fact: I am related to Bill Nye.");
          Role.setText("Role: Database Engineer (Back-End)");
          major.setText("Major: Computer Science");
        });
    memberF.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageF);
          name.setText(memberF.getText());
          FunFact.setText("Fun Fact: I played percussion for 8 years.");
          Role.setText("Role: Full-Time Developer (Front-End)");
          major.setText("Major: Computer Science and IMGD Minor");
        });
    memberG.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageG);
          name.setText(memberG.getText());
          FunFact.setText("Fun Fact: I have tri-citizenship in Italy, Australia, and America.");
          Role.setText("Role: Project Manager, Part-Time Developer (Front-End)");
          major.setText("Major: Robotics Engineering");
        });
    memberH.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageH);
          name.setText(memberH.getText());
          FunFact.setText("Fun Fact: I played Violin for 12 years.");
          Role.setText("Role: Scrum Master, Part-Time Developer (Front-End)");
          major.setText("Major: Robotics Engineering and Mechanical Engineering");
        });
    memberI.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageI);
          name.setText(memberI.getText());
          FunFact.setText("Fun Fact: I have flown an airplane.");
          Role.setText("Role: Product Owner, Part-Time Developer (Front-End)");
          major.setText("Major: Mechanical Engineering and Robotics Engineering");
        });
    memberJ.setOnMouseClicked(
        (MouseEvent e) -> {
          ImageOne.setImage(ImageJ);
          name.setText(memberJ.getText());
          FunFact.setText("Fun Fact: I was voted most likely to publish a game at my high school.");
          Role.setText("Role: Documentation Analyst, Part-Time Developer (Back-End)");
          major.setText("Major: IMGD-BS and Computer Science");
        });
  }
}
