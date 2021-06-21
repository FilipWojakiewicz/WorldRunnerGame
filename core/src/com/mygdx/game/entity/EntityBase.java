package com.mygdx.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public abstract class EntityBase implements Pool.Poolable {

    protected float x;
    protected float y;
    protected float width = 1;
    protected float height = 1;
    protected Rectangle bounds;

    public EntityBase() {
        bounds = new Rectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
        bounds.setSize(width, height);
    }
}
