package com.plipapps.blooks.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.plipapps.blooks.component.Lock;

import java.util.ArrayList;

/**
 * Created by mariano on 04/04/2017.
 */
public class LockSpawnerSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;

    RenderBatchingSystem renderSystme;
    CameraManager cameraManager;
    VisIDManager idManager;

    VisSprite lockTemplate;
    private ArrayList<Entity> niveles;


    VisSprite playerSprite;

    float lastPlatformY = 1f;
    int levelCount = 1, levelMin, cantLevels = 16;

    private int targetLayerId;

    public LockSpawnerSystem () {
        super(Aspect.all(VisSprite.class, Lock.class));
    }

    @Override
    public void afterSceneInit () {
        lockTemplate = spriteCm.get(idManager.get("lock"));

        niveles = new ArrayList<Entity>();
        for (int i = 1; i <= cantLevels; i++){
            niveles.add(idManager.get(""+i));
        }
     //   playerSprite = spriteCm.get(idManager.get("player"));

       // idManager.get("floor").edit().add(new Lock());
    }

    @Override
    protected void begin () {
        if (levelCount < cantLevels && lockTemplate != null) {
            Transform platformTransform = new Transform();

            world.createEntity().edit()
                    .add(new Renderable(10))
                    .add(new Layer(targetLayerId))
                    .add(new VisSprite(lockTemplate))
                    .add(platformTransform)
                    .add(new Origin())
                    .add(new Lock());

            Transform transform1 = transformCm.get(niveles.get(levelCount));

            platformTransform.setPosition(transform1.getX(),transform1.getY());

            renderSystme.markDirty();

          //  lastPlatformY += 1.2f;
            levelCount++;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void process (Entity e) {
        //delete platforms outside screen
     //   Transform platformTransform = transformCm.get(e);
      //  if (platformTransform.getY() < cameraManager.getCamera().position.y - 2.6f) {
          //  platformCount--;
     //       e.deleteFromWorld();
     //   }
    }

    public void setTargetLayerId (int targetLayerId) {
        this.targetLayerId = targetLayerId;
    }
}
