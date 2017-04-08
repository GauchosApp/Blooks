package com.plipapps.blooks.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.assets.SpriterAsset;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.kotcrab.vis.runtime.util.SpriterData;
import com.plipapps.blooks.component.Cuadrado;
import com.plipapps.blooks.component.Merge;
import sun.audio.AudioPlayer;

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
    private VisAssetManager mVisAssetManager;
    private Entity animacion;
    private Transform animacionTransform;

    public SquareMergeSystem () {
        super(Aspect.all(VisSprite.class, Merge.class));
    }
    @Override
    public void afterSceneInit() {
        cuadrados = new ArrayList<Cuadrado>();
        posibilidades = new ArrayList<Cuadrado>();
        mergeTemplate = new ArrayList<VisSprite>();
        animacion = idManager.get("click0");
        animacionTransform = transformCm.get(animacion);
        for (int i = 1; i <= 7; i++){
            if (i <= 4) {
                cuadrados.add(new Cuadrado(i, idManager.get("c" + i)));
            }
            posibilidades.add(new Cuadrado(i, idManager.get("p"+ i)));
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
                   // setAnimacion(cuadrados.get(i).getEntity());

                    Transform transform2 = transformCm.get(cuadrados.get(i).getEntity());
                    float x =transform2.getX()-0.3f;
                    float y=transform2.getY()-0.3f;
                    animacionTransform .setPosition(x,y);
                    posicion = i;
                    touch1 = true;
                }else if (!touch2 && posicion != i){
                        touch2 = true;
                        System.out.println("union");
                        if (cuadrados.get(posicion).getId() > cuadrados.get(i).getId()) {
                            if (cuadrados.get(posicion).getId() == 2 && cuadrados.get(i).getId() == 1) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),0);
                            }else  if (cuadrados.get(posicion).getId() == 4 && cuadrados.get(i).getId() == 1) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 3 && cuadrados.get(i).getId() == 2) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),1);
                            }else if (cuadrados.get(posicion).getId() == 4 && cuadrados.get(i).getId() == 3) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),0);
                            }
                        }else {
                            if (cuadrados.get(posicion).getId() == 1 && cuadrados.get(i).getId() == 2) {
                                swapSpritesPosition(cuadrados.get(i).getEntity(),0);
                            }else  if (cuadrados.get(posicion).getId() == 1 && cuadrados.get(i).getId() == 4) {
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
                        variables.get(animacion).putInt("selected",0);
                        animacionTransform.setPosition(12f,7f);
                        break;
                }
            }
        }
        if(variables.get(animacion).getInt("selected") == 0){
            if (animacionTransform.getX() < 12f){
                animacionTransform.setPosition(12f,7f);
                for (Cuadrado c : cuadrados){
                    variables.get(c.getEntity()).putInt("selected", 0);
                }
                posicion = -1;
                touch1 = false;
                touch2 = false;
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
  

        int aux = variables.get(posibilidades.get(i).getEntity()).getInt("rotation");
        if (aux > 0) {
            platformTransform.setRotation(aux);
            platformTransform.setPosition(transform2.getX()+mergeTemplate.get(0).getWidth(), transform2.getY());
        }else if (aux < 0){
            platformTransform.setRotation(aux);
            platformTransform.setPosition(transform2.getX()-mergeTemplate.get(0).getWidth(), transform2.getY()+mergeTemplate.get(0).getWidth());
        }else {
            platformTransform.setPosition(transform2.getX(), transform2.getY());
        }

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
    private void setAnimacion(Entity entity){
        Transform transform2 = transformCm.get(entity);
        SpriterData spriterData = mVisAssetManager.get("spriter/Animacion/Animacion.scml");
        VisSpriter visSpriter = new VisSpriter(spriterData.loader, spriterData.data, transform2.getScaleX());
        Transform platformTransform = new Transform();
        world.createEntity().edit()
                .add(new Renderable(10))
                .add(new Layer(0))
                .add(platformTransform) //this is new in 0.3.x
                .add(new AssetReference(new SpriterAsset("spriter/Animacion/Animacion.scml", 1)))
                .add(visSpriter);
        platformTransform.setPosition(transform2.getX(),transform2.getY());
    }
    @Override
    protected void process(Entity e) {

    }


}
