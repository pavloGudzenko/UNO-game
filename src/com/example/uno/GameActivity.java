package com.example.uno;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameView gameView = new GameView(this);
		gameView.setKeepScreenOn(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags
		(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(gameView);
		gameView.setBackgroundResource(R.drawable.card_background);
	}

}
