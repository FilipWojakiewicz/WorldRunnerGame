package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.config.GameConfig;

public class Obstacle extends EntityBase{

    private float m_angleDeg;
    private final Rectangle m_sensor = new Rectangle();
    private float m_sensorAngleDeg;


    public Obstacle() {
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public float getAngleDeg() {
        return m_angleDeg;
    }

    public Rectangle getSensor() {
        return m_sensor;
    }

    public float getSensorAngleDeg() {
        return m_sensorAngleDeg;
    }

    public void setAngleDeg(float value){
        m_angleDeg = value % 360;
        m_sensorAngleDeg = m_angleDeg + 30f;

        float radius = GameConfig.PLANET_HALF_SIZE - 0.1f;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-m_angleDeg) * radius;
        float newY = originY + MathUtils.sinDeg(-m_angleDeg) * radius;

        setPosition(newX, newY);

        float sensorX = originX + MathUtils.cosDeg(-m_sensorAngleDeg) * radius;
        float sensorY = originY + MathUtils.sinDeg(-m_sensorAngleDeg) * radius;

        m_sensor.set(sensorX, sensorY, getWidth(), getHeight());
    }

    @Override
    public void reset() { }
}
