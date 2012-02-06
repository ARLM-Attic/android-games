package com.rokonexamples.touchinput;

import android.view.MotionEvent;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;

public class GameScene extends Scene {

	private FixedBackground background;
	private Sprite bob;

	public GameScene() {
		super(1, 1);

		setBackground(background = new FixedBackground(Textures.background));

		// Create Sprite
		bob = new Sprite(100, 220, Textures.bob.getWidth(), Textures.bob.getHeight());
		bob.setTexture(Textures.bob);
		
		// Add the Bob sprite to the first layer.
		add(0, bob);
	}

	@Override
	public void onGameLoop() {
	}

	@Override
	public void onTouchDown(float x, float y, MotionEvent event, int pointerCount, int pointerId)
	{
		// This is called when you press down on the screen.
	}
	
	@Override
	public void onTouchMove(float x, float y, MotionEvent event, int pointerCount, int pointerId)
	{
		// This is called when you move your finger over the screen. (ie pretty much every frame if your holding your finger down)
		
		// Here we'll basically just make Bob follow your finger.
		bob.x = x - (Textures.bob.getWidth()/2);
		bob.y = y - (Textures.bob.getHeight()/2);
	}
	
	@Override
	public void onTouchUp(float x, float y, MotionEvent event, int pointerCount, int pointerId)
	{
		// And this is called when you stop pressing.
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
