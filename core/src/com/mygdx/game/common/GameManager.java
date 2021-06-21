package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.WorldRunnerGame;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();
    private static final String HIGH_SCORE_KEY = "highScore";

    private int m_score;
    private int m_displayScore;
    private int m_highScore;
    private int m_displayHighScore;

    private Preferences m_preferences;

    private GameManager() {
        m_preferences = Gdx.app.getPreferences(WorldRunnerGame.class.getSimpleName());
        m_highScore = m_preferences.getInteger(HIGH_SCORE_KEY, 0);
        m_displayHighScore = m_highScore;
    }

    public void reset(){
        m_score = 0;
        m_displayScore = 0;
    }

    public void addScore(int amount){
        m_score += amount;

        if (m_score > m_highScore){
            m_highScore = m_score;
        }
    }

    public void updateHighScore(){
        if (m_score < m_highScore)
            return;

        m_highScore = m_score;
        m_preferences.putInteger(HIGH_SCORE_KEY, m_highScore);
        m_preferences.flush();
    }

    public void updateDisplayScore(float delta){
        if (m_displayScore < m_score)
            m_displayScore = Math.min(m_score, m_displayScore + (int)(100 * delta));

        if (m_displayHighScore < m_highScore)
            m_displayHighScore = Math.min(m_highScore, m_displayHighScore + (int)(100 * delta));
    }

    public int getDisplayScore() {
        return m_displayScore;
    }

    public int getDisplayHighScore() {
        return m_displayHighScore;
    }

    public int getHighScore() {
        return m_highScore;
    }

    public int getScore() {
        return m_score;
    }
}
