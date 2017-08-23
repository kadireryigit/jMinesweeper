package jMinesweeper;

public class FieldGenerator {

	private int[][] field;
	private int width;
	private int height;
	public static final int MINE_BLOCK = 16;
	private int nrMines;
	private int spawnX, spawnY;
	
	public FieldGenerator(){
		
	}
	
	
	
	public void placeMines(int nrMines){
		//clamp nr of Mines to minimum or maximum possible (0, and fieldsize)
		if(nrMines>(width*height))
			nrMines = width*height-9;
		if(nrMines<0)
			nrMines = 0;
		this.nrMines = nrMines;
		//Randomly place mines
				while(nrMines>0){
					int x,y;
					do{
						x = (int) (Math.random()*(width))+1;
						y = (int) (Math.random()*(height))+1;
					}while(field[x][y]==MINE_BLOCK);
					field[x][y] = MINE_BLOCK;
					nrMines--;
				}
	}
	
	public void placeMines2(int nrMines){
		//clamp nr of Mines to minimum or maximum possible (0, and fieldsize)
		if(nrMines>(width*height))
			nrMines = width*height;
		if(nrMines<0)
			nrMines = 0;
		this.nrMines = nrMines;
	}
	public void clearField(){
		this.field = new int[width+2][height+2];
	}
	
	public void genNumbers(){
		//Generate surrounding mine numbers
				for(int col = 1; col<width+1;col++){
					for(int row = 1; row<height+1;row++){
						if(field[col][row]!=MINE_BLOCK){
							//count surrounding mines
							int sumMines = 0;
							// bit 4 is indicating if block is a mine. a block can only have up to 8 neighbor mines. 
							// Therefore bits 0-3 are reserved for surrounding mine count
							sumMines += field[col-1][row-1]>>4;
							sumMines += field[col]  [row-1]>>4;
							sumMines += field[col+1][row-1]>>4;
							sumMines += field[col-1][row  ]>>4;
							sumMines += field[col+1][row  ]>>4;
							sumMines += field[col-1][row+1]>>4;
							sumMines += field[col]  [row+1]>>4;
							sumMines += field[col+1][row+1]>>4;
							field[col][row] = sumMines;
						}
					}
				}
	}
	
	public int[][] genFieldAt(int x, int y, int width, int height,  int nrMines){
		
		if(width<0) width = 1;
		if(height<0) height = 1;
		//we generate empty borders to prevent border cases
		this.field = new int[width+2][height+2];
		
		this.width = width;
		this.height = height;
		
		if(x<0)
			x=0;
		if(x>=width)
			x = width-1;
		if(y<0)
			y=0;
		if(y>=height)
			y = height-1;
		spawnX = x;
		spawnY = y;
		x++;y++;  //compensation for borders
//		int[][] playField = new int[width][height];
		
		//place dummy mines around spawn coordinates to prevent mine placer from placing mines around spawn
		field[x][y-1] 	= MINE_BLOCK;
		field[x-1][y-1] = MINE_BLOCK;
		field[x+1][y-1] = MINE_BLOCK;
		field[x][y] 	= MINE_BLOCK;
		field[x-1][y] 	= MINE_BLOCK;
		field[x+1][y] 	= MINE_BLOCK;
		field[x][y+1] 	= MINE_BLOCK;
		field[x-1][y+1] = MINE_BLOCK;
		field[x+1][y+1] = MINE_BLOCK;
		
		//randomly place mines
		placeMines(nrMines);
		
		//remove dummy mines at spawn
		field[x][y-1] 	= 0;
		field[x-1][y-1] = 0;
		field[x+1][y-1] = 0;
		field[x][y] 	= 0;
		field[x-1][y] 	= 0;
		field[x+1][y] 	= 0;
		field[x][y+1] 	= 0;
		field[x-1][y+1] = 0;
		field[x+1][y+1] = 0;
		
		genNumbers();
		
//		for(int i = 0;i<width;i++){
//			for(int j = 0; j<height;j++){
//				playField[i][j] = field[i+1][j+1];
//				
//			}
//		}
		
		return field;
	}
	
	public int[][] genField(int width, int height, int nrMines){

		if(width<0) width = 1;
		if(height<0) height = 1;
		//we generate empty borders to prevent border cases
		this.field = new int[width+2][height+2];
		
		this.width = width;
		this.height = height;
		
//		int[][] playField = new int[width][height];
		placeMines(nrMines);
		
		genNumbers();

//		for(int i = 0;i<width;i++){
//			for(int j = 0; j<height;j++){
//				playField[i][j] = field[i+1][j+1];
//			}
//		}
		
		return field;
	}
	
