package jMinesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class FieldControl extends Observable{

	
	private int x,y;
//	private Number num;
	private List<FieldListener> listeners;
	private FieldState state;
	
	public FieldControl(int x, int y,FieldListener mineField){
		this.x = x;
		this.y = y;
		this.listeners = new ArrayList<FieldListener>();
		if(mineField!=null) listeners.add(mineField);
		this.state = new FieldState();
		this.reset();
	}
	
	public void addListener(FieldListener mineField){
		listeners.add(mineField);
	}
	public void removeListener(FieldListener mineField){
		listeners.remove(mineField);
	}
	public void fireEvent(){
		for(FieldListener listener : listeners)
			listener.fieldExploded(new FieldEvent(this));
	}
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setBorder(){
		state.isBorder = true;
		state.isFullPressed = true;
	}
	public void setFlag(){
//		this.num = Number.FLAG;
		if(!state.isFullPressed && !state.isHalfPressed){
			state.flagSet = true;
			state.qMarkSet = false;
			state.numVisible = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void setQMark(){
		if(!state.isFullPressed && !state.isHalfPressed){
	//		this.num = Number.QUESTION;
			state.flagSet = false;
			state.qMarkSet = true;
			state.numVisible = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void setZero(){
//		this.num = Number.ZERO;
		state.flagSet = false;
		state.qMarkSet = false;
		state.numVisible = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void deleteFlagOrMark(){
//		this.num = Number.ZERO;
		state.flagSet = false;
		state.qMarkSet = false;
		state.numVisible = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean setOffMine(){
		if(state.isMine){
			setChanged();
			notifyObservers();
		}
		return state.isMine;
	}
	
	public void setMine(){
		state.num = Number.MINE;
		state.isMine = true;
	}
	
	public boolean fullPress(boolean fireEvent){
		if(!state.flagSet && !state.isFullPressed){
			if(fireEvent & state.isMine)
				fireEvent();
			state.isFullPressed = true;
			state.isHalfPressed = false;
			state.numVisible = true;
			state.qMarkSet = false;
			this.setChanged();
			this.notifyObservers();
			return true;
		}
		return false;
	}
	
	public boolean halfPress(){
		if(!state.flagSet && !state.isFullPressed){
			state.isHalfPressed = true;
			state.isFullPressed = false;
			this.setChanged();
			this.notifyObservers();
		}
		return !state.flagSet && !state.isFullPressed;
	}
	
	public boolean unPress(){
		if(!state.isFullPressed && state.isHalfPressed){
			state.isFullPressed = false;
			state.isHalfPressed = false;
			this.setChanged();
			this.notifyObservers();
			return true;
		}
		return false;
	}
	
	public void hoverOn(){
		if(!state.isPressed()){
			state.isHovered = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void hoverOff(){
		state.isHovered = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setNum(Number num){ 
		this.state.num = num;
//		numVisible = true;
//		this.setChanged();
//		this.notifyObservers();
	}
	
	public void setNum(int num){
		if(num==FieldGenerator.MINE_BLOCK){
			state.num = Number.MINE;
			state.isMine = true;
		}else{
			state.isMine = false;
			num%=9;
			state.num = Number.values()[num];
		}
//		numVisible = true;
//		this.setChanged();
//		this.notifyObservers();
	}
	
	public void setNumVisible(){
		state.numVisible = true;
		this.setChanged();
		this.notifyObservers();
	}
	
	public Number getNum(){
		return state.num;
	}
	
	public FieldState getState(){
		return state;
	}
	public void changeRightClick(){
		if(!state.isFullPressed && !state.isHalfPressed){
			if(state.flagSet)
				setQMark();
			else if(state.qMarkSet)
				setZero();
			else setFlag();
		}
	}
	public void reset(){
		state.reset();
	}
	
	class FieldState{
		private boolean isHalfPressed;
		private boolean isFullPressed;
		private boolean isHovered;
		private boolean numVisible;
		private boolean flagSet;
		private boolean qMarkSet;
		private boolean isMine;
		private boolean isBorder;
		private Number num;
		
		public FieldState(){
			reset();
		}
		public boolean isNumber(){
			return !isMine;
		}
		public boolean isMine(){
			return isMine;
		}
		public boolean isQmarkSet(){
			return qMarkSet;
		}
		public boolean isFlagSet(){
			return flagSet;
		}
		public boolean isExploded(){
			return isFullPressed && isMine;
		}
		public boolean isPressed(){
			return isHalfPressed || isFullPressed;
		}
		public boolean isHalfPressed(){
			return isHalfPressed;
		}
		
		public boolean isFullPressed(){
			return isFullPressed;
		}
		
		public boolean isHovered(){
			return isHovered;
		}
		
		public boolean numVisible(){
			return isFullPressed || flagSet || qMarkSet;
		}
		
		public boolean isBorder(){
			return isBorder;
		}
		
		public Number getNum(){
			return num;
		}
		
		public void reset(){
			isHalfPressed = false;
			isHovered = false;
			numVisible = false;
			isFullPressed = false;
			flagSet = false;
			qMarkSet = false;
			isMine = false;
			num = Number.ZERO;
		}
		
	}
}
