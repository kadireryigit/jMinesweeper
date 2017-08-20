package jMinesweeper;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MineFieldControl extends Observable implements MouseListener,MouseMotionListener {

	
	private int[][] fieldNums;
	private FieldControl[][] fieldC;
	private FieldDrawer[][] fieldD;
	private int nrMines;
	private int spawnX, spawnY;
	private FieldControl halfPressed;
	private FieldControl middleClicked;
	
	/*
	 * Enables reset of a clicked field if the mouse was moved away from the field before release of the mouse button
	 */
	private boolean clickReset = true;
	
	
	private boolean init = true;
	private int fieldWidth, fieldHeight;
	private int mines = 99;
	private FieldGenerator fieldGen;
	private FieldControl hoveredField;
	private Observer obs;
	
	public MineFieldControl(int width, int height, Observer obs){
		fieldGen = new FieldGenerator();
		this.obs = obs;
		this.addObserver(obs);
		this.fieldWidth = width;
		this.fieldHeight = height;
	}
	
	public void leftPressOnField(FieldControl field){
		if(clickReset){
			field.halfPress();
			halfPressed = field;
		}
		else
			field.fullPress();
	}
	
	public void middlePressOnField(FieldControl field){
		middleClicked = field;
		int x = field.getX()+1;
		int y = field.getY()+1;
		fieldC[x-1][y-1].halfPress();
		fieldC[x][y-1].halfPress();
		fieldC[x+1][y-1].halfPress();
		fieldC[x-1][y].halfPress();
//		fieldC[x][y].halfPress();
		fieldC[x+1][y].halfPress();
		fieldC[x-1][y+1].halfPress();
		fieldC[x][y+1].halfPress();
		fieldC[x+1][y+1].halfPress();
	}

	public void leftReleaseOnField(){
		
	}
	
	public void middleReleaseOnField(FieldControl field){
		int x = middleClicked.getX()+1;
		int y = middleClicked.getY()+1;
		int fieldVal = middleClicked.getNum().ordinal();
		int fieldFlags = 0;
		
		//count flags
		fieldFlags += fieldC[x-1][y-1].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x][y-1].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y-1].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x-1][y].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x-1][y+1].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x][y+1].isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y+1].isFlagSet() ? 1 : 0;
		
		if(fieldFlags==fieldVal){
			//TODO need to check neighbors here
			
			if(!fieldC[x-1][y-1].isBorder())
				clickZeroField(fieldC[x-1][y-1]);
			if(!fieldC[x][y-1].isBorder())
				clickZeroField(fieldC[x][y-1]);
			if(!fieldC[x+1][y-1].isBorder())
				clickZeroField(fieldC[x+1][y-1]);
			if(!fieldC[x-1][y].isBorder())
				clickZeroField(fieldC[x-1][y]);
			if(!fieldC[x][y].isBorder())
				clickZeroField(fieldC[x][y]);
			if(!fieldC[x+1][y].isBorder())
				clickZeroField(fieldC[x+1][y]);
			if(!fieldC[x-1][y+1].isBorder())
				clickZeroField(fieldC[x-1][y+1]);
			if(!fieldC[x][y+1].isBorder())
				clickZeroField(fieldC[x][y+1]);
			if(!fieldC[x+1][y+1].isBorder())
				clickZeroField(fieldC[x+1][y+1]);
		} else{
			fieldC[x-1][y-1].unPress();
			fieldC[x][y-1].unPress();
			fieldC[x+1][y-1].unPress();
			fieldC[x-1][y].unPress();
			fieldC[x][y].unPress();
			fieldC[x+1][y].unPress();
			fieldC[x-1][y+1].unPress();
			fieldC[x][y+1].unPress();
			fieldC[x+1][y+1].unPress();
		}
		
		middleClicked = null;
	}

	
	public void rightClickOnField(FieldControl field){
		field.changeRightClick();
	}

	
	public void doubleClickOnField(FieldControl field){
		middlePressOnField(field);
		middleReleaseOnField(field);
	}
	
	/**
	 * Converts pixel coordinates in the field image to coordinates for the field array
	 * @param x - column pixel coordinate of the image 
	 * @param y - row pixel coordinate of the image
	 * @return the field array coordinate according to the pixel coordinate
	 */
	public Point pixelToFieldCoor(int x, int y){
		int xI = x/MineField.singleWidth-MineField.fieldOffsetX+1;
		int yI = y/MineField.singleHeight-MineField.fieldOffsetY+1;
		
//		System.out.println(new Point(xI,yI));
		return new Point(xI,yI);
	}
	
	public Point fieldCoorToPixel(int x, int y){
		int xP = (x+MineField.fieldOffsetX)*MineField.singleWidth;
		int yP = (y+MineField.fieldOffsetY)*MineField.singleHeight;
		
		return new Point(xP,yP);
	}
	
	public boolean checkIndices(int x, int y){
		return x>0 && x<fieldWidth+1 && y>0 && y<fieldHeight+1;
	}
	
	public void genField(int atX, int atY){
		
		int[][] fieldNums = fieldGen.genFieldAt(atX,atY,fieldWidth,fieldHeight,mines);
		for(int i = 0;i<fieldWidth;i++)
			for(int j = 0; j<fieldHeight;j++){
				fieldC[i+1][j+1].setNum(fieldNums[i+1][j+1]);
			}
	}
	
	public void reset(){
		for(int i = 0;i<fieldWidth;i++)
			for(int j = 0; j<fieldHeight;j++){
				fieldC[i+1][j+1].reset();
			}
		init = true;
		drawField();
	}
	
	
	/**
	 * Draws a bright overlay above a single fieldbutton.
	 * @param x - column coordinate of the field button
	 * @param y - row coordinate of the field button
	 */
	public void hoverSingleField(int x, int y){
		if(hoveredField!=null)
			hoveredField.hoverOff();

		if(checkIndices(x,y)){
			hoveredField = fieldC[x][y];				
			hoveredField.hoverOn();

		}else hoveredField = null;
		
	}
	
	public void drawField(){
		
		for(int i = 0; i<fieldWidth;i++)
			for(int j = 0; j<fieldHeight;j++)
				fieldD[i+1][j+1].draw(false);
		
		this.setChanged();
		this.notifyObservers();
	}
	

	private void clickZeroField(FieldControl field){
		List<FieldControl> neighbors = new ArrayList<FieldControl>();
		
		neighbors.add(field);
		int x = field.getX()+1;
		int y = field.getY()+1;
		field.fullPress();
		
		//TODO optimize this
		while(neighbors.size()>0){
			
			FieldControl curField = neighbors.remove(0);
			
			if (!curField.isBorder() && curField.getNum().ordinal()==0){ //collect neighbors
				x = curField.getX()+1;
				y = curField.getY()+1;
				//top left
				if(!fieldC[x-1][y-1].isPressed() && fieldC[x-1][y-1].isNumber()){
						fieldC[x-1][y-1].fullPress();
						if(fieldC[x-1][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y-1]);
				}
				//top
				if(!fieldC[x][y-1].isPressed() && fieldC[x][y-1].isNumber()){
						fieldC[x][y-1].fullPress();
						if(fieldC[x][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x][y-1]);
				}
				//top right
				if(!fieldC[x+1][y-1].isPressed() && fieldC[x+1][y-1].isNumber()){
						fieldC[x+1][y-1].fullPress();
						if(fieldC[x+1][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y-1]);
				}
				//left
				if(!fieldC[x-1][y].isPressed() && fieldC[x-1][y].isNumber()){
						fieldC[x-1][y].fullPress();
						if(fieldC[x-1][y].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y]);
				}
				//right
				if(!fieldC[x+1][y].isPressed() && fieldC[x+1][y].isNumber()){
						fieldC[x+1][y].fullPress();
						if(fieldC[x+1][y].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y]);
				}
				//bottom left
				if(!fieldC[x-1][y+1].isPressed() && fieldC[x-1][y+1].isNumber()){
						fieldC[x-1][y+1].fullPress();
						if(fieldC[x-1][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y+1]);
				}
				//bottom 
				if(!fieldC[x][y+1].isPressed() && fieldC[x][y+1].isNumber()){
						fieldC[x][y+1].fullPress();
						if(fieldC[x][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x][y+1]);
				}
				//bottom right
				if(!fieldC[x+1][y+1].isPressed() && fieldC[x+1][y+1].isNumber()){
						fieldC[x+1][y+1].fullPress();
						if(fieldC[x+1][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y+1]);
				}
				
				
			}				
		}
	}
	
	public void setMineCount(int mines){
		this.mines = mines;
	}
	
	public void setDimensions(int width, int height, Graphics2D g2){
		
		this.fieldC = new FieldControl[width+2][height+2]; //Extra edges so we don't need to check bounds
		this.fieldD = new FieldDrawer[width+2][height+2];
		
		this.fieldWidth = width;
		this.fieldHeight = height;
		
		this.init = true;
		
		//initialize borders: fullpress borders to identify them
		for(int i = 0; i<width+2;i++){
			this.fieldC[i][0] = new FieldControl(i-1,-1);//top border
			this.fieldC[i][0].setBorder();
			this.fieldC[i][fieldHeight+1] = new FieldControl(i-1,fieldHeight);//bottom border
			this.fieldC[i][fieldHeight+1].setBorder();	
		}
		for(int i = 1; i<height+1;i++){
			this.fieldC[0][i] = new FieldControl(-1,i-1);//left border
			this.fieldC[0][i].setBorder();
			this.fieldC[fieldWidth+1][i] = new FieldControl(fieldWidth,i-1);//right border
			this.fieldC[fieldWidth+1][i].setBorder();
		}
//		Initialize middle
		for(int i = 1; i<width+1;i++)
			for(int j =1;j<height+1;j++){
				this.fieldC[i][j] = new FieldControl(i-1,j-1);
				this.fieldD[i][j] = new FieldDrawer(g2,i-1,j-1,fieldC[i][j],obs);
			}
		drawField();
		
//		this.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point fieldCo = pixelToFieldCoor(e.getX() ,e.getY());
		if(halfPressed!=null){
			if(fieldCo.x != halfPressed.getX()+1 || fieldCo.y != halfPressed.getY()+1 ){
				halfPressed.unPress();
				halfPressed = null;
			}
				
		}
		
		if(middleClicked!=null){
			if(fieldCo.x != middleClicked.getX()+1 || fieldCo.y != middleClicked.getY()+1 ){
				middleClicked.unPress();
				
				int x = middleClicked.getX()+1;
				int y = middleClicked.getY()+1;
				fieldC[x-1][y-1].unPress();
				fieldC[x][y-1].unPress();
				fieldC[x+1][y-1].unPress();
				fieldC[x-1][y].unPress();
//				fieldC[x][y].unPress();
				fieldC[x+1][y].unPress();
				fieldC[x-1][y+1].unPress();
				fieldC[x][y+1].unPress();
				fieldC[x+1][y+1].unPress();
				middleClicked = null;
			}
				
		}
//		System.out.println("dragged");	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point fieldCo = pixelToFieldCoor(e.getX() ,e.getY());
		hoverSingleField(fieldCo.x, fieldCo.y);
//	System.out.println("moved");
		
	}
	boolean doubleClickState = false;
	long doubleClickTimeStart;
	@Override
	public void mouseClicked(MouseEvent e) {
		long delta = System.nanoTime() - doubleClickTimeStart;
		doubleClickTimeStart = System.nanoTime();
		if(delta<200000000){ //200ms time between single clicks
			//Double Click detected
			Point fieldCo = pixelToFieldCoor(e.getX() ,e.getY());
			if(checkIndices(fieldCo.x, fieldCo.y)){
				FieldControl field = fieldC[fieldCo.x][fieldCo.y];
				
				doubleClickOnField(field);
			}
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//Entered the image are
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//Exited the image are
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point fieldCo = pixelToFieldCoor(e.getX() ,e.getY());
		if(checkIndices(fieldCo.x,fieldCo.y)){
			FieldControl field = fieldC[fieldCo.x][fieldCo.y];
			
			//Left click
			if(e.getButton() == MouseEvent.BUTTON1)
				leftPressOnField(field);
			
			//Right click
			if(e.getButton() == MouseEvent.BUTTON3)
				rightClickOnField(field);
			
			
			//Middle click
			middleClicked = null;
			if(e.getButton() == MouseEvent.BUTTON2)
				middlePressOnField(field);
		
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
			Point fieldCo = pixelToFieldCoor(e.getX() ,e.getY());
			if(checkIndices(fieldCo.x, fieldCo.y)){
				FieldControl field = fieldC[fieldCo.x][fieldCo.y];
				
				if (field.isHalfPressed() || !clickReset){
				
					if(init){
						init = false;	
						genField(fieldCo.x-1,fieldCo.y-1);
//						System.out.println("new field generated");
					}
					
					field.fullPress();
					halfPressed = null;
					//open all neighbor fields that are zero. stop at fields that are numbers.
					if(field.getNum().ordinal()==0)
						clickZeroField(field);
					
					
				
					
				}

				if(middleClicked!=null){
					middleReleaseOnField(field);
				}
			}
			
	}
		
		
	
		
	
	
	
}
