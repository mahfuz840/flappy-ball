package com.the_spartan.flappy_ball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.naming.Context;
import javax.security.sasl.SaslClient;

/**
 * Created by Spartan on 2/21/2018.
 */

public class MenuScreen implements Screen {

    Game game;
    Texture bg;
    Texture bg2;
    Texture fg;
    Texture ball;
    TextButton playButton;
    Stage stage;
    Table table;
    TextureAtlas atlas;
    Skin skin;
    BitmapFont font;
    Sprite spriteFG;
    TextButton rateButton;

    SpriteBatch batch;
    int x;
    int x2;

    Sound click;

    public MenuScreen( Game game){
        this.game = game;
        x = 0;
        x2 = Gdx.graphics.getWidth() * 2;
        bg = new Texture("splashBG.png");
        bg2 = new Texture("splashBG.png");
        fg = new Texture("splashFG.png");

        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        spriteFG = new Sprite(fg);

        stage = new Stage();
        atlas = new TextureAtlas("iconButtons.pack");
        skin = new Skin(atlas);
        font = new BitmapFont();

        TextButton.TextButtonStyle playButtonStyle = new TextButton.TextButtonStyle();
        playButtonStyle.up = skin.getDrawable("playButton");
        playButtonStyle.down = skin.getDrawable("playButton");
        playButtonStyle.pressedOffsetX = 1;
        playButtonStyle.pressedOffsetY = -1;
        playButtonStyle.font = font;

        TextButton.TextButtonStyle rateButtonStyle = new TextButton.TextButtonStyle();
        rateButtonStyle.up = skin.getDrawable("rateButton");
        rateButtonStyle.down = skin.getDrawable("rateButton");
        rateButtonStyle.pressedOffsetX = 1;
        rateButtonStyle.pressedOffsetY = -1;
        rateButtonStyle.font = font;

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2.5f);
        playButton = new TextButton(" ", playButtonStyle);
        playButton.pad(20);

        rateButton = new TextButton(" ", rateButtonStyle);
        //   rateButton.pad(20);

        table.columnDefaults(1).space(Scaler.scale(60));
        table.add(playButton)
                .width(Scaler.scale(playButton.getWidth()/1.5f))
                .height(Scaler.scale(playButton.getHeight()/1.5f));
        table.add(rateButton)
                .width(Scaler.scale(rateButton.getWidth()/1.5f))
                .height(Scaler.scale(rateButton.getHeight()/1.5f));
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

    }

    @Override
    public void show() {
        playButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                click.play();
                game.setScreen(new LevelSelectionScreen(game));
//                Gdx.input.setInputProcessor(null);
                return true;
            }
        });


        rateButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("RATE BUTTON", "TAPPED");

                click.play();

                try{
                    Gdx.net.openURI("market://details?id=com.the_spartan.flappy_ball");
                } catch (Exception e){
                    Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.the_spartan.flappy_ball");
                }
                return true;
            }
        });


        stage.act();
    }

    @Override
    public void render(float delta) {
        x--;
        x2 --;

        if (x == -Gdx.graphics.getWidth()){
            x2 = Gdx.graphics.getWidth();
        } else if (x2 == -Gdx.graphics.getWidth()){
            x = Gdx.graphics.getWidth();
        }
        batch.begin();
        batch.draw(bg, x, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight());
        batch.draw(bg2, x2, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight());

        batch.draw(fg,
                Gdx.graphics.getWidth()/2 - Scaler.scale(fg.getWidth())/2,
                Gdx.graphics.getHeight() - Scaler.scale(fg.getHeight() + fg.getHeight()/4f),
                Scaler.scale(fg.getWidth()),
                Scaler.scale(fg.getHeight()));

        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
