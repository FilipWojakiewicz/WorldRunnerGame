package com.mygdx.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.assets.AssetDescriptors;
import com.mygdx.assets.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.GameState;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.entity.Coin;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Obstacle;
import com.mygdx.game.entity.Planet;
import com.mygdx.game.entity.Player;
import com.mygdx.game.entity.Shot;
import com.mygdx.game.screen.menu.GameOverOverlay;
import com.mygdx.game.screen.menu.MenuOverlay;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    private final GameController m_controller;
    private final SpriteBatch m_batch;
    private final AssetManager m_assetManager;

    private OrthographicCamera m_camera;
    private Viewport m_viewport;
    private ShapeRenderer m_renderer;

    private Viewport m_hudViewport;
    private BitmapFont m_font;

    private final GlyphLayout m_layout = new GlyphLayout();

    private DebugCameraController m_debugCameraController;

    private TextureRegion m_backgroundRegion;
    private TextureRegion m_planetAnimation;

    private TextureRegion m_obstacleRegion;
    private TextureRegion m_enemyRegion;
    private TextureRegion m_shotRegion;
    private Animation m_coinAnimation;
    private Animation m_playerAnimation;

    private Stage m_hudStage;
    private MenuOverlay m_menuOverlay;
    private GameOverOverlay m_gameOverOverlay;


    public GameRenderer(GameController m_controller, SpriteBatch batch, AssetManager assetManager) {
        this.m_controller = m_controller;
        m_batch = batch;
        m_assetManager = assetManager;
        init();
    }

    private void init(){
        m_camera = new OrthographicCamera();
        m_viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, m_camera);
        m_renderer = new ShapeRenderer();

        m_hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        m_hudStage = new Stage(m_hudViewport, m_batch);

        m_font = m_assetManager.get(AssetDescriptors.FONT);
        m_font.setColor(Color.valueOf("FFA37B"));

        Skin skin = m_assetManager.get(AssetDescriptors.SKIN);

        m_debugCameraController = new DebugCameraController();
        m_debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        TextureAtlas gamePlayAtlas = m_assetManager.get(AssetDescriptors.GAME_PLAY);

        m_backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        m_planetAnimation = gamePlayAtlas.findRegion(RegionNames.PLANET);
        m_obstacleRegion = gamePlayAtlas.findRegions(RegionNames.OBSTACLE).get(2);
        m_enemyRegion = gamePlayAtlas.findRegions(RegionNames.ENEMY).first();
        m_shotRegion = gamePlayAtlas.findRegions(RegionNames.SHOT).first();

        m_coinAnimation = new Animation(0.1f,
                gamePlayAtlas.findRegions(RegionNames.COIN),
                Animation.PlayMode.LOOP
        );
        m_playerAnimation = new Animation(0.05f,
                gamePlayAtlas.findRegions(RegionNames.PLAYER),
                Animation.PlayMode.LOOP
        );

        m_menuOverlay = new MenuOverlay(skin, m_controller.getCallback());
        m_gameOverOverlay = new GameOverOverlay(skin, m_controller.getCallback());

        m_hudStage.addActor(m_menuOverlay);
        m_hudStage.addActor(m_gameOverOverlay);

        Gdx.input.setInputProcessor(m_hudStage);
    }

    public void render(float delta){
        m_debugCameraController.handleDebugInput(delta);
        m_debugCameraController.applyTo(m_camera);

        renderGamePlay(delta);
        //renderDebug();
        renderHud();
    }

    public void resize(int width, int height){
        m_viewport.update(width, height, true);
        m_hudViewport.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(m_viewport);
    }

    @Override
    public void dispose() {
        m_renderer.dispose();
    }

    private void renderGamePlay(float delta){
        m_viewport.apply();
        m_batch.setProjectionMatrix(m_camera.combined);
        m_batch.begin();

        drawGamePlay(delta);

        m_batch.end();
    }

    private void drawGamePlay(float delta){
        m_batch.draw(m_backgroundRegion,
                0,0,
                GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        Planet planet = m_controller.getPlanet();
        m_batch.draw(m_planetAnimation,
                planet.getX(), planet.getY(),
                planet.getWidth(), planet.getHeight());

        Array<Obstacle> obstacles = m_controller.getObstacles();
        for (Obstacle obstacle : obstacles){
            m_batch.draw(m_obstacleRegion,
                    obstacle.getX(), obstacle.getY(),
                    0, 0,
                    obstacle.getWidth(), obstacle.getHeight(),
                    1.0f, 1.0f,
                    GameConfig.START_ANGLE - obstacle.getAngleDeg());
        }

        Array<Enemy> enemies = m_controller.getEnemies();
        for (Enemy enemy : enemies){
            m_batch.draw(m_enemyRegion,
                    enemy.getX(), enemy.getY(),
                    0, 0,
                    enemy.getWidth(), enemy.getHeight(),
                    1.0f, 1.0f,
                    GameConfig.START_ANGLE - enemy.getAngleDeg());
        }

        Array<Coin> coins = m_controller.getCoins();
        TextureRegion coinRegion = (TextureRegion) m_coinAnimation.getKeyFrame(m_controller.getGameTime());

        for (Coin coin : coins){
            m_batch.draw(coinRegion,
                    coin.getX(), coin.getY(),
                    0, 0,
                    coin.getWidth(), coin.getHeight(),
                    1.0f, 1.0f,
                    GameConfig.START_ANGLE - coin.getAngleDeg());
        }

        Player player = m_controller.getPlayer();
        TextureRegion playerRegion = (TextureRegion) m_playerAnimation.getKeyFrame(m_controller.getGameTime());
        m_batch.draw(playerRegion,
                player.getX(), player.getY(),
                0, 0,
                player.getWidth(), player.getHeight(),
                1.0f, 1.0f,
                GameConfig.START_ANGLE - player.getAngleDeg()
        );

        Array<Shot> shots = m_controller.getPlayer().getShots();
        for (Shot shot : shots){
            m_batch.draw(m_shotRegion,
                    shot.getX(), shot.getY(),
                    0, 0,
                    shot.getWidth(), shot.getHeight(),
                    1.0f, 1.0f,
                    GameConfig.START_ANGLE - shot.getAngleDeg());
        }
    }

    private void renderDebug(){
        ViewportUtils.drawGrid(m_viewport, m_renderer, GameConfig.CELL_SIZE);

        m_viewport.apply();
        m_renderer.setProjectionMatrix(m_camera.combined);
        m_renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        m_renderer.end();
    }

    private void drawDebug(){
        //Planet
        m_renderer.setColor(Color.RED);
        Planet planet = m_controller.getPlanet();
        Circle planetBounds = planet.getBounds();
        m_renderer.circle(planetBounds.x, planetBounds.y, planetBounds.radius, 30);

        //Player
        m_renderer.setColor(Color.BLUE);
        Player player = m_controller.getPlayer();
        Rectangle playerBounds = player.getBounds();
        m_renderer.rect(playerBounds.x, playerBounds.y,
                0,0,
                playerBounds.width, playerBounds.height,
                1,1,
                GameConfig.START_ANGLE - player.getAngleDeg()
        );

        //Coins
        m_renderer.setColor(Color.YELLOW);
        for (Coin coin : m_controller.getCoins()){
            Rectangle coinBounds = coin.getBounds();
            m_renderer.rect(coinBounds.x, coinBounds.y,
                    0,0,
                    coinBounds.width, coinBounds.height,
                    1,1,
                    GameConfig.START_ANGLE - coin.getAngleDeg()
            );
        }

        //Obstacles
        for (Obstacle obstacle : m_controller.getObstacles()){
            m_renderer.setColor(Color.GREEN);
            Rectangle obstacleBounds = obstacle.getBounds();
            m_renderer.rect(
                    obstacleBounds.x, obstacleBounds.y,
                    0,0,
                    obstacleBounds.width, obstacleBounds.height,
                    1,1,
                    GameConfig.START_ANGLE - obstacle.getAngleDeg()
            );

            m_renderer.setColor(Color.CYAN);
            Rectangle sensorBounds = obstacle.getSensor();
            m_renderer.rect(
                    sensorBounds.x, sensorBounds.y,
                    0,0,
                    sensorBounds.width, sensorBounds.height,
                    1,1,
                    GameConfig.START_ANGLE - obstacle.getSensorAngleDeg()
            );
        }
    }

    private void renderHud(){
        m_hudViewport.apply();
        m_menuOverlay.setVisible(false);
        m_gameOverOverlay.setVisible(false);

        GameState gameState = m_controller.getGameState();
        if (gameState.isPlaying() || gameState.isReady()){
            m_batch.setProjectionMatrix(m_hudViewport.getCamera().combined);
            m_batch.begin();
            drawHud();
            m_batch.end();
            return;
        }

        if (gameState.isMenu() && !m_menuOverlay.isVisible()){
            m_menuOverlay.updateLabel();
            m_menuOverlay.setVisible(true);
        }else if (gameState.isGameOver() && !m_gameOverOverlay.isVisible()){
            m_gameOverOverlay.updateLabels();
            m_gameOverOverlay.setVisible(true);
        }

        m_hudStage.act();
        m_hudStage.draw();
    }

    private void drawHud(){
        float padding = 20;

        String highScoreString = "HIGH SCORE: " + GameManager.INSTANCE.getDisplayHighScore();
        m_layout.setText(m_font, highScoreString);
        m_font.draw(m_batch, m_layout, padding, GameConfig.HUD_HEIGHT - m_layout.height);

        String scoreString = "SCORE: " + GameManager.INSTANCE.getDisplayScore();
        m_layout.setText(m_font, scoreString);
        m_font.draw(m_batch, m_layout,
                GameConfig.HUD_WIDTH - m_layout.width - padding,
                GameConfig.HUD_HEIGHT - m_layout.height
        );

        float startWaitTimer = m_controller.getStartWaitTimer();

        if (startWaitTimer >= 0){
            int waitTime = (int) startWaitTimer;
            String waitTimeString = waitTime == 0 ? "GO!" : ""+waitTime;
            m_layout.setText(m_font, waitTimeString);

            m_font.draw(m_batch, m_layout,
                    (GameConfig.HUD_WIDTH - m_layout.width) / 2f,
                    (GameConfig.HUD_HEIGHT + m_layout.height) / 2f
            );
        }
    }
}
