package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController {

    private final int Chase = 0; // red
    private final int xInt = 1; // pink
    private final int yInt = 2; // orange
    private final int WildCard = 3; // blue

    boolean moveLeft = false;
    boolean moveUp = false;

    int prevAttackX = 0;
    int prevAttackY = 0;

    int[] moves = {0, 1, 2, 3}; // up right down left

    int x = 0;



    public void init(Game game) {
    }

    public void shutdown(Game game) {
    }

    public int[] update(Game game, long timeDue) {


        int[] actions = new int[Game.NUM_DEFENDER];
        List<Defender> enemies = game.getDefenders();
        List<Node> powerPills = game.getPowerPillList();




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

            int deltaX = attackerX - prevAttackX;
            int deltaY = attackerY - prevAttackY;

            if (deltaX > 0) {
                moveLeft = false;
            } else {
                moveLeft = true;
            }

            if (deltaY > 0) {
                moveUp = true;
            } else {
                moveUp = false;
            }


            boolean approach = !defender.isVulnerable();

            if (possibleDirs.size() != 0) {
                switch (i) {
                    case 0:
                        actions[i] = chase(n, approach, defender);
                        break;
                    case 1:
                        actions[i] = xint(n, approach, defender, powerPills);
                        break;
                    case 2:
                        actions[i] = yint(n, approach, defender, powerPills);
                        break;
                    case 3:
                        actions[i] = protect(n, approach, defender, powerPills);
                        break;
                }
//					actions[i] = defender.getNextDir(n, !approach);
            }
        }

        return actions;
    }

    private int chase(Node n, boolean a, Defender defender) {
        return defender.getNextDir(n, a);
    }

    private int xint(Node n, boolean a, Defender defender, List<Node> powerPills) {

        for (Node powerPill : powerPills) {
            if (n.getPathDistance(powerPill) < 20) {
                return defender.getNextDir(n, false);
            }
        }
        if (moveLeft) {
            try {
                if (a) {
                    Node threeLeft = n.getNeighbor(3).getNeighbor(3).getNeighbor(3);
                    return defender.getNextDir(threeLeft, a);
                } else {
                    return defender.getNextDir(n, a);
                }
            } catch (NullPointerException e) {
                return yint(n, a, defender, powerPills);
            }
        } else {
            try {
                if (a) {
                    Node threeRight = n.getNeighbor(1).getNeighbor(1).getNeighbor(1);
                    return defender.getNextDir(threeRight, a);
                } else {
                    return defender.getNextDir(n, a);
                }
            } catch (NullPointerException e) {
                return yint(n, a, defender, powerPills);
            }
        }

    }

    private int yint(Node n, boolean a, Defender defender, List<Node> powerPills) {
        for (Node powerPill : powerPills) {
            if (n.getPathDistance(powerPill) < 10) {
                return defender.getNextDir(n, false);
            }
        }
        if (moveUp) {
            try {
                if (a) {
                    Node threeUp = n.getNeighbor(0).getNeighbor(0).getNeighbor(0);
                    return defender.getNextDir(threeUp, a);
                } else {
                    return defender.getNextDir(n, a);
                }
            } catch (NullPointerException e) {
                return defender.getNextDir(n, a);
            }
        } else {
            try {
                if (a) {
                    Node threeDown = n.getNeighbor(2).getNeighbor(2).getNeighbor(2);
                    return defender.getNextDir(threeDown, a);
                } else {
                    return defender.getNextDir(n, a);
                }
            } catch (NullPointerException e) {
                return defender.getNextDir(n, a);
            }
        }
    }

    private int protect(Node n, boolean a, Defender defender, List<Node> powerPills) {
        for (Node powerPill : powerPills) {
            if (n.getPathDistance(powerPill) < 20) {
                return defender.getNextDir(n, false);
            }
        }
        if (a){
            if (n.getPathDistance(defender.getLocation()) < 10) {
                return chase(n, a, defender);
            } else {
                switch (x) {
                    case 0:
                        x++;
                        return x - 1;
                    case 1:
                        x++;
                        return x - 1;
                    case 2:
                        x++;
                        return x - 1;
                    case 3:
                        x = 0;
                        return 3;

                }
            }
        } else {
            return defender.getNextDir(n, a);
        }
        return -1;
    }
}