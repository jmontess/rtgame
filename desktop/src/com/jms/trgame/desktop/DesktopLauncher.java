package com.jms.trgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jms.trgame.TRGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TRGame.GAME_TITLE;
		config.x = TRGame.SCREEN_WIDTH;
		config.y = TRGame.SCREEN_HEIGHT;
		new LwjglApplication(new TRGame(), config);
	}
}
