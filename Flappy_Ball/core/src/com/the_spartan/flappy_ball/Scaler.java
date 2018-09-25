package com.the_spartan.flappy_ball;

import com.badlogic.gdx.Gdx;

public class Scaler {
    public static float scale(float x){
        float metrics = Gdx.graphics.getDensity() * 0.5f;
        return x * metrics;
    }
}
