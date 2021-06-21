package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.config.GameConfig;

public class Shot extends EntityBase {

    private float m_angleDeg;
    private float m_angleDegSpeed = GameConfig.SHOT_START_ANG_SPEED;
    private float m_shotTimer;

    public float getAngleDeg() {
        return m_angleDeg;
    }

    public float getShotTimer() {
        return m_shotTimer;
    }

    public void setShotTimer(float shotTimer) {
        m_shotTimer = shotTimer;
    }

    public Shot() {
        setSize(GameConfig.SHOT_SIZE, GameConfig.SHOT_SIZE);
        m_shotTimer = 1f;
    }

    public void setAngleDeg(float value){
        m_angleDeg = value % 360;

        float radius = GameConfig.PLANET_HALF_SIZE + 0.25f;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-m_angleDeg) * radius;
        float newY = originY + MathUtils.sinDeg(-m_angleDeg) * radius;

        setPosition(newX, newY);
    }

    public void update(float delta){

        m_shotTimer -= delta;

        m_angleDeg += m_angleDegSpeed * delta;
        m_angleDeg = m_angleDeg % 360;

        float radius = GameConfig.PLANET_HALF_SIZE + 0.25f;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-m_angleDeg) * radius;
        float newY = originY + MathUtils.sinDeg(-m_angleDeg) * radius;

        setPosition(newX, newY);
    }

    @Override
    public void reset() { }
}
