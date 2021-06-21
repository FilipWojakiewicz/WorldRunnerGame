package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.game.config.GameConfig;

public class Player extends EntityBase{

    private float m_angleDeg = GameConfig.START_ANGLE;
    private float m_angleDegSpeed = GameConfig.PLAYER_START_ANG_SPEED;
    private float m_speed = 0;
    private float m_acceleration = GameConfig.PLAYER_START_ACC;
    private PlayerState m_state = PlayerState.WALKING;

    private final Array<Shot> m_shots = new Array<>();
    private final Pool<Shot> m_shotPool = Pools.get(Shot.class, 10);

    public float getAngleDeg() {
        return m_angleDeg;
    }

    public Array<Shot> getShots() {
        return m_shots;
    }

    public Player() {
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);
    }

    public void jump(){
        m_state = PlayerState.JUMPING;
    }

    public void shoot(){
        spawnShot();
    }

    private void spawnShot(){
        if (m_shots.size > 0)
            return;

        Shot shot = m_shotPool.obtain();
        shot.setAngleDeg(this.m_angleDeg);
        m_shots.add(shot);
    }

    public boolean isWalking(){
        return m_state.isWalking();
    }


    public void update(float delta){
        updateShots(delta);

        if (m_state.isJumping()){
            m_speed += m_acceleration * delta;

            if (m_speed >= GameConfig.PLAYER_MAX_SPEED)
                fall();

        } else if (m_state.isFalling()){
            m_speed -= m_acceleration * delta;

            if (m_speed <= 0){
                m_speed = 0;
                walk();
            }
        }

        m_angleDeg += m_angleDegSpeed * delta;
        m_angleDeg = m_angleDeg % 360;

        float radius = GameConfig.PLANET_HALF_SIZE + m_speed;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-m_angleDeg) * radius;
        float newY = originY + MathUtils.sinDeg(-m_angleDeg) * radius;

        setPosition(newX, newY);
    }

    private void updateShots(float delta){

        for (int j = 0; j < m_shots.size; j++){
            Shot shot = m_shots.get(j);

            shot.update(delta);

            if (shot.getShotTimer() <= 0){
                shot.setShotTimer(GameConfig.SHOT_TIMER);
                m_shotPool.free(shot);
                m_shots.removeIndex(j);
            }
        }
    }

    private void fall(){
        m_state = PlayerState.FALLING;
    }

    private void walk(){
        m_state = PlayerState.WALKING;
    }

    @Override
    public void reset() {
        m_angleDeg = GameConfig.START_ANGLE;
        m_shotPool.freeAll(m_shots);
        m_shots.clear();
    }
}



















