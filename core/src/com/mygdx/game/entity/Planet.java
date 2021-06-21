package com.mygdx.game.entity;

import com.badlogic.gdx.math.Circle;
import com.mygdx.game.config.GameConfig;

public class Planet {

    private float x;
    private float y;
    private float width = 1;
    private float height = 1;
    private Circle bounds;

    public Planet() {
        bounds = new Circle(x, y, GameConfig.PLANET_HALF_SIZE);
        setSize(GameConfig.PLANET_SIZE, GameConfig.PLANET_SIZE);
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

    public Circle getBounds() {
        return bounds;
    }

    public void updateBounds() {
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;
        bounds.setPosition(x + halfWidth, y + halfHeight);
    }
}
