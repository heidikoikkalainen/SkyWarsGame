package skyWarsGame;

import java.io.Serializable;
import java.util.ArrayList;

public class Row implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList <Square> theSquares = new ArrayList <Square> ();
	private int rowNumber;
	
	public Row() {}

	public Row(int rowNumber) {
		setRowNumber(rowNumber);
	}

	public ArrayList<Square> getTheSquares() {
		return this.theSquares;
	}

	public int getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

}
