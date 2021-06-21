package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screen.loading.LoadingScreen;

public class WorldRunnerGame extends Game {

	private AssetManager m_assetManager;
	private SpriteBatch m_spriteBatch;

	public AssetManager getAssetManager() {
		return m_assetManager;
	}

	public SpriteBatch getSpriteBatch() {
		return m_spriteBatch;
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		m_assetManager = new AssetManager();
		m_assetManager.getLogger().setLevel(Logger.DEBUG);
		m_spriteBatch = new SpriteBatch();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		m_assetManager.dispose();
		m_spriteBatch.dispose();
	}
}
