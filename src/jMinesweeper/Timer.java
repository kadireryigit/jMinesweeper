package jMinesweeper;

import java.util.Observable;
import java.util.Observer;

public class Timer extends Observable implements Runnable{

	private long timeZero = 0;
	private long count = 0;
	private boolean run = false;
	private boolean init = true;
	private Thread TimerThread = null;
	private boolean hasObserver;
	
	public Timer(){
		TimerThread = new Thread(this);
		TimerThread.setDaemon(true);
	}
	
	@Override
	public void run() {
//		while(!Thread.interrupted()){
			while(true){
				if(run){
					if(init){
						timeZero = System.currentTimeMillis();
						init = false;
					}
					count = System.currentTimeMillis()-timeZero;
//					System.out.println("passed time: " + count);
					int sleeptime = (int) (1000-(count%1000));
					
						this.setChanged();
						this.notifyObservers();
					
					try {	
						Thread.sleep(sleeptime);
					} catch (InterruptedException e) {/*System.out.println("one int");*/}
		
				}else{
					if(!init){
						count = System.currentTimeMillis()-timeZero;
//						System.out.println("End time: " + count);	
						
					}
					this.setChanged();
					this.notifyObservers();
					try {
						while(!Thread.interrupted()){
							Thread.sleep(10000);//Nop Loop
						}
					} catch (InterruptedException e) {/*System.out.println("two int");*/}
					
				}
				
			}
//		}
		
	}

	public long getTimeInMillis(){
		return count;
	}
	
	public void startTimer(){
		
		if(!this.run){
			
//			System.out.println("Timer started");
			this.run = true;
			if(!TimerThread.isAlive())
				TimerThread.start();
			else
				TimerThread.interrupt();
		}	
		
		
	}
	
	public void stopTimer(){
		
		if(this.run){
//			System.out.println("Timer stopped");
			this.run = false;
			TimerThread.interrupt();
		}
	}
	
	public void reset(){
		this.init = true;
//		System.out.println("Timer reset");
		this.run = false;
		count = 0;
		this.setChanged();
		this.notifyObservers();
//		TimerThread.interrupt();
	}
	
	
	public static void main(String[]args){
		Timer t = new Timer();
		t.startTimer();
		try {
			Thread.sleep(5123);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.stopTimer();
		
	}

}
