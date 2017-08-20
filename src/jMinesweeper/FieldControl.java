package jMinesweeper;

import java.util.Observable;

public class FieldControl extends Observable{

	private boolean isHalfPressed;
	private boolean isFullPressed;
	private boolean isHovered;
	private boolean numVisible;
	private boolean flagSet;
	private boolean qMarkSet;
	private boolean isMine;
	private boolean isBorder;
	private int x,y;
	private Number num;
	
	public FieldControl(int x, int y){
		this.x = x;
		this.y = y;
		this.reset();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
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
	
	public void setBorder(){
		this.isBorder = true;
	}
	public void setFlag(){
//		this.num = Number.FLAG;
		if(!isFullPressed && !isHalfPressed){
			this.flagSet = true;
			this.qMarkSet = false;
			this.numVisible = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void setQMark(){
		if(!isFullPressed && !isHalfPressed){
	//		this.num = Number.QUESTION;
			this.flagSet = false;
			this.qMarkSet = true;
			this.numVisible = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void setZero(){
//		this.num = Number.ZERO;
		this.flagSet = false;
		this.qMarkSet = false;
		this.numVisible = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void deleteFlagOrMark(){
//		this.num = Number.ZERO;
		this.flagSet = false;
		this.qMarkSet = false;
		this.numVisible = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean setOffMine(){
		if(isMine){
			this.setChanged();
			this.notifyObservers();
		}
		return isMine;
	}
	
	public void setMine(){
		this.num = Number.MINE;
		this.isMine = true;
	}
	
	public boolean fullPress(){
		if(!flagSet && !isFullPressed){
			isFullPressed = true;
			isHalfPressed = false;
			numVisible = true;
			qMarkSet = false;
			this.setChanged();
			this.notifyObservers();
		}
		return !flagSet && !isFullPressed;
	}
	
	public boolean halfPress(){
		if(!flagSet && !isFullPressed){
			isHalfPressed = true;
			isFullPressed = false;
			this.setChanged();
			this.notifyObservers();
		}
		return !flagSet && !isFullPressed;
	}
	
	public boolean unPress(){
		if(!isFullPressed){
			isFullPressed = false;
			isHalfPressed = false;
			this.setChanged();
			this.notifyObservers();
		}
		return !isFullPressed;
	}
	
	public void hoverOn(){
		if(!isPressed()){
			isHovered = true;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void hoverOff(){
		isHovered = false;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setNum(Number num){
		this.num = num; 
//		numVisible = true;
//		this.setChanged();
//		this.notifyObservers();
	}
	
	public void setNum(int num){
		if(num==FieldGenerator.MINE_BLOCK){
			this.num = Number.MINE;
			this.isMine = true;
		}else{
			this.isMine = false;
			num%=9;
			this.num = Number.values()[num];
		}
//		numVisible = true;
//		this.setChanged();
//		this.notifyObservers();
	}
	
	public void setNumVisible(){
		numVisible = true;
		this.setChanged();
		this.notifyObservers();
	}
	
	public Number getNum(){
		return this.num;
	}
	
	public void changeRightClick(){
		if(!isFullPressed && !isHalfPressed){
			if(flagSet)
				setQMark();
			else if(qMarkSet)
				setZero();
			else setFlag();
		}
	}
	public void reset(){
		this.isHalfPressed = false;
		this.isHovered = false;
		this.numVisible = false;
		this.isFullPressed = false;
		this.flagSet = false;
		this.qMarkSet = false;
		this.isMine = false;
		this.num = Number.ZERO;
	}
}
