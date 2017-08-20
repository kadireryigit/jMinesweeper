package jMinesweeper;

import java.awt.Color;

public enum Number {

	ZERO(' ',Color.BLACK),
	ONE('1',new Color(0x0000FF)),
	TWO('2',new Color(0x007B00)),
	THREE('3',new Color(0xFF0000)),
	FOUR('4',new Color(0x00007B)),
	FIVE('5',new Color(0x7B0000)),
	SIX('6',new Color(0x008284)),
	SEVEN('7',new Color(0x840084)),
	EIGHT('8',Color.BLACK),
	FLAG('\u25BA',Color.RED),
	QUESTION('?',Color.green),
	MINE('\u00A4',Color.BLACK);
	
	private char num;
	private Color numCol;
	
	private Number(char num,Color col){
		this.numCol = col;
		this.num = num;
	}
	
	public Color getColor(){
		return numCol;
	}
	
	public char getChar(){
		return num;
	}
	
	public static Number random(){
		int rnd = (int) (Math.random()*8);
		switch(rnd){
		case 0: return Number.ONE;
		case 1: return Number.TWO;
		case 2: return Number.THREE;
		case 3: return Number.FOUR;
		case 4: return Number.FIVE;
		case 5: return Number.SIX;
		case 6: return Number.SEVEN;
		case 7: return Number.EIGHT;
		default: return Number.ONE;
		}
	}
}
