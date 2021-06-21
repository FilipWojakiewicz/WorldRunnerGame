package com.mygdx.game.config;

public class GameConfig {

    //Rozdzielczość mojego telefonu
//    public static final float WIDTH = 1080f;
//    public static final float HEIGHT = 2340f;
//
//    public static final float HUD_WIDTH = 1080f;
//    public static final float HUD_HEIGHT = 2340f;
//
//    public static final float WORLD_WIDTH = 16f;
//    public static final float WORLD_HEIGHT = 34f;

    public static final float WIDTH = 600f;
    public static final float HEIGHT = 800f;

    public static final float HUD_WIDTH = 600f;
    public static final float HUD_HEIGHT = 800f;

    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 24f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;

    public static final int CELL_SIZE = 1;

    public static final float PLANET_SIZE = 12f;
    public static final float PLANET_HALF_SIZE = PLANET_SIZE / 2f;

    public static final float PLAYER_SIZE = 1f;
    public static final float PLAYER_HALF_SIZE = PLAYER_SIZE / 2f;
    public static final float PLAYER_START_ANG_SPEED = 45f;
    public static final float START_ANGLE = -90f;

    public static final float SHOT_SIZE = 1f;
    public static final float SHOT_TIMER = 0.5f;
    public static final float SHOT_START_ANG_SPEED = 180f;

    public static final float ENEMY_SIZE = 1f;
    public static final float ENEMY_SPAWN_TIME = 2f;
    public static final int MAX_ENEMIES = 4;
    public static final int ENEMY_SCORE = 10;
    public static final float ENEMY_SPEED = 20f;

    public static final float PLAYER_MAX_SPEED = 2.3f;
    public static final float PLAYER_START_ACC = 4.5f;

    public static final float COIN_SIZE = 1f;
    public static final float COIN_SPAWN_TIME = 1.25f;
    public static final int MAX_COINS = 2;
    public static final int COIN_SCORE = 10;
    public static final float COIN_SPAWN_RADIUS = 1.4f;

    public static final float OBSTACLE_SIZE = 1f;
    public static final float OBSTACLE_SPAWN_TIME = 0.75f;
    public static final int MAX_OBSTACLES = 2;
    public static final int OBSTACLE_SCORE = 10;

    public static final float START_WAIT_TIME = 3f;
    public static final float MIN_ANG_DIST = 80f;

    private GameConfig() {}
}
