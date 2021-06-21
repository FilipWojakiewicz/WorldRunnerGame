package com.mygdx.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.GameState;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Coin;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Obstacle;
import com.mygdx.game.entity.Planet;
import com.mygdx.game.entity.Player;
import com.mygdx.game.entity.Shot;
import com.mygdx.game.screen.menu.OverlayCallback;

public class GameController {

    private Planet m_planet;
    private Player m_player;

    private float m_playerStartX;
    private float m_playerStartY;

    private final Array<Coin> m_coins = new Array<>();
    private final Pool<Coin> m_coinPool = Pools.get(Coin.class, 10);
    private float m_coinTimer;

    private final Array<Obstacle> m_obstacles = new Array<>();
    private final Pool<Obstacle> m_obstaclePool = Pools.get(Obstacle.class, 10);
    private float m_obstacleTimer;

    private final Array<Enemy> m_enemies = new Array<>();
    private final Pool<Enemy> m_enemyPool = Pools.get(Enemy.class, 10);
    private float m_enemyTimer;

    private float m_startWaitTimer = GameConfig.START_WAIT_TIME;
    private float m_gameTime;

    private GameState gameState = GameState.MENU;
    private OverlayCallback callback;

    public GameController() { init(); }

    private void init(){
        m_planet = new Planet();
        m_planet.setPosition(
                GameConfig.WORLD_CENTER_X - GameConfig.PLANET_HALF_SIZE,
                GameConfig.WORLD_CENTER_Y - GameConfig.PLANET_HALF_SIZE
        );

        m_playerStartX = GameConfig.WORLD_CENTER_X - GameConfig.PLAYER_HALF_SIZE;
        m_playerStartY = GameConfig.WORLD_CENTER_Y + GameConfig.PLANET_HALF_SIZE;

        m_player = new Player();
        m_player.setPosition(m_playerStartX, m_playerStartY);

        callback = new OverlayCallback() {
            @Override
            public void home() {
                gameState = GameState.MENU;
            }

            @Override
            public void ready() {
                restart();
                gameState = GameState.READY;
            }
        };
    }

    public void update(float delta){
        if (gameState.isReady() && m_startWaitTimer > 0){
            m_startWaitTimer -= delta;

            if (m_startWaitTimer <= 0){
                gameState = GameState.PLAYING;
            }
        }

        if (!gameState.isPlaying())
            return;

        m_gameTime += delta;
        GameManager.INSTANCE.updateDisplayScore(delta);

        if (Gdx.input.isTouched()){
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (m_player.isWalking() && touchPosition.x < GameConfig.WIDTH / 2)
                m_player.jump();
            else if (touchPosition.x > GameConfig.WIDTH / 2)
                m_player.shoot();
        }

        if (m_enemies.size > 0){
            for (Enemy enemy : m_enemies) {
                enemy.update(delta);
            }
        }

        m_player.update(delta);
        spawnObstacles(delta);
        spawnCoins(delta);
        spawnEnemy(delta);
        checkCollision();
    }

    public Planet getPlanet() {
        return m_planet;
    }

    public Player getPlayer() {
        return m_player;
    }

    public Array<Coin> getCoins() {
        return m_coins;
    }

    public Array<Obstacle> getObstacles() {
        return m_obstacles;
    }
    public Array<Enemy> getEnemies() {
        return m_enemies;
    }

    public float getStartWaitTimer() {
        return m_startWaitTimer;
    }

    public float getGameTime() {
        return m_gameTime;
    }

    public GameState getGameState() {
        return gameState;
    }

    public OverlayCallback getCallback() {
        return callback;
    }

    private void spawnCoins(float delta){
        m_coinTimer += delta;

        if(m_coinTimer < GameConfig.COIN_SPAWN_TIME)
            return;

        m_coinTimer = 0;

        if (m_coins.size == 0)
            addCoins();
    }

    private void addCoins(){
        int count = GameConfig.MAX_COINS;

        for (int i = 0; i < count; i++){
            float randomAngle = MathUtils.random(360f);
            boolean canSpawn = !isCoinNearBy(randomAngle) && !isPlayerNearBy(randomAngle);

            if (canSpawn){
                Coin coin = m_coinPool.obtain();
                coin.setAngleDeg(randomAngle);
                m_coins.add(coin);
            }
        }
    }

    private void spawnEnemy(float delta){
        m_enemyTimer += delta;

        if (m_enemyTimer < GameConfig.ENEMY_SPAWN_TIME)
            return;

        m_enemyTimer = 0;

        if (m_enemies.size == 0)
            addEnemies();
    }

