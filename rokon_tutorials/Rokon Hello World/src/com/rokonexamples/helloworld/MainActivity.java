package com.rokonexamples.helloworld;

import com.stickycoding.rokon.DrawPriority;
import com.stickycoding.rokon.RokonActivity;

public class MainActivity extends RokonActivity {

	public static final float GAME_WIDTH = 480f;
	public static final float GAME_HEIGHT = 320f;

	private GameScene scene;

	public void onCreate() {
		debugMode();		// This basically makes it print debugging stuffs such as FPS, and your own Debug.print() calls.
		forceFullscreen();	// Force the game to become fullscreen.
		forceLandscape();	// Force the game to be in landscape mode. (Can be replaced by forcePortrait())
							// Should also be backed up by setting 'android:screenOrientation="landscape"' in the manifest.
		setGameSize(GAME_WIDTH, GAME_HEIGHT);		// Set the width and height of the game.
		setDrawPriority(DrawPriority.PRIORITY_VBO);	// Makes the engine use VBO's if it can (if the device supports it).
													// This is good because drawing VBO's is faster than normal rendering.
		setGraphicsPath("textures/");	// Set the path to where you have all the graphics in your game. (in this case 'assets/textures/')
		createEngine();					// And then we create the engine. (duh)
	}
	
	public void onLoadComplete() {
		// This is called when the engine has been successfully created.
		
		Textures.load(); // Load the textures we're going to use.
		setScene(scene = new GameScene());	// And setup the main scene.
											// Later you might want to use more than one scene.
											// (for example, one for the game and one for the menu)
	}

}
