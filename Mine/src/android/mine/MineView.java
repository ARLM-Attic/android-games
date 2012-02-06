package android.mine;

import java.util.Timer;
import java.util.Random;
import java.util.TimerTask;

import android.util.Log;
import android.view.View;
import android.view.MotionEvent;

import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

public class MineView extends View{
	private static final int OP_OPEN = 0;
	private static final int OP_SIGN = 1;
	private static final int OP_CHECK = 3;
	private static final int BTN_DWN = 0;
	private static final int BTN_WIN = 1;
	private static final int BTN_LSE = 2;
	private static final int BTN_CAR = 3;
	private static final int BTN_SMA = 4;
	
	private static final int frameW = 18;	
	private static final int frameH = 75;	
	private static final int gridSize = 24;	
	
	private static final Random random = new Random();
	private static final int ltColor = Color.argb(255, 128, 128, 128);
	private static final int rbColor = Color.argb(255, 224, 224, 224);
	
	
	private Paint paint;
	private Timer timer;
	private Mine[][] mineMap;
	private Rect start, sign;
	private TimerTask timerTask;
	private Bitmap face, mine, number;	
	private long useTimes, startTimes;	
	private int gameState, opState, mineCount, remainCount;
	private int scrWidth, scrHeight, tileX, tileY, offsetX, offsetY;
	
	public MineView(Context context) {
		super(context);			
		initResource();
		setFocusable(true);
	}
	
	//定时器更新
	public void update() {
		if (canClick()) {
			useTimes = (System.currentTimeMillis() - startTimes)/1000;
			useTimes = (useTimes > 999) ? 999 : useTimes;
			postInvalidate();
		}
	}
	
	public boolean canClick() {
		return (gameState != BTN_WIN && gameState != BTN_LSE);
	}