    private void addEnemies(){
        int count = GameConfig.MAX_ENEMIES;

        for (int i = 0; i < count; i++){
            float randomAngle = m_player.getAngleDeg() - i * GameConfig.MIN_ANG_DIST - MathUtils.random(60, 80);
            boolean canSpawn = !isObstacleNearBy(randomAngle) && !isEnemyNearBy(randomAngle) && !isPlayerNearBy(randomAngle);

            if (canSpawn){
                Enemy enemy = m_enemyPool.obtain();
                enemy.setAngleDeg(randomAngle);
                m_enemies.add(enemy);
            }
        }
    }

    private boolean isCoinNearBy(float angle){
        for (Coin coin : m_coins){
            float angleDeg = coin.getAngleDeg();
            float difference = Math.abs(Math.abs(angleDeg) - Math.abs(angle));

            if (difference < GameConfig.MIN_ANG_DIST)
                return true;
        }

        return false;
    }

    private boolean isEnemyNearBy(float angle){
        for (Enemy enemy : m_enemies){
            float angleDeg = enemy.getAngleDeg();
            float difference = Math.abs(Math.abs(angleDeg) - Math.abs(angle));

            if (difference < GameConfig.MIN_ANG_DIST)
                return true;
        }

        return false;
    }

    private boolean isPlayerNearBy(float angle){
        float playerDiff = Math.abs(Math.abs(m_player.getAngleDeg()) - Math.abs(angle));

        return playerDiff < GameConfig.MIN_ANG_DIST;
    }

    private boolean isObstacleNearBy(float angle){
        for (Obstacle obstacle : m_obstacles){
            float angleDeg = obstacle.getAngleDeg();

            float difference = Math.abs(Math.abs(angleDeg) - Math.abs(angle));

            if (difference > GameConfig.MIN_ANG_DIST){
                return true;
            }
        }

        return false;
    }

    private void spawnObstacles(float delta){
        m_obstacleTimer += delta;

        if (m_obstacleTimer < GameConfig.OBSTACLE_SPAWN_TIME)
            return;

        m_obstacleTimer = 0;

        if (m_obstacles.size == 0)
            addObstacles();
    }

    private void addObstacles(){
        int count = GameConfig.MAX_OBSTACLES;

        for (int i = 0; i < count; i++){
            float randomAngle = m_player.getAngleDeg() - i * GameConfig.MIN_ANG_DIST - MathUtils.random(80, 100);
            boolean canSpawn = !isObstacleNearBy(randomAngle) && !isEnemyNearBy(randomAngle) && !isPlayerNearBy(randomAngle);

            if (canSpawn){
                Obstacle obstacle = m_obstaclePool.obtain();
                obstacle.setAngleDeg(randomAngle);
                m_obstacles.add(obstacle);
            }
        }
    }

    private void checkCollision(){
        for (int i = 0; i < m_coins.size; i++){
            Coin coin = m_coins.get(i);

            if (Intersector.overlaps(m_player.getBounds(), coin.getBounds())){
                GameManager.INSTANCE.addScore(GameConfig.COIN_SCORE);
                m_coinPool.free(coin);
                m_coins.removeIndex(i);
            }
        }

        for (int i = 0; i < m_obstacles.size; i++){
            Obstacle obstacle = m_obstacles.get(i);

            if (Intersector.overlaps(m_player.getBounds(), obstacle.getSensor())){
                GameManager.INSTANCE.addScore(GameConfig.OBSTACLE_SCORE);
                m_obstaclePool.free(obstacle);
                m_obstacles.removeIndex(i);
            } else if (Intersector.overlaps(m_player.getBounds(), obstacle.getBounds()))
                gameState = GameState.GAME_OVER;
        }

        for (int i = 0; i < m_enemies.size; i++){
            Enemy enemy = m_enemies.get(i);
            if (Intersector.overlaps(m_player.getBounds(), enemy.getBounds()))
                gameState = GameState.GAME_OVER;
        }

        for (int i = 0; i < m_player.getShots().size; i++){
            Shot shot = m_player.getShots().get(i);

            for (int j = 0; j < m_enemies.size; j++){
                Enemy enemy = m_enemies.get(j);
                if (Intersector.overlaps(enemy.getBounds(), shot.getBounds())){
                    shot.setShotTimer(0f);
                    GameManager.INSTANCE.addScore(GameConfig.ENEMY_SCORE);
                    m_enemyPool.free(enemy);
                    m_enemies.removeIndex(j);
                }
            }
        }
    }

    public void restart(){

        m_coinPool.freeAll(m_coins);
        m_coins.clear();

        m_obstaclePool.freeAll(m_obstacles);
        m_obstacles.clear();

        m_enemyPool.freeAll(m_enemies);
        m_enemies.clear();

        m_player.reset();
        m_player.setPosition(m_playerStartX, m_playerStartY);

        GameManager.INSTANCE.updateHighScore();
        GameManager.INSTANCE.reset();
        m_startWaitTimer = GameConfig.START_WAIT_TIME;

        m_gameTime = 0f;
        gameState = GameState.READY;
    }
}
