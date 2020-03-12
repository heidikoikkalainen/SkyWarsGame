package skyWarsGame;

import java.util.Random;

public class EnemyShipFactory {

	public EnemyShip getEnemyShip() {

		Random rand = new Random();
		int random;
		int typesOfEnemyShips = 3;

		random = rand.nextInt(typesOfEnemyShips);

		if(random == 0) {
			return new BattleCruiser();
		}
		if(random == 1) {
			return new BattleShooter();
		} else {
			return new BattleStar();
		}
	}

}
