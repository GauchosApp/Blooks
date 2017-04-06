package com.plipapps.blooks;

/**
 * Created by mariano on 30/03/2017.
 */
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.kotcrab.vis.runtime.component.VisSound;
import com.kotcrab.vis.runtime.scene.VisAssetManager;

/** @author Kotcrab */
public class SoundController {
    private boolean enabled = true;
    Music music;
    Sound click;

    public SoundController (VisAssetManager manager) {
        //music is persisted across whole game, so we load it manually
        String musicPath = "music/music.mp3";
        String clickPath = "sound/click.wav";

        manager.load(musicPath, Music.class);
        manager.load(clickPath, Sound.class);

        manager.finishLoading();

        music = manager.get(musicPath, Music.class);
        click = manager.get(clickPath, Sound.class);

        if (isEnabled()) music.play();
    }

    public void playClick () {
        play(click);
    }

    public void play (VisSound component) {
        play(component.sound);
    }

    public void play (Sound sound) {
        if (isEnabled()) {
            sound.play();
        }
    }

    public void setSoundEnabled (boolean enabled) {
        this.setEnabled(enabled);

        if (enabled)
            music.play();
        else
            music.pause();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}