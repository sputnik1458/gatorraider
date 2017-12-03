
package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController {
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int[] update(Game game, long timeDue) {

		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();


		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for (int i = 0; i < actions.length; i++) {
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
			/*if (possibleDirs.size() != 0 && (i % 2 == 1))
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			else
				actions[i] = -1;
				*/
			Node n = game.getAttacker().getLocation();
			int attackerX = game.getAttacker().getLocation().getX();
			int attackerY = game.getAttacker().getLocation().getY();
			int defenderX = defender.getLocation().getX();
			int defenderY = defender.getLocation().getY();

			boolean approach = defender.isVulnerable();

			if (possibleDirs.size() != 0) {
				actions[i] = defender.getNextDir(n, !approach);


				}

			}



		return actions;
	}
}