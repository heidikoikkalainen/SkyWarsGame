package skyWarsGame;

import java.io.Serializable;

public abstract class Ship implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shipName;

	public Ship() {}

	public Ship(String shipName) {
		this.shipName = shipName;
	}

	public abstract String getPrintChar();

	public String getShipName() {
		return this.shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

}
