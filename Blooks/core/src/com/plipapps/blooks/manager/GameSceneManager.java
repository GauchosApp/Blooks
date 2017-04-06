package com.plipapps.blooks.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.plipapps.blooks.Blooks;
import com.plipapps.blooks.component.Bounds;
import com.plipapps.blooks.component.Cuadrado;

import java.util.ArrayList;

/**
 * Created by mariano on 05/04/2017.
 */
public class GameSceneManager extends BaseSceneManager {
    private ArrayList<Cuadrado> cuadrados;
    private ComponentMapper<Variables> variables;
    private Bounds back, undo, restart, share, soundOn, soundOff, rate;
    private int nivel;
    private com.plipapps.blooks.SoundController soundController;
    private Entity soundOnEntity, soundOffEntity;

    public GameSceneManager(Blooks game, int nivel) {
        super(game);
        this.nivel = nivel;
        soundController = game.getSoundController();
    }

    @Override
    public void afterSceneInit() {
        super.afterSceneInit();
        cuadrados = new ArrayList<Cuadrado>();
           for (int i = 1; i <= 4; i++){
            getCuadrados().add(new Cuadrado(i, getSpriteBounds("c"+i), idManager.get("c"+i)));
        }

        back = getSpriteBounds("back");
        undo = getSpriteBounds("undo");
        restart = getSpriteBounds("restart");
        share = getSpriteBounds("share");
        soundOnEntity = idManager.get("soundOn");
        soundOffEntity = idManager.get("soundOff");
        rate = getSpriteBounds("rate");
        if (!soundController.isEnabled()){
            swapSpritesPosition(soundOnEntity, soundOffEntity);
        }
        soundOn = boundsCm.get(soundOnEntity);
        soundOff = boundsCm.get(soundOffEntity);
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        unprojectVec.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(unprojectVec);
        float x = unprojectVec.x;
        float y = unprojectVec.y;
        for (int i = 0; i < getCuadrados().size(); i++){
            if (getCuadrados().get(i).getBounds().contains(x, y)){
                if (variables.get(cuadrados.get(i).getEntity()).getInt("visibility") == 0) {
                    System.out.println("toco");
                    getCuadrados().get(i).setSeleccionado(true);
                    // sprite = spriteCm.get(cuadrados.get(i).getEntity());
                    variables.get(cuadrados.get(i).getEntity()).putInt("selected", 1);
                }
            }
        }
        if (back.contains(x, y)){
            game.loadLevelScene();
        }
        if (undo.contains(x, y)){
            System.out.println("undo");
        }
        if (restart.contains(x, y)){
           game.loadGameScene(nivel);
        }
        if (share.contains(x, y)){
            System.out.println("share");
        }
        if (soundOn.contains(x, y)) {
            soundController.setSoundEnabled(false);
            swapSpritesPosition(soundOnEntity, soundOffEntity);
        } else if (soundOff.contains(x, y)) {
            soundController.setSoundEnabled(true);
            swapSpritesPosition(soundOffEntity, soundOnEntity);
        }
        if (rate.contains(x, y)){
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=ar.m2wapps.shark_music");
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

    public ArrayList<Cuadrado> getCuadrados() {
        return cuadrados;
    }
}
