package jMinesweeper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

public class FlagCountDrawer{

	private Graphics2D g2;
	private int x,y;

	public FlagCountDrawer(int x, int y, Graphics2D g2){
		this.x = x;
		this.y = y;
		this.g2 = g2;
	}
	
	public void draw(int count){
		g2.setColor(Color.white);
		g2.fillRect(x, y-20, 200, 20);
		g2.setColor(Color.black);
		g2.drawString( "Mines Left: " + count ,x,y);
		//TODO add mine image instead of text
	}

	
}
