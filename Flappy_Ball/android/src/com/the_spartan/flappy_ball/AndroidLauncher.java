package com.the_spartan.flappy_ball;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.fitness.data.Device;

public class AndroidLauncher extends android.support.v4.app.FragmentActivity implements com.badlogic.gdx.backends.android.AndroidFragmentApplication.Callbacks {

    private static final String TAG = "AndroidLauncher";
    AdView adView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        setContentView(R.layout.main_layout);

		GameFragment fragment = new GameFragment();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.GameView, fragment);
        tr.commit();

        setAdMob();
	}

	@Override
	public void exit(){

	}

	private void setAdMob(){
        adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();

        adView.loadAd(adRequest);
    }

}
