package com.rokonexamples.modifier;

import android.view.MotionEvent;

import com.rokonexamples.modifier.modifiers.ColorModifier;
import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;

public class GameScene extends Scene {

	private FixedBackground background;
	private Sprite bob;
	private ColorModifier modifier;

	public GameScene() {
		super(1, 1);

		setBackground(background = new FixedBackground(Textures.background));

		// Create the Bob sprite
		bob = new Sprite(100, 220, Textures.bob.getWidth(), Textures.bob.getHeight());
		bob.setTexture(Textures.bob);

		// Add the Bob sprite to the first layer.
		add(0, bob);
		
		// And create the modifier.
		modifier = new ColorModifier();
	}

	@Override
	public void onGameLoop() {
	}

	@Override
	public void onTouchUp(float x, float y, MotionEvent event, int pointerCount, int pointerId) {
		// Add your modifier to the sprite.
		bob.addModifier(modifier);
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onReady() {
	}

}
