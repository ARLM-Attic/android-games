package android.llk;

import java.util.Timer;
import java.util.Random;
import java.util.TimerTask;
import java.util.Vector;

import android.util.Log;
import android.view.View;
import android.view.MotionEvent;

import android.graphics.Rect;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Context;
import android.content.res.Resources;

public class GameView extends View{
	private static final int STATE_WAIT = 0;
	private static final int STATE_MENU= 1;
	private static final int STATE_RUN = 2;
	private static final int STATE_ABOUT = 3;
	
	private static final int gridX = 34;
	private static final int gridY = 40;
	private static final int illegal = -1;
	private static final int gridType = 10;
	
	private static final Random random = new Random();
	
	private Paint paint;
	private Timer timer;
	private TimerTask timerTask;
	private Rect start, about, exit, back, notice;
	
	private int[][] llkMap;
	private Vector<Point> vector = new Vector<Point>();
	private int gameState, selX, selY, selK, selL, remain;
	private int scrWidth, scrHeight, tileX, tileY, offsetX, offsetY;
	private Bitmap face, ground, bstart, babout, bexit, bback, bnotice, grid, llk, select;	
	
	public GameView(Context context) {
		super(context);	
		initResource();
		setFocusable(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(gameState == STATE_WAIT){
			drawImageZoom(canvas, face, 0, 0, scrWidth, scrHeight, 0, 0, face.getWidth(), face.getHeight());
		}
		else if(gameState == STATE_MENU){
			drawImageZoom(canvas, face, 0, 0, scrWidth, scrHeight, 0, 0, face.getWidth(), face.getHeight());
			drawImage(canvas, bstart, start.left, start.top, 0, 0, start.width(), start.height());
			drawImage(canvas, babout, about.left, about.top, 0, 0, about.width(), about.height());
			drawImage(canvas, bexit, exit.left, exit.top, 0, 0, exit.width(), exit.height());
		}
		else if(gameState == STATE_RUN){
			drawImageZoom(canvas, ground, 0, 0, scrWidth, scrHeight, 0, 0, face.getWidth(), face.getHeight());
			drawImage(canvas, bnotice, notice.left, notice.top, 0, 0, notice.width(), notice.height());
			drawImage(canvas, bback, back.left, back.top, 0, 0, back.width(), back.height());
			for(int i = 0; i < tileX; i++){
				for(int j = 0; j < tileY; j++){
					if(llkMap[i][j] != illegal){
						int dx = offsetX+(i*gridX);
						int dy = offsetY+(j*gridY);
						drawImageZoom(canvas, grid, dx, dy, 39, 45, 0, 0, grid.getWidth(), grid.getHeight()/6);
						if((selX == i && selY == j)||(selK == i && selL == j)){
							drawImageZoom(canvas, select, dx, dy, gridX, gridY, 0, 0, select.getWidth(), select.getHeight());
						}
						drawImageZoom(canvas, llk, dx+2, dy+5, 30, 30, 0, llkMap[i][j]*(llk.getHeight()/42), llk.getWidth()/3, llk.getHeight()/42);
					}
				}
			}
			if(vector.size() > 1){
				Point f = vector.get(0);				
				for(int i = 1; i < vector.size(); i++){
					Point s = vector.get(i);
					canvas.drawLine(offsetX+f.x*gridX+gridX/2, offsetY+f.y*gridY+gridY/2, offsetX+s.x*gridX+gridX/2, offsetY+s.y*gridY+gridY/2, paint);
					f = s;
				}
			}
		}
		else if(gameState == STATE_ABOUT){
			drawImageZoom(canvas, ground, 0, 0, scrWidth, scrHeight, 0, 0, face.getWidth(), face.getHeight());
			drawImage(canvas, bback, back.left, back.top, 0, 0, back.width(), back.height());
		}
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		if (action == MotionEvent.ACTION_DOWN){
			if(gameState == STATE_WAIT){
				gameState = STATE_MENU;
				invalidate();
			}
			else if(gameState == STATE_MENU){
				if (start.contains(x, y)){
					startGame();
				}
				else if (about.contains(x, y)){
					gameState = STATE_ABOUT;
					invalidate();
				}
				else if (exit.contains(x, y)){
					System.exit(0);
				}
			}
			else if(gameState == STATE_RUN){
				if (notice.contains(x, y)){
					doNotice();				
				}
				else if (back.contains(x, y)){
					gameState = STATE_MENU;
					invalidate();
				}
				else{
					doClickGrid(x, y);
				}
			}
			else if(gameState == STATE_ABOUT){
				if (back.contains(x, y)){
					gameState = STATE_MENU;
					invalidate();
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {		
		initCanvas(right-left, bottom-top);
		super.onLayout(changed, left, top, right, bottom);
	}
	
	private void startGame(){
		vector.clear();	
		gameState = STATE_RUN;
		remain = tileX * tileY;
		selX = selY = selK = selL = illegal;
		int len = tileX*tileY/2;
		for(int i = 0; i < len; i++){
			int value = random.nextInt(gridType);
			llkMap[(2*i)%tileX][(int)((2*i)/tileX)] = value;
			llkMap[(2*i+1)%tileX][(int)((2*i+1)/tileX)] = value;
		}
		fixCards();
		invalidate();
	}
	
	private void fixCards(){
		for (int i = 0; i < 500; i++){
			int x1 = random.nextInt(tileX);
			int x2 = random.nextInt(tileX);
			int y1 = random.nextInt(tileY);
			int y2 = random.nextInt(tileY);
			if (llkMap[x1][y1] != illegal && llkMap[x2][y2] != illegal)
			{
				int temp = llkMap[x1][y1];
				llkMap[x1][y1] = llkMap[x2][y2];
				llkMap[x2][y2] = temp;
			}
		}
	}
	
	private void doNotice(){
		vector.clear();
		Log.i("doNotice1", "start");
		for(int i = 0; i < tileX; i++){
			for(int j = 0; j < tileY; j++){
				if(llkMap[i][j] != illegal){					
					for(int k = 0; k < tileX; k++){
						for(int l = 0; l < tileY; l++){
							if((k!=i || l!=j) && llkMap[k][l] != illegal){
								if(llkMap[i][j] == llkMap[k][l] && canConnect(k, l, i, j)){
									selX = i; selY = j;
									selK = k; selL = l;
									invalidate();
									return;
								}
							}
						}
					}
				}
			}
		}	
		Log.i("doNotice1", "stop");
	}
	
	private void doClickGrid(int x, int y){
		int sx = pos2GridX(x);
		int sy = pos2GridY(y);		
		if(sx >= 0 && sx < tileX && sy >= 0 && sy < tileY && llkMap[sx][sy] != illegal){
			vector.clear();
			if(selX < 0 || selY < 0){
				selX = sx; selY = sy;
			}
			else if(selX == sx && selY == sy){
				selX = selY = illegal;
			}
			else{
				if(llkMap[sx][sy] == llkMap[selX][selY] && canConnect(sx, sy, selX, selY)){
					remain -= 2;
					llkMap[sx][sy] = illegal;
					llkMap[selX][selY] = illegal;
					if(remain == 0){
						gameState = STATE_MENU;
					}
				}
				selX = selY = illegal;
			}
			selK = selL = illegal;
			invalidate();
		}
	}
	
	//初始化画布属性
	private void initCanvas(int w, int h) {	
		scrWidth = w;
		scrHeight = h;
		tileX = w/gridX-1;
		tileY = (h-32)/gridY-1;
		offsetX = (w%gridX+gridX)/2;
		offsetY = 32+((h-32)%gridY+gridY)/2;		
		if((tileX*tileY) % 2 != 0){
			tileX--;
			offsetX += gridX/2;
		}			
		gameState = STATE_WAIT;				
		llkMap = new int[tileX][tileY];
		
		int mWidth = bstart.getWidth();
		int mHeight = bstart.getHeight();
		back = new Rect(w-bback.getWidth(), 0, w, bback.getHeight());
		notice = new Rect(0, 0, bnotice.getWidth(), bnotice.getHeight());
		about = new Rect((w-mWidth)/2, (h-mHeight)/2, (w+mWidth)/2, (h+mHeight)/2);
		start = new Rect((w-mWidth)/2, (h-mHeight)/2-mHeight-20, (w+mWidth)/2, (h+mHeight)/2-mHeight-20);
		exit = new Rect((w-mWidth)/2, (h-mHeight)/2+mHeight+20, (w+mWidth)/2, (h+mHeight)/2+mHeight+20);		
	//	setTimer(1000);
	}
	
	//初始化资源
	private void initResource() {			
		timer = new Timer();
		paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setColor(0xffff0000);
		Resources r = this.getContext().getResources();
		llk = BitmapFactory.decodeResource(r, R.drawable.llk);
		grid = BitmapFactory.decodeResource(r, R.drawable.grid);
		face = BitmapFactory.decodeResource(r, R.drawable.face);
		bexit = BitmapFactory.decodeResource(r, R.drawable.exit);
		bback = BitmapFactory.decodeResource(r, R.drawable.stop);		
		bstart = BitmapFactory.decodeResource(r, R.drawable.start);
		babout = BitmapFactory.decodeResource(r, R.drawable.about);		
		ground = BitmapFactory.decodeResource(r, R.drawable.ground);
		select = BitmapFactory.decodeResource(r, R.drawable.select);
		bnotice = BitmapFactory.decodeResource(r, R.drawable.notice);
	}
	
	//屏幕坐标转方块坐标
	private int pos2GridX(int dx) {
		dx = dx-offsetX;
		return dx/gridX;
	}
	
	//屏幕坐标转方块坐标
	private int pos2GridY(int dy) {
		dy = dy-offsetY;
		return dy/gridY;		
	}
	
	//设置定时器
	private void setTimer(int time){
		if(timer != null){
			killTimer();
			timerTask = new TimerTask(){
				public void run(){
					GameView.this.update();
				}
			};
			timer.schedule(timerTask, 0, time);
		}
	}
	
	//定时器更新
	public void update(){
		if(gameState == STATE_RUN){
			postInvalidate();
		}
	}
	
	//关闭定时器
	private void killTimer(){
		if(timerTask != null){
			timerTask.cancel();
	   	}
	}

	//绘制图像接口
	private void drawImage(Canvas canvas, Bitmap bmp, int dx, int dy, int sx, int sy, int w, int h) {
		drawImageZoom(canvas, bmp, dx, dy, w, h, sx, sy, w, h);
	}
	
	//绘制缩放图像
	private void drawImageZoom(Canvas canvas, Bitmap bmp, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh) {
		Rect src = new Rect(sx, sy, sx + sw, sy + sh);
        Rect dst = new Rect(dx, dy, dx + dw, dy + dh);
        canvas.drawBitmap(bmp, src, dst, paint);
        src = dst = null;
	}
	
	private boolean canConnect(int x1, int y1, int x2, int y2){
		if(x1 == x2 && checkXLink(x1, y1, y2)){
			return true;
		}
		if(y1 == y2 && checkYLink(y1, x1, x2)){
			return true;
		}
		if(checkXYLink(x1, x2, y1, y2)){
			return true;
		}
		return false;
	}
	
	private boolean canXLink(int x, int y1, int y2){
		if(y1 > y2){
			int temp = y1; y1 = y2; y2 = temp;
		}
		for(int i = y1+1; i <= y2; i++){
			if(i == y2) {
				return true;
			}
			if(llkMap[x][i] != illegal){
				return false;
			}
		}
		return false;
	}
	
	private boolean canYLink(int y, int x1, int x2){
		if(x1 > x2){
			int temp = x1; x1 = x2; x2 = temp;
		}
		for(int i = x1+1; i <= x2; i++){
			if(i == x2) {
				return true;
			}
			if(llkMap[i][y] != illegal){
				return false;
			}
		}		
		return false;
	}
	
	private boolean checkXLink(int x, int y1, int y2){
		if(canXLink(x, y1, y2)){
			vector.add(new Point(x, y1));
			vector.add(new Point(x, y2));
			return true;
		}
		for(int i = x-1; i >= -1; i--){
			if(i == -1){
				vector.add(new Point(x, y1));
				vector.add(new Point(-1, y1));
				vector.add(new Point(-1, y2));
				vector.add(new Point(x, y2));
				return true;
			}
			if(llkMap[i][y1] != illegal || llkMap[i][y2] != illegal){
				break;
			}
			if(canXLink(i, y1, y2)){
				vector.add(new Point(x, y1));
				vector.add(new Point(i, y1));
				vector.add(new Point(i, y2));
				vector.add(new Point(x, y2));
				return true;
			}
		}
		for(int i = x+1; i <= tileX; i++){
			if(i == tileX){
				vector.add(new Point(x, y1));
				vector.add(new Point(tileX, y1));
				vector.add(new Point(tileX, y2));
				vector.add(new Point(x, y2));
				return true;
			}
			if(llkMap[i][y1] != illegal || llkMap[i][y2] != illegal){
				break;
			}
			if(canXLink(i, y1, y2)){
				vector.add(new Point(x, y1));
				vector.add(new Point(i, y1));
				vector.add(new Point(i, y2));
				vector.add(new Point(x, y2));
				return true;
			}
		}
		return false;
	}
	
	private boolean checkYLink(int y, int x1, int x2){
		if(canYLink(y, x1, x2)){
			vector.add(new Point(x1, y));
			vector.add(new Point(x2, y));
			return true;
		}
		for(int i = y-1; i >= -1; i--){
			if(i == -1){
				vector.add(new Point(x1, y));
				vector.add(new Point(x1, -1));
				vector.add(new Point(x2, -1));
				vector.add(new Point(x2, y));
				return true;
			}
			if(llkMap[x1][i] != illegal || llkMap[x2][i] != illegal){
				break;
			}
			if(canYLink(i, x1, x2)){
				vector.add(new Point(x1, y));
				vector.add(new Point(x1, i));
				vector.add(new Point(x2, i));
				vector.add(new Point(x2, y));
				return true;
			}
		}
		for(int i = y+1; i <= tileY; i++){
			if(i == tileY){
				vector.add(new Point(x1, y));
				vector.add(new Point(x1, tileY));
				vector.add(new Point(x2, tileY));
				vector.add(new Point(x2, y));
				return true;
			}
			if(llkMap[x1][i] != illegal || llkMap[x2][i] != illegal){
				break;
			}
			if(canYLink(i, x1, x2)){
				vector.add(new Point(x1, y));
				vector.add(new Point(x1, i));
				vector.add(new Point(x2, i));
				vector.add(new Point(x2, y));
				return true;
			}
		}
		return false;
	}
	
	private boolean checkXYLink(int x1, int x2, int y1, int y2){
		if(llkMap[x2][y1] == illegal && canYLink(y1, x1, x2) && canXLink(x2, y1, y2)){
			vector.add(new Point(x1, y1));
			vector.add(new Point(x2, y1));
			vector.add(new Point(x2, y2));
			return true;
		}
		if(llkMap[x1][y2] == illegal && canYLink(y2, x1, x2) && canXLink(x1, y1, y2)){
			vector.add(new Point(x1, y1));
			vector.add(new Point(x1, y2));
			vector.add(new Point(x2, y2));
			return true;
		}
		for(int i = 0; i < tileX; i++){
			if(i != x1 && i != x2){
				if(llkMap[i][y1] == illegal && llkMap[i][y2] == illegal && canYLink(y1, x1, i) && canYLink(y2, x2, i) && canXLink(i, y1, y2)){
					vector.add(new Point(x1, y1));
					vector.add(new Point(i, y1));
					vector.add(new Point(i, y2));
					vector.add(new Point(x2, y2));
					return true;
				}
			}
		}
		for(int i = 0; i < tileY; i++){
			if(i != y1 && i != y2){
				if(llkMap[x1][i] == illegal && llkMap[x2][i] == illegal && canXLink(x1, y1, i) && canXLink(x2, y2, i) && canYLink(i, x1, x2)){
					vector.add(new Point(x1, y1));
					vector.add(new Point(x1, i));
					vector.add(new Point(x2, i));
					vector.add(new Point(x2, y2));
					return true;
				}
			}
		}
		if((llkMap[x1][0]==illegal||y1 == 0) && (llkMap[x2][0]==illegal||y2==0) 
				&& (y1==0||canXLink(x1, y1, 0)) && (y2==0||canXLink(x2, y2, 0))){
			vector.add(new Point(x1, y1));
			vector.add(new Point(x1, -1));
			vector.add(new Point(x2, -1));
			vector.add(new Point(x2, y2));
			return true;
		}
		if((llkMap[x1][tileY-1]==illegal||y1 ==tileY-1) && (llkMap[x2][tileY-1]==illegal||y2==tileY-1) 
				&& (y1==tileY-1||canXLink(x1, y1, tileY-1)) && (y2==tileY-1||canXLink(x2, y2, tileY-1))){
			vector.add(new Point(x1, y1));
			vector.add(new Point(x1, tileY));
			vector.add(new Point(x2, tileY));
			vector.add(new Point(x2, y2));
			return true;
		}
		if((llkMap[0][y1]==illegal||x1==0) && (llkMap[0][y2]==illegal||x2==0) 
				&& (x1==0||canYLink(y1, x1, 0)) && (x2==0||canYLink(y2, x2, 0))){
			vector.add(new Point(x1, y1));
			vector.add(new Point(0, y1));
			vector.add(new Point(0, y2));
			vector.add(new Point(x2, y2));
			return true;
		}
		if((llkMap[tileX-1][y1]==illegal||x1==tileX-1) && (llkMap[tileX-1][y2]==illegal||x2==tileX-1) 
				&& (x1==tileX-1||canYLink(y1, x1, tileX-1)) && (x2==tileX-1||canYLink(y2, x2, tileX-1))){
			vector.add(new Point(x1, y1));
			vector.add(new Point(tileX, y1));
			vector.add(new Point(tileX, y2));
			vector.add(new Point(x2, y2));
			return true;
		}
		return false;
	}
}