	@Override
	public String toString(){
		char topLeftCorner = '\u2554';
		char topRightCorner = '\u2557';
		char horizontalBorder = '\u2550';
		char verticalBorder = '\u2551';
		char bottomLeftCorner = '\u255a';
		char bottomRightCorner = '\u255D';
		char thinVertLine = '\u2502';
		char thinHorLine = '\u2500';
		char thinCross = '\u253c';
		char mineChar = '\u058D';
		StringBuffer sField = new StringBuffer();
		sField.append("jMinesweeper. Field: (" + width + " X " + height + ") mines: " + nrMines + " at spawn: ("+spawnX+","+spawnY+")\n");
		
		sField.append(topLeftCorner);	//special extended ascii characters to print pretty border
		for(int col = 0;col<width-1;col++ )sField.append(horizontalBorder +"" +horizontalBorder +"" +horizontalBorder +""+horizontalBorder);
		sField.append(horizontalBorder +""+horizontalBorder +""+horizontalBorder +""+topRightCorner);
		
		sField.append("\n");
		for(int row = 1; row<height+1; row++){
			sField.append(verticalBorder);
			for(int col = 1; col<width+1; col++){
				if(field[col][row]<MINE_BLOCK)
					sField.append(" " + field[col][row]+ " ");
				else sField.append(" "+mineChar+" ");
				if(col<width)
					sField.append(thinVertLine);
				
			}
			if(row<height){
				sField.append(verticalBorder + "\n" + verticalBorder);
				//draw vertical lines
				
				for(int col = 0; col<width; col++){
					if(col<width-1)
						sField.append(thinHorLine + ""+thinHorLine + ""+thinHorLine + "" +thinCross);
					else 
						sField.append(thinHorLine + "" +thinHorLine + ""+thinHorLine );
				}
			}
			sField.append(verticalBorder);
			sField.append("\n");
		}
		sField.append(bottomLeftCorner);	//special extended ascii characters to print pretty border
		for(int col = 0;col<width-1;col++ )sField.append(horizontalBorder +"" +horizontalBorder +"" +horizontalBorder +""+horizontalBorder);
		sField.append(horizontalBorder +""+horizontalBorder +""+horizontalBorder + "" + bottomRightCorner);
		
		return sField.toString();
}
	public void genSpeedTest(){
		long duration = 1000000000L; //1second 
		long delta;
		int iterations = 100;
		int genCount = 0;
		long start = System.nanoTime();
		do{
			for(int i = 0;i<iterations;i++){
				genField(width,height,width*height/5);
				clearField();
				}
			delta = System.nanoTime()-start;
//			System.out.println(delta);
			genCount+=iterations;
		}while(delta<duration);
		System.out.println("Generated " + genCount + " mines in a second");
	}
	public void emptyBordersTest(){
		boolean correct = true;
		
			for(int j = 0;j<field[0].length;j++){
				if(field[0][j]!=0){
					correct = false;
					System.out.println("Error in border: (0," + j+") value is: " +field[0][j] + "should be: 0");
				}
				if(field[field.length-1][j]!=0){
					correct = false;
					System.out.println("Error in border: ("+(field.length-1)+"," + j+") value is: " +field[field.length-1][j] + "should be: 0");
				}
			}
			for(int i = 0;i<field.length;i++){
				if(field[i][0]!=0){
					correct = false;
					System.out.println("Error in border: ("+i+",0) value is: " +field[i][0] + "should be: 0");
				}
				if(field[i][field[0].length-1]!=0){
					correct = false;
					System.out.println("Error in border: ("+ i +","+(field[0].length-1) +") value is: " +field[i][field[0].length-1] + "should be: 0");
				}
			}
			if(correct)
				System.out.println("All borders correct");
	}
	
	
	public void genFieldAtTest(){
		int[][] test;
		FieldGenerator fg = new FieldGenerator();
		boolean error = false;
		for(int i = 0; i<width;i++){
			for(int j = 0; j<height;j++){
				test  = fg.genFieldAt(i,j,width,height,nrMines);
				if(test[i][j]!=0){
					System.out.println("Error at gen: " + i + ", " + j +" should be 0 is: " + test[i][j]);
					System.out.println(fg);
					
					error = true;
				}
			}
		}
		if(!error) System.out.println("genFieldAt Test successful");
	}
	public static void main(String[]args){
		int w = 32, h = 16;
		int mines = 99;
		
		FieldGenerator test = new FieldGenerator();
		
//		test.genField(mines);
		test.genFieldAt(2, 2,w,h, mines);
//		test.emptyBordersTest();
		
		System.out.println(test);
		
		test.genFieldAtTest();
//		for(int i= 0;i<10;i++) test.genSpeedTest();
	}
}
