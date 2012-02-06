package android.mine;

enum SHOW_STATE {
	SHOW_NON_UP,
	SHOW_SGN_UP,
	SHOW_QUE_UP,
	SHOW_DIE_DN,
	SHOW_WRO_DN,
	SHOW_MIN_DN,
	SHOW_QUE_DN,
	SHOW_EIG_DN,
	SHOW_SIV_DN,
	SHOW_SIX_DN,
	SHOW_FIV_DN,
	SHOW_FOU_DN,
	SHOW_THR_DN,
	SHOW_TWO_DN,
	SHOW_ONE_DN,
	SHOW_NON_DN,
}

public class Mine {	
	private int mState = 0;
	private int mCount = 0;
	private SHOW_STATE mShow = SHOW_STATE.SHOW_NON_UP; 
	
	public Mine() {		
	}
	
	public boolean isMine(){
		return mState == 1;
	}
	
	public boolean hasSign(){
		return mShow == SHOW_STATE.SHOW_SGN_UP;
	}
	
	public int getShow(){
		switch(mShow){
		case SHOW_NON_UP: return 0;
		case SHOW_SGN_UP: return 1;
		case SHOW_QUE_UP: return 2;
		case SHOW_DIE_DN: return 3;
		case SHOW_WRO_DN: return 4;
		case SHOW_MIN_DN: return 5;
		case SHOW_QUE_DN: return 6;
		case SHOW_EIG_DN: return 7;
		case SHOW_SIV_DN: return 8;
		case SHOW_SIX_DN: return 9;
		case SHOW_FIV_DN: return 10;
		case SHOW_FOU_DN: return 11;
		case SHOW_THR_DN: return 12;
		case SHOW_TWO_DN: return 13;
		case SHOW_ONE_DN: return 14;
		case SHOW_NON_DN: return 15;
		default: return 0;
		}
	}	
	
	public boolean canClick(){
		if(mShow == SHOW_STATE.SHOW_NON_UP || mShow == SHOW_STATE.SHOW_QUE_UP){
			return true;
		}
		return false;
	}
	
	public boolean canSign(){
		return getShow() < 3;
	}
	
	public int getMineCount(){
		return mCount;
	}
	
	public boolean sign(){
		if(mShow == SHOW_STATE.SHOW_SGN_UP){	
			mShow = SHOW_STATE.SHOW_QUE_UP;
		}
		else if(mShow == SHOW_STATE.SHOW_QUE_UP){	
			mShow = SHOW_STATE.SHOW_NON_UP;
		}
		else if(mShow == SHOW_STATE.SHOW_NON_UP){	
			mShow = SHOW_STATE.SHOW_SGN_UP;
			return true;
		}
		return false;
	}
	
	public void show(boolean mouse){
		if(canSign()){
			if(mState == 1){
				mShow = mouse ? SHOW_STATE.SHOW_DIE_DN : SHOW_STATE.SHOW_MIN_DN; 
			}
			else{
				if(mShow != SHOW_STATE.SHOW_SGN_UP){			
					switch(mCount){
					case 1: mShow = SHOW_STATE.SHOW_ONE_DN; break;
					case 2: mShow = SHOW_STATE.SHOW_TWO_DN; break;
					case 3: mShow = SHOW_STATE.SHOW_THR_DN; break;
					case 4: mShow = SHOW_STATE.SHOW_FOU_DN; break;
					case 5: mShow = SHOW_STATE.SHOW_FIV_DN; break;
					case 6: mShow = SHOW_STATE.SHOW_SIX_DN; break;
					case 7: mShow = SHOW_STATE.SHOW_SIV_DN; break;
					case 8: mShow = SHOW_STATE.SHOW_EIG_DN; break;
					default: mShow = SHOW_STATE.SHOW_NON_DN; break;
					}
				}
				else{
					mShow = SHOW_STATE.SHOW_WRO_DN;
				}
			}
		}		
	}
	
	public void addMine(){
		if(mState == 0){
			mCount++;
		}
	}
	
	public void setMine(boolean bState){
		mState = bState ? 1 :0;
	}
}
