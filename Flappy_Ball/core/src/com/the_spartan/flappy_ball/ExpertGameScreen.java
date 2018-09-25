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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by Spartan on 2/27/2018.
 */

public class ExpertGameScreen implements Screen {

    Game game;
    SpriteBatch batch;
    Texture background;

    Texture gameover;

    Texture ball;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    int gameState = 0;
    float gravity = 1.5f;

    Texture topTube;
    Texture bottomTube;
    Texture smallTube;
    float gap = 500;
    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    boolean[] tubeUp = new boolean[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    Preferences preferences;
    int highscore;

    Random random;
    Sound wing;
    Sound point;
    Sound hit;

    float tubeSpeed = 3.5f;
    float lastScore = 0;
    float metrics;

    long hitAudioId;
    long wingAudioId = -1;
    long coinAudioId = -1;

    public ExpertGameScreen(Game game){

        metrics = Gdx.graphics.getDensity();

        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameOver.png");
        birdCircle = new Circle();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(metrics);

        gap = Scaler.scale(500);
        gravity = Scaler.scale(gravity);
        tubeVelocity = Scaler.scale(tubeVelocity);

        ball = new Texture("ball.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        smallTube = new Texture("smallTube.png");

        wing = Gdx.audio.newSound(Gdx.files.internal("wing.wav"));
        point = Gdx.audio.newSound(Gdx.files.internal("point.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - Scaler.scale(100);
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        random = new Random();

        startGame();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        {
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (gameState == 1) {

                if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 3) {

                    score++;

                    playScoreAudio();

                    Gdx.app.log("Score", String.valueOf(score));

                    if (scoringTube < numberOfTubes - 1) {

                        scoringTube++;

                    } else {

                        scoringTube = 0;

                    }

                }

                if (Gdx.input.justTouched()) {
                    velocity = Scaler.scale(-27);
                    playWingAudio();
                }

                for (int i = 0; i < numberOfTubes; i++) {

                    if (tubeX[i] < -Scaler.scale(topTube.getWidth())) {
                        tubeX[i] += numberOfTubes * distanceBetweenTubes;
                        tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - Scaler.scale(200));
                    } else {
                        tubeX[i] = tubeX[i] - tubeVelocity;
                    }

                    if (score - lastScore == 5){
                        game.pause();
                        lastScore = score;
                        tubeSpeed += Scaler.scale(0.5f);
                    }

                    if (tubeUp[i]) {
                        tubeOffset[i] += tubeSpeed;
                    } else {
                        tubeOffset[i] -= tubeSpeed;
                    }

                    Gdx.app.log("OFFSET", String.valueOf(tubeOffset[i]));

                    if ((tubeOffset[i] + Scaler.scale(smallTube.getHeight())) >= Gdx.graphics.getHeight()) {
                        tubeUp[i] = false;
                    } else if (tubeOffset[i] <= 0) {
                        tubeUp[i] = true;
                    }

                    batch.draw(smallTube,
                            tubeX[i],
                            tubeOffset[i],
                            Scaler.scale(smallTube.getWidth()),
                            Scaler.scale(smallTube.getHeight()));
//                    batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                    topTubeRectangles[i] = new Rectangle(tubeX[i],
                            tubeOffset[i],
                            Scaler.scale(smallTube.getWidth()),
                            Scaler.scale(smallTube.getHeight()));
                //    bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
                }


                if (birdY > 0 && birdY < Gdx.graphics.getHeight()) {

                    velocity = velocity + gravity;
                    birdY -= velocity;

                } else {

                    gameState = 2;

                }

            } else if (gameState == 0) {

                if (Gdx.input.justTouched()) {

                    velocity = Scaler.scale(-22);
                    gameState = 1;


                }

            } else if (gameState == 2) {
                playHitAudio();
                if(coinAudioId != -1){
                    point.stop(coinAudioId);
                }
                preferences = Gdx.app.getPreferences("intermediateHighscorePreference");
                highscore = preferences.getInteger("highscore", 0);
                if (score > highscore){
                    Gdx.app.log("SCORE", "NEW HIGHSCORE");
                    highscore = score;
                    preferences.putInteger("highscore", highscore);
                    preferences.flush();
                }
                game.setScreen(new GameOverScreen(game, score, highscore, Constants.EXPERT, hitAudioId));

                batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

                if (Gdx.input.justTouched()) {

                    velocity = Scaler.scale(-30);
                    gameState = 1;
                    startGame();
                    score = 0;
                    scoringTube = 0;

                }

            }


            batch.draw(ball,
                    Gdx.graphics.getWidth() / 3 - ball.getWidth() / 2,
                    birdY,
                    Scaler.scale(ball.getWidth()),
                    Scaler.scale(ball.getHeight()));

            font.draw(batch, String.valueOf(score),
                    Gdx.graphics.getWidth()/2 - font.getSpaceWidth()/2 ,
                    Gdx.graphics.getHeight() - 50);

            birdCircle.set(Gdx.graphics.getWidth() / 3,
                    birdY + Scaler.scale(ball.getHeight()) / 2,
                    Scaler.scale(ball.getWidth()) / 2);

            for (int i = 0; i < numberOfTubes; i++) {

                if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

                    gameState = 2;

                }

            }

            batch.end();

        }

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

    public void startGame() {

        birdY = Gdx.graphics.getHeight() / 2 - ball.getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - Scaler.scale(200));

            Gdx.app.log("OFFSET", String.valueOf(tubeOffset[i]));

            tubeUp[i] = true;

            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }

    }
    void playScoreAudio(){
        coinAudioId = point.play();
    }

    void playWingAudio(){
        if (wingAudioId != -1){
            wing.stop(wingAudioId);
        }
        wingAudioId = wing.play();
    }

    void playHitAudio(){
        hitAudioId = hit.play();
    }
}
