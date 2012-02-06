package com.rokonexamples.modifier.modifiers;

import com.stickycoding.rokon.Modifier;
import com.stickycoding.rokon.Sprite;

public class ColorModifier extends Modifier {
	
	private float color;
	
	@Override
	public void onStart(Sprite sprite) {
		// This is called when the modifier is set to start.
		color = 0;
	}
	
	@Override
	public void onUpdate(Sprite sprite) {
		// This is called every frame so this is where you will 'modify' your sprite.
		
		// Here you can do anything to the sprite, like move it or whatever.
		// But we will just do a simple color modification.
		sprite.setRGB(1, color, color);
		color += 0.1;
		
		// When the sprite's original colors has been restored, end it.
		if (color >= 1)
			end();
	}

	@Override
	public void onEnd(Sprite sprite) {
		// After you have called 'end()' the modifier will be removed from the sprite and this will be called.
		
		// I do this to make sure that the sprite is returned to normal afterwards.
		sprite.setRGB(1, 1, 1);
	}

}