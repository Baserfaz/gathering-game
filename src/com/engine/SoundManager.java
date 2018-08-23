package com.engine;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enumerations.SoundEffect;
import com.gameobjects.GameObject;
import com.utilities.Mathf;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundManager {
      
      private Map<SoundEffect, Sound> sounds;
      
      private float soundVolume = 0.1f;
      
      public SoundManager() {
          TinySound.init();
          TinySound.setGlobalVolume(this.soundVolume);
          this.sounds = new HashMap<SoundEffect, Sound>();
          this.loadSounds();
      }
    
      private void loadSounds() {
          int soundCount = 0;
          int errorCount = 0;
          
          List<SoundEffect> errorEffects = new ArrayList<SoundEffect>();
          
          for(SoundEffect effect : SoundEffect.values()) {
              String path = this.getPath(effect);
              URL soundURL = this.getClass().getResource(path);
              Sound sound = TinySound.loadSound(soundURL);
              
              if(sound != null) this.sounds.put(effect, sound);
              else {
                  errorCount += 1;
                  errorEffects.add(effect);
              }
              
              soundCount += 1;
          }
          
          System.out.println("Loaded " + (soundCount - errorCount) + "/" + soundCount + " sound effects.");
          
          if(errorEffects.isEmpty() == false) {
              System.out.print("These sound effects failed to load:");
              for(SoundEffect e : errorEffects) { System.out.print(" " + e.toString()); }
              System.out.print("\n");
          } else {
              System.out.println("All sound effects succesfully loaded!");
          }
          
      }
      
      // player's sounds and GUI sounds should use playSound
      // and other sounds playSoundWithPan.
      public void playSound(SoundEffect effect) {
          
          Sound s = this.sounds.get(effect);
          if(s != null) {
              s.play();
          } else {
              System.out.println("Could not play sound: " + effect.toString());
          }
      }
      
      public void playSoundWithPan(SoundEffect effect, GameObject target) {
          
          // 1. calculate if the target is in range.
          // 2. calculate the position of the target (right/left) from the player
          // 3. calculate the volume of the sound effect using distance.
          
          Point camPos = Game.instance.getCamera().getCameraCenterPosition();
          Point targetPos = target.getCenterPoint();
          
          double distance = camPos.distance(targetPos.x, targetPos.y);
          double pan = 0.0;
          
          if(distance < 100.0) {
          
              // right or left
              pan = (targetPos.x > camPos.x) ? 1 : -1;
              
              // Convert 0.0 to enemy_activ_range -> 0.0 to 1.0
              // distance is a point on this range.
              // https://stackoverflow.com/questions/5731863/mapping-a-numeric-range-onto-another
              double output = 0.0 + ((1.0 - 0.0) / (100.0 - 0.0)) * (distance - 0.0);
              output = Mathf.round(1.0 - output, 2);
              
              // the closer the target is to player
              // the higher the volume and nearer the sound.
              this.sounds.get(effect).play(1.0 * output, pan * output);
          }
      }
      
      private String getPath(SoundEffect effect) {
        
          String path = "";
          
          switch(effect) {
          case SELECT:
              path = "/sounds/select.wav";
              break;
          case HOVER:
              path = "/sounds/hover.wav";
              break;
          default:
              System.out.println("SoundManager:: play: unsupported sound effect!");
              break;
          }
          return path;
      }
}
