package com.the_spartan.flappy_ball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Spartan on 2/18/2018.
 */

public class GameOverScreen implements Screen {

    Stage stage;
    TextureAtlas atlas;
    Skin skin;
    Table table;
    TextButton replayButton;
    TextButton mainMenuButton;
    BitmapFont font;
    Label label;
    Game game;
    Texture bg;

    SpriteBatch batch;


    int score;
    int highscore;
    int difficulty;
    long hitAudioId;

    Sound hit;
    Sound click;

    public GameOverScreen(Game game, int score, int highscore, int difficulty, long hitAudioId){
        this.game = game;
        this.score = score;
        this.highscore = highscore;
        this.difficulty = difficulty;
        this.hitAudioId = hitAudioId;

        hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        stage = new Stage();
        atlas = new TextureAtlas("iconButtons.pack");
        skin = new Skin(atlas);
        bg = new Texture("gameOver.png");
        font = new BitmapFont(Gdx.files.internal("scoreFont.fnt"));
        font.setColor(Color.FIREBRICK);
        font.getData().setScale(Scaler.scale(2));
        batch = new SpriteBatch();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("replayButton");
        textButtonStyle.down = skin.getDrawable("replayButton");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.up = skin.getDrawable("menuButton");
        textButtonStyle2.down = skin.getDrawable("menuButton");
        textButtonStyle2.pressedOffsetX = 1;
        textButtonStyle2.pressedOffsetY = -1;
        textButtonStyle2.font = font;

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/3);
        replayButton = new TextButton(" ", textButtonStyle);
        mainMenuButton = new TextButton(" ", textButtonStyle2);

        table.columnDefaults(1).space(Scaler.scale(60));
        table.add(replayButton)
                .width(Scaler.scale(replayButton.getWidth()/1.5f))
                .height(Scaler.scale(replayButton.getHeight()/1.5f));
   //     table.row();
        table.add(mainMenuButton)
                .width(Scaler.scale(mainMenuButton.getWidth()/1.5f))
                .height(Scaler.scale(mainMenuButton.getHeight()/1.5f));
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        replayButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                switch (difficulty){
                    case 1:
                        hit.stop(hitAudioId);
                        click.play();
                        game.setScreen(new BeginnerGameScreen(game));
                        break;
                    case 2:
                        hit.stop(hitAudioId);
                        click.play();
                        game.setScreen(new IntermediateGameScreen(game));
                        break;
                    case 3:
                        hit.stop(hitAudioId);
                        click.play();
                        game.setScreen(new ExpertGameScreen(game));
                        break;
                }
                Gdx.input.setInputProcessor(null);
                return true;
            }
        });

        mainMenuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hit.stop(hitAudioId);
                click.play();
                game.setScreen(new LevelSelectionScreen(game));
                return true;
            }
        });


        stage.act();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.draw(batch,
                "Your Score: " + score,
                Gdx.graphics.getWidth()/2 - Scaler.scale(200),
                Gdx.graphics.getHeight()/2.15f);
        font.draw(batch,
                "High Score: " + highscore,
                Gdx.graphics.getWidth()/2 - Scaler.scale(200),
                Gdx.graphics.getHeight()/2.45f);
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
