package com.plipapps.blooks.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.plipapps.blooks.component.Animacion;

/**
 * Created by mariano on 06/04/2017.
 */
public class AnimationSelectedSystem extends EntityProcessingSystem implements AfterSceneInit {

    /**
     * Creates a new EntityProcessingSystem.
     *
     * @param aspect the aspect to match entities
     */
    private Entity animacion;
    VisIDManager idManager;
    private ComponentMapper<Variables> variables;
    protected ComponentMapper<Transform> transformCm;
    private  Transform transform;
    private float constante = 0.01f;
    public AnimationSelectedSystem() {
        super(Aspect.all(VisSprite.class, Animacion.class));
    }

    @Override
    public void afterSceneInit() {
        animacion = idManager.get("click0");
       transform = transformCm.get(animacion);
    }

    @Override
    protected void begin() {
        super.begin();
        if (variables.get(animacion).getInt("selected") == 1){
            if (transform.getScaleX() >= 1.09f){
                constante = -0.005f;
            }
            else if (transform.getScaleX() <= 1.0f){
                constante = 0.005f;
            }
             float x = transform.getScaleX() + constante;
             float y = transform.getScaleY() + constante;
             transform.setScale(x, y);
        }
    }

    @Override
    protected void process(Entity e) {

    }
}
