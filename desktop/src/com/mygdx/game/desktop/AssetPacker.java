package com.mygdx.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    private static final String RAW_ASSETS_PATH = "desktop/assets";
    private static final String ASSETS_PATH = "android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings m_settings = new TexturePacker.Settings();
        m_settings.flattenPaths = true;
        m_settings.combineSubdirectories = true;

        TexturePacker.process(m_settings,
                RAW_ASSETS_PATH + "/gameplay",
                ASSETS_PATH + "/gameplay",
                "gameplay");

        TexturePacker.process(m_settings,
                RAW_ASSETS_PATH + "/ui",
                ASSETS_PATH + "/ui",
                "skin");
    }
}
