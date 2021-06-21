package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.config.GameConfig;

public class Coin extends EntityBase implements Pool.Poolable {

    private float m_angleDeg;

    public Coin() {
        setSize(GameConfig.COIN_SIZE, GameConfig.COIN_SIZE);
    }

    public float getAngleDeg() {
        return m_angleDeg;
    }

    public void setAngleDeg(float value){
        m_angleDeg = value % 360;

        float radius = GameConfig.PLANET_HALF_SIZE;

        radius += GameConfig.COIN_SIZE * GameConfig.COIN_SPAWN_RADIUS;

        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-m_angleDeg) * radius;
        float newY = originY + MathUtils.sinDeg(-m_angleDeg) * radius;

        setPosition(newX, newY);
    }

    @Override
    public void reset() { }
}
