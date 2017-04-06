package com.plipapps.blooks.manager;

/**
 * Created by mariano on 30/03/2017.
 */

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.component.Transform;
import com.plipapps.blooks.Blooks;
import com.plipapps.blooks.component.Bounds;

import java.util.ArrayList;

/** @author Kotcrab */
public class LevelSceneManager extends BaseSceneManager {
    private Bounds backSprite, rateSprite;
    private Entity soundOnEntity;
    private Entity soundOffEntity;
    private Bounds soundOnBounds;
    private Bounds soundOffBounds;
    private ArrayList<Bounds> nivelesSprites;
    private int cantLevels = 16, levelCount = 1;
    private com.plipapps.blooks.SoundController soundController;

    public LevelSceneManager(Blooks game) {
        super(game);
        soundController = game.getSoundController();
    }

    @Override
    public void afterSceneInit () {
        super.afterSceneInit();
        nivelesSprites = new ArrayList<Bounds>();
        for (int i = 1; i <= cantLevels; i++){
            nivelesSprites.add(getSpriteBounds(""+i));
        }
        backSprite = getSpriteBounds("back");
        rateSprite = getSpriteBounds("rate");
        soundOnEntity = idManager.get("soundOn");
        soundOffEntity = idManager.get("soundOff");
        if (!soundController.isEnabled()){
            swapSpritesPosition(soundOnEntity, soundOffEntity);
        }
        soundOnBounds = boundsCm.get(soundOnEntity);
        soundOffBounds = boundsCm.get(soundOffEntity);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        unprojectVec.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(unprojectVec);

        float x = unprojectVec.x;
        float y = unprojectVec.y;

        if (backSprite.contains(x, y)) {
            soundController.playClick();
            game.loadMenuScene();
        }
        if (rateSprite.contains(x, y)){
            soundController.playClick();
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=ar.m2wapps.shark_music");
        }
        if (soundOnBounds.contains(x, y)) {
            soundController.setSoundEnabled(false);
            swapSpritesPosition(soundOnEntity, soundOffEntity);
        } else if (soundOffBounds.contains(x, y)) {
            soundController.setSoundEnabled(true);
            swapSpritesPosition(soundOffEntity, soundOnEntity);
        }
        for (int i = 1; i <= nivelesSprites.size(); i++){
            if (nivelesSprites.get(i-1).contains(x, y)){
                if (i <= levelCount) {
                    game.loadGameScene(i);
                }
            }
        }
        return false;
    }

    public void swapSpritesPosition (Entity entity1, Entity entity2) {
        Transform transform1 = transformCm.get(entity1);
        Transform transform2 = transformCm.get(entity2);

        float xPos = transform1.getX(), yPos = transform1.getY();
        transform1.setPosition(transform2.getX(), transform2.getY());
        transform2.setPosition(xPos, yPos);
    }
}
