package com.plipapps.blooks.component;

import com.artemis.Entity;

/**
 * Created by mariano on 05/04/2017.
 */
public class Cuadrado {
    private final Entity entity;
    private Bounds bounds;
    private boolean seleccionado;
    private int id;
    public Cuadrado(int id, Bounds bounds, Entity entity){
        this.id = id;
        this.bounds = bounds;
        this.entity = entity;
    }
    public Cuadrado(int id, Entity entity){
        this.id = id;
        this.entity = entity;
    }
    public Bounds getBounds() {
        return bounds;
    }
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public int getId() {
        return id;
    }

    public Entity getEntity() {
        return entity;
    }
}
