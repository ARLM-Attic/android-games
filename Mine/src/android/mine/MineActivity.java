package android.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class MineActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MineView(this));
    }
    
    @Override
    protected void onDestroy() {
    	  super.onDestroy(); 
    	  System.exit(0);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();   
            System.exit(0); 
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}