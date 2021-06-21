package com.mygdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> FONT = new AssetDescriptor<>(AssetPaths.FONT, BitmapFont.class);
    public static final AssetDescriptor<TextureAtlas> GAME_PLAY = new AssetDescriptor<>(AssetPaths.GAME_PLAY, TextureAtlas.class);
    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<>(AssetPaths.SKIN, Skin.class);

    private AssetDescriptors() {
    }
}
