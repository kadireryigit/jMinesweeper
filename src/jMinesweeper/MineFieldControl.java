package jMinesweeper;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MineFieldControl extends Observable implements MouseListener,MouseMotionListener,FieldListener {

	
	private int[][] fieldNums;
	private FieldControl[][] fieldC;
	private FieldDrawer[][] fieldD;
	private int nrMines;
	private int spawnX, spawnY;
	private FieldControl halfPressed;
	private FieldControl middleClicked;
	private FlagCountDrawer flagCountDrawer;
	private TimerDrawer timeDraw;
//	private int flagCount;
	
	/*
	 * Enables reset of a clicked field if the mouse was moved away from the field before release of the mouse button
	 */
	private boolean clickReset = true;
	
	private Timer timer;
	private boolean init = true;
	private int fieldWidth, fieldHeight;
	private int mines = 99;
	private FieldGenerator fieldGen;
	private FieldControl hoveredField;
	private Observer obs;
	private MineFieldState fieldState;
	private MineFieldDrawer mine;
	private static final int startWidth = 32;
	private static final int startHeight = 16;
	private static final int startMines = 99;
	private JFrame f; 
	
	public MineFieldControl(int width, int height,int mineCount){
		this.fieldGen = new FieldGenerator();
		this.timer = new Timer();
		this.fieldState = new MineFieldState(mineCount);
		this.mine = new MineFieldDrawer(startWidth,startHeight,fieldState);
		this.mine.addMouseListener(this); 
		this.mine.addMouseMotionListener(this);
		this.addObserver(mine);
		obs = mine;
		this.setDimensions(width, height);
		this.init();
	}
	
	public MineFieldControl(){
		 this(startWidth,startHeight,startMines);
		
	}
	public void leftPressOnField(FieldControl field){
		if(clickReset){
			field.halfPress();
			halfPressed = field;
		}
		else
			field.fullPress(true);
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
		//TODO implement this
	}
	
	public void middleReleaseOnField(FieldControl field){
		
		int x = middleClicked.getX()+1;
		int y = middleClicked.getY()+1;
		int fieldVal = middleClicked.getNum().ordinal();
		int fieldFlags = 0;
		
		//count flags
		fieldFlags += fieldC[x-1][y-1].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x][y-1].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y-1].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x-1][y].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x-1][y+1].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x][y+1].getState().isFlagSet() ? 1 : 0;
		fieldFlags += fieldC[x+1][y+1].getState().isFlagSet() ? 1 : 0;
		
		if(field.getState().isPressed() && fieldFlags==fieldVal){
			
			if(!fieldC[x-1][y-1].getState().isBorder())
				clickZeroField(fieldC[x-1][y-1]);
			if(!fieldC[x][y-1].getState().isBorder())
				clickZeroField(fieldC[x][y-1]);
			if(!fieldC[x+1][y-1].getState().isBorder())
				clickZeroField(fieldC[x+1][y-1]);
			if(!fieldC[x-1][y].getState().isBorder())
				clickZeroField(fieldC[x-1][y]);
			if(!fieldC[x][y].getState().isBorder())
				clickZeroField(fieldC[x][y]);
			if(!fieldC[x+1][y].getState().isBorder())
				clickZeroField(fieldC[x+1][y]);
			if(!fieldC[x-1][y+1].getState().isBorder())
				clickZeroField(fieldC[x-1][y+1]);
			if(!fieldC[x][y+1].getState().isBorder())
				clickZeroField(fieldC[x][y+1]);
			if(!fieldC[x+1][y+1].getState().isBorder())
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
		if(field.getState().isFlagSet()){
			fieldState.flagCount--;
			flagCountDrawer.draw(fieldState.flagCount);
		}
			
		else if(field.getState().isQmarkSet()) {
			fieldState.flagCount++;
			flagCountDrawer.draw(fieldState.flagCount);
		}
		
//		System.out.println(fieldState.flagCount);
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
		int xI = x/MineFieldDrawer.singleWidth-MineFieldDrawer.fieldOffsetX+1;
		int yI = y/MineFieldDrawer.singleHeight-MineFieldDrawer.fieldOffsetY+1;
		
//		System.out.println(new Point(xI,yI));
		return new Point(xI,yI);
	}
	
	public Point fieldCoorToPixel(int x, int y){
		int xP = (x+MineFieldDrawer.fieldOffsetX)*MineFieldDrawer.singleWidth;
		int yP = (y+MineFieldDrawer.fieldOffsetY)*MineFieldDrawer.singleHeight;
		
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
		fieldState.reset();
		timer.reset();
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
		timeDraw.draw();
		flagCountDrawer.draw(fieldState.flagCount);
		this.setChanged();
		this.notifyObservers();
	}
	

	private void clickZeroField(FieldControl field){
		List<FieldControl> neighbors = new ArrayList<FieldControl>();
		
		neighbors.add(field);
		int x = field.getX()+1;
		int y = field.getY()+1;
		field.fullPress(true);
		
		//TODO optimize this
		while(neighbors.size()>0){
			
			FieldControl curField = neighbors.remove(0);
			
			if (!curField.getState().isBorder() && curField.getNum().ordinal()==0){ //collect neighbors
				x = curField.getX()+1;
				y = curField.getY()+1;
				//top left
				if(!fieldC[x-1][y-1].getState().isPressed() && fieldC[x-1][y-1].getState().isNumber()){
						fieldC[x-1][y-1].fullPress(true);
						if(fieldC[x-1][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y-1]);
				}
				//top
				if(!fieldC[x][y-1].getState().isPressed() && fieldC[x][y-1].getState().isNumber()){
						fieldC[x][y-1].fullPress(true);
						if(fieldC[x][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x][y-1]);
				}
				//top right
				if(!fieldC[x+1][y-1].getState().isPressed() && fieldC[x+1][y-1].getState().isNumber()){
						fieldC[x+1][y-1].fullPress(true);
						if(fieldC[x+1][y-1].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y-1]);
				}
				//left
				if(!fieldC[x-1][y].getState().isPressed() && fieldC[x-1][y].getState().isNumber()){
						fieldC[x-1][y].fullPress(true);
						if(fieldC[x-1][y].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y]);
				}
				//right
				if(!fieldC[x+1][y].getState().isPressed() && fieldC[x+1][y].getState().isNumber()){
						fieldC[x+1][y].fullPress(true);
						if(fieldC[x+1][y].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y]);
				}
				//bottom left
				if(!fieldC[x-1][y+1].getState().isPressed() && fieldC[x-1][y+1].getState().isNumber()){
						fieldC[x-1][y+1].fullPress(true);
						if(fieldC[x-1][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x-1][y+1]);
				}
				//bottom 
				if(!fieldC[x][y+1].getState().isPressed() && fieldC[x][y+1].getState().isNumber()){
						fieldC[x][y+1].fullPress(true);
						if(fieldC[x][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x][y+1]);
				}
				//bottom right
				if(!fieldC[x+1][y+1].getState().isPressed() && fieldC[x+1][y+1].getState().isNumber()){
						fieldC[x+1][y+1].fullPress(true);
						if(fieldC[x+1][y+1].getNum().ordinal()==0)
							neighbors.add(fieldC[x+1][y+1]);
				}
				
				
			}				
		}
	}
	
	public void setMineCount(int mines){
		this.mines = mines;
		this.fieldState.flagCount = mines;
		this.fieldState.flagCountInit = mines;
	}
	
	public MineFieldState getState(){
		return fieldState;
	}
	
	public Timer getTimer(){
		return timer;
	}
	
	public void setDimensions(int width, int height){
		
		if(width!=fieldWidth || height!=fieldHeight){ //new size chosen
			mine.setDimensions(width, height);
			Graphics2D g2 = mine.getGraphics2D();
			this.fieldC = new FieldControl[width+2][height+2]; //Extra edges so we don't need to check bounds
			this.fieldD = new FieldDrawer[width+2][height+2];
			
			this.fieldWidth = width;
			this.fieldHeight = height;
			
			this.flagCountDrawer = new FlagCountDrawer(fieldWidth*MineFieldDrawer.singleWidth-40,20,g2);
			this.timeDraw = new TimerDrawer(10,20,timer,g2,obs);
			
			//initialize borders: fullpress borders to identify them
			for(int i = 0; i<width+2;i++){
				this.fieldC[i][0] = new FieldControl(i-1,-1,this);//top border
				this.fieldC[i][0].setBorder();
				this.fieldC[i][fieldHeight+1] = new FieldControl(i-1,fieldHeight,this);//bottom border
				this.fieldC[i][fieldHeight+1].setBorder();	
			}
			for(int i = 1; i<height+1;i++){
				this.fieldC[0][i] = new FieldControl(-1,i-1,this);//left border
				this.fieldC[0][i].setBorder();
				this.fieldC[fieldWidth+1][i] = new FieldControl(fieldWidth,i-1,this);//right border
				this.fieldC[fieldWidth+1][i].setBorder();
			}
	//		Initialize middle
			for(int i = 1; i<width+1;i++)
				for(int j =1;j<height+1;j++){
					this.fieldC[i][j] = new FieldControl(i-1,j-1,this);
					this.fieldD[i][j] = new FieldDrawer(g2,i-1,j-1,fieldC[i][j].getState(),obs);
					this.fieldC[i][j].addObserver(fieldD[i][j]);
				}
			drawField();
		}
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
	long doubleClickTimeStart;
	@Override
	public void mouseClicked(MouseEvent e) {
		long delta = System.nanoTime() - doubleClickTimeStart;
		doubleClickTimeStart = System.nanoTime();
		if(!fieldState.lost && delta<200000000){ //200ms time between single clicks
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
		if(!fieldState.lost && checkIndices(fieldCo.x,fieldCo.y)){
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
				
				if (field.getState().isHalfPressed() || !clickReset){
				//TODO clean this up
				//TODO add win event
					if(init){
						init = false;	
						fieldState.initialized = true;
						genField(fieldCo.x-1,fieldCo.y-1);
						timer.startTimer();
//						System.out.println("new field generated");
					}
					
					field.fullPress(true);
					fieldState.started = true;
					
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
		
		
	class MineFieldState{
		private boolean initialized;
		private boolean started;
		private boolean lost;
		private boolean won;
		private int flagCount;
		private int flagCountInit;
		
		public MineFieldState(int flagCount){
			flagCountInit = flagCount;
			reset();
		}
		
		public void reset(){
			this.initialized = false;
			this.lost = false;
			this.won = false;		
			this.started = false;
			this.flagCount = flagCountInit;
		}
		
		public int getFlagCount(){
			return flagCount;
		}
		public boolean hasStarted(){
			return started;
		}
		public boolean isInitialized(){
			return initialized;
		}
		public boolean hasLost(){
			return lost;
		}
		public boolean isWon(){
			return won;
		}
	}

	public void init(){
		this.f= new JFrame("jMineSweeper");
	
		//Frame settings
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
	//	f.setMinimumSize(new Dimension(240,350));
		
		//GUI components
		JPanel buttonPanel = new JPanel();	//panel that includes all control buttons
		buttonPanel.setPreferredSize(new Dimension(400,35));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));	//arrange everything left to right
		JButton genB = new JButton("Generate");	//add generate button 
		genB.setPreferredSize(new Dimension(100,30));
		JLabel widthL = new JLabel("width:");
		JLabel heightL = new JLabel("height:");
		JLabel mineCountL = new JLabel("mines:");
		JTextField widthTF = new JTextField(String.valueOf(startWidth));
		
		widthTF.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				try{
					Integer.valueOf(widthTF.getText());	
				}catch(NumberFormatException ex){
					widthTF.setText(String.valueOf(startWidth));
			
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				widthTF.selectAll();
				
			}
		});
		widthTF.setPreferredSize(new Dimension(50,24));
		JTextField heightTF = new JTextField(String.valueOf(startHeight));
		heightTF.setPreferredSize(new Dimension(50,24));
		heightTF.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				try{
					Integer.valueOf(heightTF.getText());	
				}catch(NumberFormatException ex){
					heightTF.setText(String.valueOf(startHeight));
			
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				heightTF.selectAll();
				
			}
		});
		JTextField mineCountTF = new JTextField(String.valueOf(startMines));
		mineCountTF.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				try{
					Integer.valueOf(mineCountTF.getText());	
				}catch(NumberFormatException ex){
					mineCountTF.setText(String.valueOf(startMines));
			
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				mineCountTF.selectAll();
				
			}
		});
		mineCountTF.setPreferredSize(new Dimension(50,24));
		
		genB.addActionListener(new ActionListener() {
			boolean exception = false;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int width=startWidth,height=startHeight,mines=startMines;
				try{
					width = Integer.valueOf(widthTF.getText());
					if(width<0){
						widthTF.setText(String.valueOf(-width));
						width = -width;
					}
						
				}catch(NumberFormatException e){
					widthTF.setText(String.valueOf(startWidth));
					System.out.println("Illegal width, Enter new value");
					exception = true;
				}
				try{
					
					height = Integer.valueOf(heightTF.getText());	
					if(height<0){
						heightTF.setText(String.valueOf(-height));
						height = -height;
					}
					
				}catch(NumberFormatException e){
					heightTF.setText(String.valueOf(startHeight));
					System.out.println("Illegal height, Enter new value");
					exception = true;
				}				
				
				try{
					
					mines = Integer.valueOf(mineCountTF.getText());	
					if(mines<0){
						mineCountTF.setText(String.valueOf(-mines));
						mines = -mines;
					}
					
				}catch(NumberFormatException e){
					heightTF.setText(String.valueOf(startMines));
					System.out.println("Illegal mine Count, Enter new value");
					exception = true;
				}			
				
				if(!exception){
					reset();
					setMineCount(mines);
					setDimensions(width, height);
	//				System.out.println("draw new field");
					f.pack();
				}
				
			}
		});
		
		buttonPanel.add(genB);
		buttonPanel.add(widthL);
		buttonPanel.add(widthTF);
		buttonPanel.add(heightL);
		buttonPanel.add(heightTF);
		buttonPanel.add(mineCountL);
		buttonPanel.add(mineCountTF);
		
		
	//	mine.drawField();
		
		
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //arrange all panels vertically
		f.add(buttonPanel);
		f.add(mine);
		f.pack();
	}
	
	@Override
	public void fieldExploded(FieldEvent e) {
		fieldState.lost=true;
		timer.stopTimer();
		for(int i = 0;i<fieldWidth;i++)
			for(int j = 0; j<fieldHeight;j++){
				if(fieldC[i+1][j+1].getState().isMine())
					fieldC[i+1][j+1].fullPress(false);
			}
		
		//TODO add highscore table and make this Dialog better looking
		JOptionPane.showMessageDialog(f, "Game Over");
	}
		
	
	
	
}
