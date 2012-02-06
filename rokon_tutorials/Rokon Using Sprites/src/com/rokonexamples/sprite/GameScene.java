package com.rokonexamples.sprite;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.Time;
import com.stickycoding.rokon.background.FixedBackground;

public class GameScene extends Scene {

	private FixedBackground background;
	private Sprite bob, bob2, bob3;

	public GameScene() {
		super(1, 3);	// Setup the scene to have 1 layer of sprites with a maximum of 3 sprites per layer.
						// (note that the background does NOT count as a sprite)

		setBackground(background = new FixedBackground(Textures.background));

		// Create the Bob sprites.
		bob = new Sprite(100, 220, Textures.bob.getWidth(), Textures.bob.getHeight());
		bob.setTexture(Textures.bob); // Set the actual texture.
		bob2 = new Sprite(100, 180, Textures.bob.getWidth(), Textures.bob.getHeight());
		bob2.setTexture(Textures.bob); // Note that you can use the same texture for multiple sprites.
		bob3 = new Sprite(100, 260, Textures.bob.getWidth(), Textures.bob.getHeight());
		bob3.setTexture(Textures.bob);
		
		// Add the Bob sprites to the first layer.
		add(0, bob);
		add(0, bob2);
		add(0, bob3);
	}

	@Override
	public void onGameLoop() {
		// This is the game loop that is called once every frame.
		// It's where you update all your objects.
		
		// Here we make Bob constantly move to the right.
		bob.x += 1;
		
		// If Bob walks outside the screen, move him back.
		if (bob.x >= MainActivity.GAME_WIDTH)
		{
			bob.x = 0;
		}
		
		// Here we make the 2nd Bob rotate by 2 degrees every frame.
		bob2.rotate(2);
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onReady() {
		// You can also set it to move over a period of time.
		// (if this is called again while it is already moving, the previous movement is canceled)
		// It takes x, y and time in ms.
		bob3.moveTo(450, 100, 5000);
	}

}
