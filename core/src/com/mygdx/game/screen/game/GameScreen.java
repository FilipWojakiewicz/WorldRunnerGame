package com.mygdx.game.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.WorldRunnerGame;
import com.mygdx.game.util.GdxUtils;

public class GameScreen extends ScreenAdapter {

    private final WorldRunnerGame m_game;
    private final AssetManager m_assetManager;

    private GameController m_controller;
    private GameRenderer m_renderer;

    public GameScreen(WorldRunnerGame game) {
        m_game = game;
        m_assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        m_controller = new GameController();
        m_renderer = new GameRenderer(m_controller, m_game.getSpriteBatch(), m_assetManager);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        m_controller.update(delta);
        m_renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        m_renderer.resize(width, height);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        m_renderer.dispose();
    }
}
