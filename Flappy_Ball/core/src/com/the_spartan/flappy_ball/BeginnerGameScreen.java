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

public class BeginnerGameScreen implements Screen {

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
    float gap;
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

    float tubeSpeed = 2;
    float lastScore = 0;
    float metrics;
    long hitAudioId;
    long wingAudioId = -1;
    long coinAudioId = -1;

    public BeginnerGameScreen(Game game) {

        metrics = Gdx.graphics.getDensity();
        gap = Scaler.scale(380);
        //    gap = 400 * metrics * 0.5f;
//        gravity *= metrics * 0.5f;
//        tubeSpeed *= metrics;
//        tubeVelocity *= metrics * 0.5f;
        gravity = Scaler.scale(gravity);
        tubeVelocity = Scaler.scale(tubeVelocity);

        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameOver.png");
        birdCircle = new Circle();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(metrics);

        ball = new Texture("ball.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        wing = Gdx.audio.newSound(Gdx.files.internal("wing.wav"));
        point = Gdx.audio.newSound(Gdx.files.internal("point.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - Scaler.scale(100);
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 5;
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
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            Gdx.app.log("BIRDY", "" + birdY);
            Gdx.app.log("HEIGHT", "" + Gdx.graphics.getHeight());

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
                velocity = -27 * metrics * 0.5f;
                playWingAudio();
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -Scaler.scale(topTube.getWidth())) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - Scaler.scale(200));
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                if (score - lastScore == 5) {
                    game.pause();
                    lastScore = score;
                    tubeSpeed += Scaler.scale(0.5f);
                }

//                if (tubeUp[i]) {
//                    tubeOffset[i] += tubeSpeed;
//                } else {
//                    tubeOffset[i] -= tubeSpeed;
//                }

                if ((Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]) >= Scaler.scale(Gdx.graphics.getHeight()) - random.nextInt(150)) {
                    tubeUp[i] = false;
                } else if ((Scaler.scale(Gdx.graphics.getHeight()) / 2 - gap / 2 + tubeOffset[i]) <= random.nextInt(150)) {
                    tubeUp[i] = true;
                }

                batch.draw(topTube,
                        tubeX[i],
                        Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
                        Scaler.scale(topTube.getWidth()),
                        Scaler.scale(topTube.getHeight()));
                batch.draw(bottomTube,
                        tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - Scaler.scale(bottomTube.getHeight()) + tubeOffset[i],
                        Scaler.scale(bottomTube.getWidth()),
                        Scaler.scale(bottomTube.getHeight()));

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }


            if (birdY > 0 && birdY < Gdx.graphics.getHeight()) {

                velocity = velocity + gravity;
                birdY -= velocity;

            }
     /*       else if (birdY > Gdx.graphics.getHeight()){

                gameState = 2;

            }  */

            else {

                gameState = 2;

            }

        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {

                velocity = Scaler.scale(-27);
                gameState = 1;


            }

        } else if (gameState == 2) {
            playHitAudio();
            if(coinAudioId != -1){
                point.stop(coinAudioId);
            }
            preferences = Gdx.app.getPreferences("beginnerHighscorePreference");
            highscore = preferences.getInteger("highscore", 0);
            Gdx.app.log("SCORE", String.valueOf(highscore));
            if (score > highscore) {
//                Gdx.app.log("SCORE", "NEW HIGHSCORE");
                highscore = score;
                preferences.putInteger("highscore", highscore);
                preferences.flush();
            }
            game.setScreen(new GameOverScreen(game, score, highscore, Constants.BEGINNER, hitAudioId));

            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

            if (Gdx.input.justTouched()) {

                velocity = -30;
                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                //	velocity = 0;


            }

        }


        batch.draw(ball,
                Gdx.graphics.getWidth() / 3 - ball.getWidth() / 2,
                birdY,
                Scaler.scale(ball.getWidth()),
                Scaler.scale(ball.getHeight()));

        font.draw(batch,
                String.valueOf(score),
                Gdx.graphics.getWidth() / 2 - font.getSpaceWidth() / 2,
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

    private void startGame() {

        birdY = Gdx.graphics.getHeight() / 2 - ball.getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            Gdx.app.log("OFFSET", String.valueOf(tubeOffset[i]));

            tubeUp[i] = true;

            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }
    }

    void playScoreAudio() {

        coinAudioId = point.play();
    }

    void playWingAudio() {
        if (wingAudioId != -1){
            wing.stop(wingAudioId);
        }

        wingAudioId = wing.play();
    }

    void playHitAudio() {
        hitAudioId = hit.play();
    }
}
