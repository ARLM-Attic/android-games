package com.rokonexamples.helloworld;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;

public class GameScene extends Scene {

	private FixedBackground background;

	public GameScene() {
		super();
		
		// Create a fixed background (ie a non-changing background) with the texture loaded in Textures.load().
		setBackground(background = new FixedBackground(Textures.background));
	}

	@Override
	public void onGameLoop() {
		// This is the game loop that is called once every frame.
	}

	@Override
	public void onPause() {
		// This is called when the game is hidden. (ie when the user switches to another app without turning this one off)
		// (this should be used to pause the game logic so the user doesn't miss anything while he/she's gone)
	}

	@Override
	public void onResume() {
		// And when the user return to this app.
	}

	@Override
	public void onReady() {
		// This is called when the scene has been successfully created and is ready to be used.
	}

}
