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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Spartan on 2/27/2018.
 */

public class LevelSelectionScreen implements Screen {

    Game game;
    Texture bg;
    Texture bg2;
    Texture fg;
    Texture ball;
    TextButton beginnerButton;
    TextButton intermediateButton;
    TextButton expertButton;
    TextButton.TextButtonStyle textButtonStyle;
    Stage stage;
    Table table;
    TextureAtlas atlas;
    TextureAtlas atlasForHome;
    Skin skin;
    BitmapFont font;
    Sprite spriteFG;
    TextButton homeButton;

    Sound click;

    SpriteBatch batch;
    int x;
    int x2;

    public LevelSelectionScreen(Game game){
        this.game = game;
        x = 0;
        x2 = Gdx.graphics.getWidth() * 2;
        bg = new Texture("splashBG.png");
        bg2 = new Texture("splashBG.png");

        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        stage = new Stage();
        atlas = new TextureAtlas("levelButtons.pack");
        skin = new Skin(atlas);
        font = new BitmapFont();
        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle beginnerButtonStyle = new TextButton.TextButtonStyle();
        beginnerButtonStyle.up = skin.getDrawable("beginner");
        beginnerButtonStyle.down = skin.getDrawable("beginner");
        beginnerButtonStyle.pressedOffsetX = 1;
        beginnerButtonStyle.pressedOffsetY = -1;
        beginnerButtonStyle.font = font;
        beginnerButton = new TextButton(" ", beginnerButtonStyle);
//        beginnerButton.pad(20);


        TextButton.TextButtonStyle intermediateButtonStyle = new TextButton.TextButtonStyle();
        intermediateButtonStyle.up = skin.getDrawable("intermediate");
        intermediateButtonStyle.down = skin.getDrawable("intermediate");
        intermediateButtonStyle.pressedOffsetX = 1;
        intermediateButtonStyle.pressedOffsetY = -1;
        intermediateButtonStyle.font = font;
        intermediateButton = new TextButton(" ", intermediateButtonStyle);
//        intermediateButton.pad(20);


        TextButton.TextButtonStyle expertButtonStyle = new TextButton.TextButtonStyle();
        expertButtonStyle.up = skin.getDrawable("expert");
        expertButtonStyle.down = skin.getDrawable("expert");
        expertButtonStyle.pressedOffsetX = 1;
        expertButtonStyle.pressedOffsetY = -1;
        expertButtonStyle.font = font;
        expertButton = new TextButton(" ", expertButtonStyle);


        TextButton.TextButtonStyle homeButtonStyle = new TextButton.TextButtonStyle();
        homeButtonStyle.up = skin.getDrawable("homeButton");
        homeButtonStyle.down = skin.getDrawable("homeButton");
        homeButtonStyle.pressedOffsetX = 1;
        homeButtonStyle.pressedOffsetY = -1;
        homeButtonStyle.font = font;
        homeButton = new TextButton(" ", homeButtonStyle);

        table.add(beginnerButton)
                .width(Scaler.scale(beginnerButton.getWidth() / 1.5f))
                .height(Scaler.scale(beginnerButton.getHeight() / 1.5f));
        table.row();
        table.add(intermediateButton)
                .width(Scaler.scale(intermediateButton.getWidth() / 1.5f))
                .height(Scaler.scale(intermediateButton.getHeight() / 1.5f));
        table.row();
        table.add(expertButton)
                .width(Scaler.scale(expertButton.getWidth() / 1.5f))
                .height(Scaler.scale(expertButton.getHeight() / 1.5f));
        table.row();
        table.add(homeButton)
        .width(Scaler.scale(homeButton.getWidth()/1.5f))
        .height(Scaler.scale(homeButton.getHeight()/1.5f));

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

    }

    @Override
    public void show() {
        beginnerButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("BUTTON", "BEGINNER");

                click.play();

                game.setScreen(new BeginnerGameScreen(game));
                Gdx.input.setInputProcessor(null);
                return true;
            }
        });

        intermediateButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("BUTTON", "INTERMEDIATE");

                click.play();

                game.setScreen(new IntermediateGameScreen(game));
                Gdx.input.setInputProcessor(null);
                return true;
            }
        });

        expertButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("BUTTON", "EXPERT");

                click.play();

                game.setScreen(new ExpertGameScreen(game));
                Gdx.input.setInputProcessor(null);
                return true;
            }
        });

        homeButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                click.play();

                game.setScreen(new MenuScreen(game));
           //     Gdx.input.setInputProcessor(null);
                return true;
            }
        });

        stage.act();
    }

    @Override
    public void render(float delta) {
        x--;
        x2 --;
        Gdx.app.log("POSITION", String.valueOf(x));
        if (x == -Gdx.graphics.getWidth()){
            x2 = Gdx.graphics.getWidth();
        } else if (x2 == -Gdx.graphics.getWidth()){
            x = Gdx.graphics.getWidth();
        }
        batch.begin();
        batch.draw(bg, x, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight());
        batch.draw(bg2, x2, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight());

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
