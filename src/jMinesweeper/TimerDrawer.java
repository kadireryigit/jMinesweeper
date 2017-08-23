package jMinesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

public class TimerDrawer extends Observable implements Observer {

	private Graphics2D g2;
	private int x,y;
	private Timer timer;
	
	public TimerDrawer(int x, int y, Timer t ,Graphics2D g2, Observer obs){
		this.addObserver(obs);
		this.g2 = g2;
		this.timer = t;
		this.x = x;
		this.y = y;
	}
	
	public void draw(){
		long milli = timer.getTimeInMillis();
		String sec,min,hou;
		sec = String.valueOf(milli/1000%60);
		min = String.valueOf(milli/60000%60);
		hou = String.valueOf(milli/3600000%24);
//		System.out.println("Time: " + hou+ ":"+min + ":"+sec);
		g2.setColor(Color.white);
		g2.fillRect(x, y-20, 100, 20);
		g2.setColor(Color.black);
		g2.setFont(new Font("Arial",Font.BOLD,16));
		g2.drawString("Time: " + hou+ ":"+min + ":"+sec, x, y);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		draw();
		
	}
	
	
}
