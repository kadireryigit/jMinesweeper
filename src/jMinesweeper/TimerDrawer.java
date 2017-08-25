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
		t.addObserver(this);
		this.g2 = g2;
		this.timer = t;
		this.x = x;
		this.y = y;
	}
	
	public void draw(){
		long milli = timer.getTimeInMillis();
		int sec,min,hou;
		sec = (int) (milli/1000%60);
		min = (int) (milli/60000%60);
		hou = (int) (milli/3600000%24);
		String s,m,h;
		if(sec<10)
			s = "0"+sec;
		else s = ""+sec;
		if(min<10)
			m = "0"+min;
		else m = ""+min;
		if(hou<10)
			h = "0"+hou;
		else h = ""+hou;
//		System.out.println("Time: " + hou+ ":"+min + ":"+sec);
		g2.setColor(Color.white);
		g2.fillRect(x, y-20, 120, 20);
		g2.setColor(Color.black);
//		g2.setFont(new Font("Arial",Font.BOLD,16));
		g2.drawString("Time: " + h+ ":"+m + ":"+s, x, y);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		draw();
		this.setChanged();
		this.notifyObservers();
		
	}
	
	
}
