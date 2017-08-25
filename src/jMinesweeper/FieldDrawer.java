package jMinesweeper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import jMinesweeper.FieldControl.FieldState;

public class FieldDrawer extends Observable implements Observer{
	
	public final FieldState fieldState;
	private int left;
	private int right; 
	private int top;
	private int bottom;
	private Graphics2D g2;
	
	private final Color lightgray= new Color(0xD5,0xD5,0xD5);
	private final Color gray= new Color(0xB8,0xB8,0xB8);
	private final Color hoverColor = new Color(0xd5,0xd5,0xd5);
	
	
	public FieldDrawer(Graphics2D g2, int x, int y, FieldState field,Observer obs){
		this.g2 = g2;
		this.fieldState = field;
		if(obs!=null)
		this.addObserver(obs);
		this.left 	= ( x + MineField.fieldOffsetX )     * MineField.singleWidth  + MineField.indentX;     
		this.right	= ( x + MineField.fieldOffsetX + 1 ) * MineField.singleWidth  - MineField.indentX - 1; 
		this.top 	= ( y + MineField.fieldOffsetY )     * MineField.singleHeight + MineField.indentY;     
		this.bottom = ( y + MineField.fieldOffsetY + 1 ) * MineField.singleHeight - MineField.indentY - 1;
	}                       
	/**
	 * Draws a single clickable fieldbutton.
	 * @param update if true the image will be updated after drawing
	 */
	public void drawUnpressedField(boolean update){
	
//		draw top and left shiny borders
		g2.setColor(lightgray);
		g2.drawLine(left, top, right, top);
		g2.drawLine(left, top, left, bottom);
		
		//draw center
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(left+1, top+1, right-left, bottom-top);
		
		//draw bottom and right dark borders
		g2.setColor(Color.darkGray);
		g2.drawLine(right, top, right, bottom);
		g2.drawLine(left, bottom, right, bottom);
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Draws a single clicked clickable fieldbutton.
	 * @param x - column coordinate of the field button
	 * @param y - row coordinate of the field button
	 */
	public void drawPressedField(boolean update){
	
//		draw top and left shiny borders
		g2.setColor(Color.DARK_GRAY);
		g2.drawLine(left, top, right, top);
		g2.drawLine(left, top, left, bottom);
		
		//draw center
		g2.setColor(gray);
		g2.fillRect(left+1, top+1, right-left, bottom-top);
		
		//draw bottom and right dark borders
		g2.setColor(Color.LIGHT_GRAY);
		
		g2.drawLine(right, top, right, bottom);
		g2.drawLine(left, bottom, right, bottom);
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void drawExplodedField(boolean update){
		g2.setColor(Color.DARK_GRAY);
		g2.drawLine(left, top, right, top);
		g2.drawLine(left, top, left, bottom);
		
		//draw center
		g2.setColor(Color.RED);
		g2.fillRect(left+1, top+1, MineField.singleWidth-2, MineField.singleHeight-2);
		
		//draw bottom and right dark borders
		g2.setColor(Color.LIGHT_GRAY);
		
		g2.drawLine(right, top, right, bottom);
		g2.drawLine(left, bottom, right, bottom);
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
	}
	/**
	 * Draws a bright overlay above a single field button.
	 * @param x - column coordinate of the field button
	 * @param y - row coordinate of the field button
	 */
	public void drawHoveredField(boolean update){   

		g2.setColor( hoverColor );
		g2.fillRect(left+1, top+1, right-left-1,bottom-top-1);	//+1/-2 because we don't want to erase the borders

		//highlight borders only
//		g2.drawLine(left+1, top+1, right-1, top+1);
//		g2.drawLine(left+1, top+1, left+1, bottom-1);
//		g2.drawLine(right-1, top+1, right-1, bottom-1);
//		g2.drawLine(left+1, bottom-1, right-1, bottom-1);
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
	}
	

	
	/**
	 * Draws a number into the field button
	 * @param x - column coordinate of the field button
	 * @param y - row coordinate of the field button
	 * @param num - number enumeration to draw
	 */
	public void drawNumber(Number num,boolean update){
		if(num==null) return;
		
		g2.setColor(num.getColor());	
		g2.drawString(String.valueOf(num.getChar()), left+MineField.singleWidth/2-5, top+MineField.singleHeight/2+5);
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void draw(boolean update){
		if(fieldState.isPressed()){
			drawPressedField(false);
			
		}
		else{
			drawUnpressedField(false);
			if(fieldState.isHovered())
				drawHoveredField(false);
		}
		
		
			
		if(fieldState.isExploded())
			drawExplodedField(false);
		if(fieldState.numVisible()){
			if(fieldState.isFlagSet())
				drawNumber(Number.FLAG,false);
			else if(fieldState.isQmarkSet())
				drawNumber(Number.QUESTION,false);
			else 
				drawNumber(fieldState.getNum(),false);
		}
		if(update){
			this.setChanged();
			this.notifyObservers();
		}
		
	}
	

	public void reset(){
		fieldState.reset();
	}
	@Override
	public void update(Observable o, Object arg) {
		draw(true);
		this.setChanged();
		this.notifyObservers();
	}
}
