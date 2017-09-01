package jMinesweeper;

import java.awt.Dimension;
import java.awt.Font;
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

public class MineFieldDrawer extends JPanel implements Observer{
	
	private BufferedImage img;
	private Graphics2D g2;
	public final static int fieldOffsetX = 1, fieldOffsetY = 1;
	public final static int indentX = 0, indentY = 0;
	public final static int singleWidth=40-2*indentX, singleHeight=40-2*indentY;
	public final static int outdentX = 0, outdentY = 0;
	
	private int fieldWidth, fieldHeight;
//	private MineFieldControl mineC;
	
	
	private MineFieldState fieldState;
	
	public MineFieldDrawer(int width, int height, MineFieldState mineState){
		super();

		this.fieldState = mineState;
			
		
		
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(img, 0, 0, null);
	}
	
	public void setDimensions(int width, int height){
		
		
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		
	
		if(this.img!=null)
			this.img.flush();
		this.fieldWidth = width;
		this.fieldHeight = height;
		this.img = gc.createCompatibleImage((fieldWidth+2)*singleWidth, (fieldHeight+2)*singleHeight);
		this.g2 = img.createGraphics();
		this.g2.setFont(new Font("Arial",Font.BOLD,16));
//		this.g2.setColor(Color.LIGHT_GRAY.brighter());
		this.g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		
		
	}
	
	public Graphics2D getGraphics2D(){
		return this.g2;
	}
	@Override
	public void update(Observable obs, Object obj) {
		this.repaint();
	}
	
	
}
