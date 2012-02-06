package com.rokonexamples.helloworld;

import com.stickycoding.rokon.Texture;
import com.stickycoding.rokon.TextureAtlas;

public class Textures {

	public static TextureAtlas atlas;
	public static Texture background;
	
	public static void load() {
		atlas = new TextureAtlas(); // Create a texture atlas to store your textures.
									// (this is basically just a holder for a set of textures)
		atlas.insert(background = new Texture("background.png")); // Add the background texture to the atlas.
		atlas.complete();
	}
}
