package jMinesweeper;

public class FieldEvent {

	private FieldControl field;
	
	public FieldEvent(FieldControl field){
		this.field = field;
	}
	
	public FieldControl getField(){
		return field;
	}
}
