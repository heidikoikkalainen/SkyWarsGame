package skyWarsGame;

import java.io.Serializable;
import java.util.ArrayList;

public class Square implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<EnemyShip> theEnemyShips = new ArrayList<EnemyShip> ();
	private MasterShip theMasterShip;
	private int columnNumber;
	
	public Square() {}

	public Square(int columnNumber) {
		setColumnNumber(columnNumber);
	}

	public void addMasterShip(MasterShip m) {
		this.theMasterShip = m;
	}

	public void removeMasterShip() {
		this.theMasterShip = null;
	}

	public void addEnemyShip(EnemyShip e) {
		theEnemyShips.add(e);
	}

	public void removeEnemyShip(EnemyShip e) {
		theEnemyShips.remove(e);
	}

	public ArrayList<EnemyShip> getTheEnemyShips() {
		return theEnemyShips;
	}

	public MasterShip getTheMasterShip() {
		return this.theMasterShip;
	}

	public int getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

}
