package jMinesweeper;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {

	
	private static MineField mine;
	
	private static int startWidth = 32;
	private static int startHeight = 16;
	private static int startMines = 99;
	
	public static void main(String[] args) {
		JFrame f = new JFrame("jMineSweeper");

		//Frame settings
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
//		f.setMinimumSize(new Dimension(240,350));
		
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
//					mine.reset();
					mine.setDimensions(width, height);
					mine.setMineCount(mines);
//					System.out.println("draw new field");
					
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
		
		mine = new MineField(startWidth,startHeight);
//		mine.drawField();
		
		
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //arrange all panels vertically
		f.add(buttonPanel);
		f.add(mine);
		f.pack();
		
	}

	
}
