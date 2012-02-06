package android.llk;

import com.stickycoding.rokon.Texture;
import com.stickycoding.rokon.TextureAtlas;

public class Textures {
	public static TextureAtlas atlas;
	public static Texture ground, face;
	public static Texture start, about, exit;
	public static Texture grid, llk, select, notice, stop;

	public static void load() {
		atlas = new TextureAtlas();
		atlas.insert(ground = new Texture("ground.jpg"));
		atlas.insert(face = new Texture("face.jpg")); 		
		atlas.insert(start = new Texture("start.png")); 
		atlas.insert(about = new Texture("about.png")); 
		atlas.insert(exit = new Texture("exit.png")); 
		atlas.insert(grid = new Texture("grid.png")); 
		atlas.insert(llk = new Texture("llk.png")); 
		atlas.insert(select = new Texture("select.png")); 
		atlas.insert(notice = new Texture("notice.png")); 
		atlas.insert(stop = new Texture("stop.png")); 		
		atlas.complete();
	}
}