	@Override
	protected void onDraw(Canvas canvas) {		
		paint.setARGB(255, 255, 255, 255);
		canvas.drawRect(0, 0, scrWidth, scrHeight, paint);
		paint.setARGB(255, 192, 192, 192);
		canvas.drawRect(6, 8, scrWidth-5, scrHeight-5, paint);
		draw3dRect(canvas, 15, 17, scrWidth-15, 63, 3, ltColor, rbColor);
		draw3dRect(canvas, 15, 72, scrWidth-15, scrHeight-15, 3, ltColor, rbColor);
		
		drawNumber(canvas, remainCount, 32, 28);
		drawNumber(canvas, (int)useTimes, scrWidth-frameH, 28);
		drawImageZoom(canvas, mine, scrWidth/2-30, 28, 24, 24, 0, opState*16, 16, 16);
		drawImage(canvas, face, scrWidth/2+6, 28, 0, gameState*24, 24, 24);
		for(int i = 0; i < tileX; i++){
			for(int j = 0; j < tileY; j++){
				int dx = frameW+offsetX+(i*gridSize);
				int dy = frameH+offsetY+(j*gridSize); 
				drawImageZoom(canvas, mine, dx, dy, gridSize, gridSize, 0, mineMap[i][j].getShow()*16, 16, 16);
			}
		}
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		if(start.contains(x, y)){
			onBtnDoer(action);
		}
		else if(sign.contains(x, y)){
			if(action == MotionEvent.ACTION_DOWN){
				opState = (opState == OP_OPEN) ? OP_SIGN : OP_OPEN;
				invalidate();
			}			
		}
		else if(canClick()){
			x = pos2GridX(x);
			y = pos2GridY(y);
			if(x >= 0 && y >=0 && x < tileX && y < tileY){
				onAreaDoer(action, x, y);
			}
		}
		return true;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {		
		initCanvas(right-left, bottom-top);
		super.onLayout(changed, left, top, right, bottom);
	}
	
	//开始按钮处理
	private void onBtnDoer(int action) {	
		if(action == MotionEvent.ACTION_UP){
			gameState = BTN_SMA;
			startMine();
			invalidate();
		}
		else if(action == MotionEvent.ACTION_DOWN){
			gameState = BTN_DWN;
			invalidate();
		}
	}
	
	//点击雷域
	private void onAreaDoer(int action, int x, int y) {		
		if(action == MotionEvent.ACTION_DOWN){
			if(opState == OP_OPEN){
				if(mineMap[x][y].canClick()){
					doOpenMine(x, y);
				}
			}
			else if(opState == OP_SIGN){
				if(mineMap[x][y].canSign()){
					doSignMine(x, y);
				}
			}
		}
	}
	
	//标记雷域
	private void doSignMine(int x, int y) {
		if(mineMap[x][y].sign()){
			remainCount--;
			if(remainCount == 0){
				int right = 0;
				for (int i = 0; i < mineCount; i++) {
					if(mineMap[x-1][y].isMine() && mineMap[x-1][y].hasSign()) {
						right++;
					}
				}
				if(right == mineCount){
					gameState = BTN_WIN;
					killTimer();
				}			
			}
		}
		invalidate();
	}
	
	//打开雷域
	private void doOpenMine(int x, int y) {
		mineMap[x][y].show(true);
		if(mineMap[x][y].isMine()){			
			doGameOver();	
		}
		else{
			if(checkMine(x, y)){
				openMine(x, y);
			}
			invalidate();
		}
	}
	
	//游戏结束
	private void doGameOver() {	
		gameState = BTN_LSE;
		for (int i = 0; i < tileX; i++) {
			for (int j = 0; j < tileY; j++) {
				mineMap[i][j].show(false);
			}
		}
		killTimer();
		invalidate();
	}
	
	//初始化画布属性
	private void initCanvas(int w, int h) {	
		scrWidth = w;
		scrHeight = h;
		w = w-frameW*2;
		h = h-frameH-frameW;
		tileX = w/gridSize;
		tileY = h/gridSize;
		offsetX = (w%gridSize)/2;
		offsetY = (h%gridSize)/2;
		mineCount = tileX + tileY;		
		sign = new Rect(scrWidth/2-30, 28, scrWidth/2-6, 52);
		start = new Rect(scrWidth/2+6, 28, scrWidth/2+30, 52);
		mineMap = new Mine[tileX][tileY];
		startMine();
	}
	
	//初始化雷
	private void startMine() {
		int x, y;		
		for (int i = 0; i < tileX; i++) {
			for (int j = 0; j < tileY; j++) {
				mineMap[i][j] = new Mine();
			}
		}
		for (int i = 0; i < mineCount; i++) {
			do{
				x = random.nextInt(tileX);
				y = random.nextInt(tileY);
			}
			while(mineMap[x][y].isMine());
			mineMap[x][y].setMine(true);
			updateMine(x, y);
		}
		useTimes = 0;
		opState = OP_OPEN;
		gameState = BTN_SMA;
		remainCount = mineCount;
		startTimes = System.currentTimeMillis();
		setTimer(1000);
	}
	
	//更新周围雷数量
	private void updateMine(int x, int y) {
		if(x-1 >= 0){
			mineMap[x-1][y].addMine();
			if(y-1 >= 0){
				mineMap[x-1][y-1].addMine();
			}
			if(y+1 < tileY){
				mineMap[x-1][y+1].addMine();
			}
		}
		if(x+1 < tileX){
			mineMap[x+1][y].addMine();
			if(y-1 >= 0){
				mineMap[x+1][y-1].addMine();
			}
			if(y+1 < tileY){
				mineMap[x+1][y+1].addMine();
			}
		}
		if(y-1 >= 0){
			mineMap[x][y-1].addMine();
		}
		if(y+1 < tileY){
			mineMap[x][y+1].addMine();
		}
	}
	
	//打开周围无雷方块
	private void openMine(int x, int y) {
		if(x-1 >= 0){
			if((!mineMap[x-1][y].isMine()) && mineMap[x-1][y].canClick()) doOpenMine(x-1, y);
			if(y-1 >= 0){
				if((!mineMap[x-1][y-1].isMine()) && mineMap[x-1][y-1].canClick()) doOpenMine(x-1, y-1);
			}
			if(y+1 < tileY){
				if((!mineMap[x-1][y+1].isMine()) && mineMap[x-1][y+1].canClick()) doOpenMine(x-1, y+1);
			}
		}
		if(x+1 < tileX){
			if((!mineMap[x+1][y].isMine()) && mineMap[x+1][y].canClick()) doOpenMine(x+1, y);
			if(y-1 >= 0){
				if((!mineMap[x+1][y-1].isMine()) && mineMap[x+1][y-1].canClick()) doOpenMine(x+1, y-1);
			}
			if(y+1 < tileY){
				if((!mineMap[x+1][y+1].isMine()) && mineMap[x+1][y+1].canClick()) doOpenMine(x+1, y+1);
			}
		}
		if(y-1 >= 0){
			if((!mineMap[x][y-1].isMine()) && mineMap[x][y-1].canClick()) doOpenMine(x, y-1);
		}
		if(y+1 < tileY){
			if((!mineMap[x][y+1].isMine()) && mineMap[x][y+1].canClick()) doOpenMine(x, y+1);
		}
	}
	
	//检查周围雷数量
	private boolean checkMine(int x, int y) {
		int sCount = 0;
		if(x-1 >= 0){
			if(mineMap[x-1][y].isMine() && mineMap[x-1][y].hasSign()) sCount++; 			
			if(y-1 >= 0){
				if(mineMap[x-1][y-1].isMine() && mineMap[x-1][y-1].hasSign()) sCount++; 
			}
			if(y+1 < tileY){
				if(mineMap[x-1][y+1].isMine() && mineMap[x-1][y+1].hasSign()) sCount++;
			}
		}
		if(x+1 < tileX){
			if(mineMap[x+1][y].isMine() && mineMap[x+1][y].hasSign()) sCount++; 
			if(y-1 >= 0){
				if(mineMap[x+1][y-1].isMine() && mineMap[x+1][y-1].hasSign()) sCount++; 
			}
			if(y+1 < tileY){
				if(mineMap[x+1][y+1].isMine() && mineMap[x+1][y+1].hasSign()) sCount++; 
			}
		}
		if(y-1 >= 0){
			if(mineMap[x][y-1].isMine() && mineMap[x][y-1].hasSign()) sCount++;
		}
		if(y+1 < tileY){
			if(mineMap[x][y+1].isMine() && mineMap[x][y+1].hasSign()) sCount++;
		}
		return sCount == mineMap[x][y].getMineCount();
	}
	
	//初始化资源
	private void initResource() {
		paint = new Paint();	
		timer = new Timer();
		Resources r = this.getContext().getResources();
		face = BitmapFactory.decodeResource(r, R.drawable.face);
		mine = BitmapFactory.decodeResource(r, R.drawable.mine);
		number = BitmapFactory.decodeResource(r, R.drawable.number);
	}
	
	//屏幕坐标转方块坐标
	private int pos2GridX(int dx) {
		dx = dx-frameW-offsetX;
		return dx/gridSize;
	}
	
	//屏幕坐标转方块坐标
	private int pos2GridY(int dy) {
		dy = dy-frameH-offsetY;
		return dy/gridSize;		
	}
	
	//设置定时器
	private void setTimer(int time){
		if(timer != null){
			killTimer();
			timerTask = new TimerTask(){
				public void run(){
					MineView.this.update();
				}
			};
			timer.schedule(timerTask, 0, time);
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
	
	//绘制数字
	private void drawNumber(Canvas canvas, int num, int dx, int dy) {
		drawImage(canvas, number, dx, dy, 0, (253-23*((num%1000)/100)), 13, 23);
		drawImage(canvas, number, dx+13, dy, 0, (253-23*((num%100)/10)), 13, 23);
		drawImage(canvas, number, dx+26, dy, 0, (253-23*(num%10)), 13, 23);
	}
	
	//绘制缩放图像
	private void drawImageZoom(Canvas canvas, Bitmap bmp, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh) {
		Rect src = new Rect(sx, sy, sx + sw, sy + sh);
        Rect dst = new Rect(dx, dy, dx + dw, dy + dh);
        canvas.drawBitmap(bmp, src, dst, paint);
        src = dst = null;
	}
	
	//绘制3D矩形框
	private void draw3dRect(Canvas canvas, int l, int t, int r, int b, int w, int ctl, int crb) {
		paint.setColor(ctl);
		canvas.drawRect(l, t, l+w, b-w, paint);
		canvas.drawRect(l, t, r-w, t+w, paint);	
		paint.setColor(crb);
		canvas.drawRect(l, b-w, r-w, b, paint);
		canvas.drawRect(r-w, t, r, b, paint);
	}
}
