package edu.wpi.cs3733.C23.teamA.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import lombok.Getter;
import lombok.Setter;

public class TextReader implements Runnable {

  @Getter @Setter private String text;
  private Voice voice;
  private Thread ttsThread;

  public TextReader(String text) {
    // set the text field to whatever is going to be read later
    this.text = text;

    // set system property - used to get the voice
    System.setProperty(
        "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

    // make a voice (kevin16)
    voice = VoiceManager.getInstance().getVoice("kevin16");

    // make a new thread (NOT starting it)
    ttsThread = new Thread(this);
  }

  public void readText() {
    ttsThread.start();
  }

  public void stopText(){
    ttsThread.interrupt();
  }

  public void forceStop() {
    // TODO: I want this to stop the .speak() call inside of the thread. the call below *should* do that, but doesn't
    voice.getAudioPlayer().cancel();
  }

  @Override
  // Reads the string in the text field of the current TextReader object.
  public void run() {
    if (voice != null) {
      // allocate the voice
      voice.allocate();

      // print out some info about it
      // System.out.println("Voice Rate: " + voice.getRate());
      // System.out.println("Voice Pitch: " + voice.getPitch());
      // System.out.println("Voice Volume: " + voice.getVolume());

      // speak the text associated with the object
      boolean status = voice.speak(this.text);
      // System.out.println("Status: " + status);

      // deallocate the voice
      voice.deallocate();
    }
    // if the voice is null, the requested voice (kevin16) couldn't be retrieved; print info to
    // console
    else {
      System.out.println("ERROR: The voice specified could not be retrieved.");
    }
  }
}
