package com.mygdx.game.screen.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.assets.ButtonStyleNames;
import com.mygdx.assets.RegionNames;
import com.mygdx.game.common.GameManager;

public class GameOverOverlay extends Table {

    private final OverlayCallback m_callback;

    private Label m_scoreLabel;
    private Label m_highScoreLabel;

    public GameOverOverlay(Skin skin, OverlayCallback callback) {
        super(skin);
        m_callback = callback;
        init();
    }

    private void init(){
        defaults().pad(20);

        Image gameOverImage = new Image(getSkin(), RegionNames.GAME_OVER);
        Table scoreTable = new Table(getSkin());
        scoreTable.defaults().pad(10);
        scoreTable.setBackground(RegionNames.PANEL);
        scoreTable.add("Score: ").row();
        m_scoreLabel = new Label("", getSkin());
        scoreTable.add(m_scoreLabel).row();
        scoreTable.add("Best: ").row();
        m_highScoreLabel = new Label("", getSkin());
        scoreTable.add(m_highScoreLabel);
        scoreTable.center();

        Table buttonTable = new Table();
        ImageButton homeButton = new ImageButton(getSkin(), ButtonStyleNames.HOME);
        homeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                m_callback.home();
            }
        });

        ImageButton restartButton = new ImageButton(getSkin(), ButtonStyleNames.RESTART);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                m_callback.ready();
            }
        });

        buttonTable.add(homeButton).center().expandX();
        buttonTable.add(restartButton).center().expandX();

        add(gameOverImage).row();
        add(scoreTable).row();
        add(buttonTable).grow();

        center();
        setFillParent(true);
        pack();
        updateLabels();
    }

    public void updateLabels(){
        m_scoreLabel.setText("" + GameManager.INSTANCE.getScore());
        m_highScoreLabel.setText("" + GameManager.INSTANCE.getHighScore());
    }
}
