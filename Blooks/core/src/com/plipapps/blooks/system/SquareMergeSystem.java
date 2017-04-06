package com.plipapps.blooks.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.plipapps.blooks.component.Cuadrado;
import com.plipapps.blooks.component.Merge;

import java.util.ArrayList;

/**
 * Created by mariano on 05/04/2017.
 */
public class SquareMergeSystem extends EntityProcessingSystem implements AfterSceneInit {
    private ComponentMapper<Variables> variables;
    protected ComponentMapper<Transform> transformCm;
    RenderBatchingSystem renderSystme;
   // Variables variable;
    ComponentMapper<VisSprite> spriteCm;
    private ArrayList<Cuadrado> cuadrados;
    private ArrayList<Cuadrado> posibilidades;
    private Entity invisible;
    private int targetLayerId;
 //   private VisSprite sprite;
    private int selected, posicion = -1;
    private boolean touch1, touch2;
    VisIDManager idManager;
    ArrayList<VisSprite> mergeTemplate;

    public SquareMergeSystem () {
        super(Aspect.all(VisSprite.class, Merge.class));
    }
    @Override
    public void afterSceneInit() {
        cuadrados = new ArrayList<Cuadrado>();
        posibilidades = new ArrayList<Cuadrado>();
        mergeTemplate = new ArrayList<VisSprite>();
        for (int i = 1; i <= 7; i++){
            if (i <= 4) {
                cuadrados.add(new Cuadrado(i, idManager.get("c" + i)));
            }
            posibilidades.add(new Cuadrado(i, idManager.get("p" + i)));
            mergeTemplate.add(spriteCm.get(idManager.get("p" + i)));
        }
        invisible = idManager.get("aux");
    }

    @Override
    protected void begin() {
        super.begin();
        for (int i = 0; i < cuadrados.size(); i++) {
          //  sprite = spriteCm.get(c.getEntity());
            selected = variables.get(cuadrados.get(i).getEntity()).getInt("selected");
            if(selected == 1){
                if(!touch1 && posicion == -1) {
                    System.out.println("posicion");
                    posicion = i;
                    touch1 = true;
                }else if (!touch2 && posicion != i){
                        touch2 = true;
                        System.out.println("union");
                        if (cuadrados.get(posicion).getId() > cuadrados.get(i).getId()) {
                            if (cuadrados.get(posicion).getId() == 2 && cuadrados.get(i).getId() == 1) {
                                swapSpritesPosition(cuadrados.get(posicion).getEntity(),0);
                            }else  if (cuadrados.get(posicion).getId() == 4 && cuadrados.get(i).getId() == 1) {
                                swapSpritesPosition(cuadrados.get(posicion).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 3 && cuadrados.get(i).getId() == 2) {
                                swapSpritesPosition(cuadrados.get(posicion).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 4 && cuadrados.get(i).getId() == 3) {
                                swapSpritesPosition(cuadrados.get(posicion).getEntity(),0);
                            }
                        }else {
                            if (cuadrados.get(posicion).getId() == 1 && cuadrados.get(i).getId() == 2) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),0);
                            }else  if (cuadrados.get(posicion).getId() == 1 && cuadrados.get(i).getId() == 4) {
                                System.out.println("uno con cuatro");
                                swapSpritesPosition(cuadrados.get(i).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 2 && cuadrados.get(i).getId() == 3) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 3 && cuadrados.get(i).getId() == 4) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),0);
                            }
                        }
                        setInvisible(invisible,cuadrados.get(i).getEntity(),cuadrados.get(posicion).getEntity());
                        variables.get(cuadrados.get(posicion).getEntity()).putInt("selected", 0);
                        variables.get(cuadrados.get(i).getEntity()).putInt("selected", 0);
                        touch1 = false;
                        touch2 = false;
                        posicion = -1;
                        break;
                }
            }
        }

    }
    public void swapSpritesPosition (Entity entity2, int i) {
        Transform transform2 = transformCm.get(entity2);
        Transform platformTransform = new Transform();

        world.createEntity().edit()
                .add(new Renderable(10))
                .add(new Layer(targetLayerId))
                .add(new VisSprite(mergeTemplate.get(i)))
                .add(platformTransform)
                .add(new Origin())
                .add(new Merge());

        platformTransform.setPosition(transform2.getX(),transform2.getY());

        renderSystme.markDirty();
    }
    private void setInvisible(Entity entity1, Entity entity2, Entity entity3){
        Transform transform1 = transformCm.get(entity1);
        Transform transform2 = transformCm.get(entity2);
        Transform transform3 = transformCm.get(entity3);
        variables.get(entity2).putInt("visibility", 1);
        variables.get(entity3).putInt("visibility", 1);
        float xPos = transform1.getX(), yPos = transform1.getY();
        transform2.setPosition(xPos, yPos);
        transform3.setPosition(xPos, yPos);
    }
    @Override
    protected void process(Entity e) {

    }


}
