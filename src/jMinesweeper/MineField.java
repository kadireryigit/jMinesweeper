package jMinesweeper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import jMinesweeper.MineFieldControl.MineFieldState;

public class MineField extends JPanel implements Observer{
	
	private BufferedImage img;
	private Graphics2D g2;
	public final static int fieldOffsetX = 1, fieldOffsetY = 1;
	public final static int indentX = 0, indentY = 0;
	public final static  int singleWidth=40-2*indentX, singleHeight=40-2*indentY;
	public final static int outdentX = 0, outdentY = 0;
	
	private int fieldWidth, fieldHeight;
//	private FieldDrawer fields[][];
//	private FieldControl fieldC[][];
	private MineFieldControl mineC;
	private Timer time;
	private TimerDrawer timeDraw;
	private MineFieldState fieldState;
	
	public MineField(int width, int height){
		super();
		
		this.mineC = new MineFieldControl(width,height,this);
		this.fieldState = mineC.getState();
		setDimensions(width,height);
			
		this.addMouseListener(mineC); 
		this.addMouseMotionListener(mineC);
		
		this.time = new Timer();
		this.timeDraw = new TimerDrawer(10,20,time,g2,this);
		this.time.addObserver(timeDraw);
		
		
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(img, 0, 0, null);
	}

	
	public void reset(){
		mineC.reset();
	}
	
	public void setMineCount(int mines){
		mineC.setMineCount(mines);
	}
	
	public void setDimensions(int width, int height){
		
		
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		
		if(width!=fieldWidth || height!=fieldHeight){ //new size chosen
			if(this.img!=null)
				this.img.flush();
			this.fieldWidth = width;
			this.fieldHeight = height;
			this.img = gc.createCompatibleImage((fieldWidth+2)*singleWidth, (fieldHeight+2)*singleHeight);
			this.g2 = img.createGraphics();
	//		this.g2.setColor(Color.LIGHT_GRAY.brighter());
			this.g2.fillRect(0, 0, img.getWidth(), img.getHeight());
			this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			
			mineC.setDimensions(width, height, g2);
		}else{
			mineC.reset();
		}
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		this.repaint();
		if(fieldState.hasStarted())
			time.startTimer();
		if(fieldState.hasLost())
			time.stopTimer();
	}
	
	
}
