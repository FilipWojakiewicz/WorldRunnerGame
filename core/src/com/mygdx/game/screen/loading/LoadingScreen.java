package com.mygdx.game.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.assets.AssetDescriptors;
import com.mygdx.game.WorldRunnerGame;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.screen.game.GameScreen;
import com.mygdx.game.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {

    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f;
    private static final float PROGRESS_BAR_HEIGHT = 60f;

    private final WorldRunnerGame m_game;
    private final AssetManager m_assetManager;

    private OrthographicCamera m_camera;
    private Viewport m_viewport;
    private ShapeRenderer m_renderer;

    private float m_progress;
    private float m_waitTime = 0.75f;

    private boolean changeScreen;

    public LoadingScreen(WorldRunnerGame game) {
        m_game = game;
        m_assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        m_camera = new OrthographicCamera();
        m_viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, m_camera);
        m_renderer = new ShapeRenderer();

        m_assetManager.load(AssetDescriptors.FONT);
        m_assetManager.load(AssetDescriptors.GAME_PLAY);
        m_assetManager.load(AssetDescriptors.SKIN);
    }

    @Override
    public void render(float delta) {
        update(delta);

        GdxUtils.clearScreen();
        m_viewport.apply();
        m_renderer.setProjectionMatrix(m_camera.combined);
        m_renderer.begin(ShapeRenderer.ShapeType.Filled);

        draw();

        m_renderer.end();

        if (changeScreen)
            m_game.setScreen(new GameScreen(m_game));
    }

    @Override
    public void resize(int width, int height) {
        m_viewport.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        m_renderer.dispose();
    }

    private void update(float delta){
        m_progress = m_assetManager.getProgress();

        if (m_assetManager.update()){
            m_waitTime -= delta;

            if (m_waitTime <= 0)
                changeScreen = true;
        }
    }

    private void draw(){
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;

        m_renderer.rect(progressBarX, progressBarY,
                m_progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
    }
}
