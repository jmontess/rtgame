package com.jms.trgame.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.jms.trgame.TRGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new TRGame();
        }
}