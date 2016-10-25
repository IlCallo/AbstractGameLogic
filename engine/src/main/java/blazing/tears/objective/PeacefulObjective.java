package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.zone.BaseZone;

public class PeacefulObjective extends BaseObjective {
    private int mTurns;

    public PeacefulObjective(int turns) {
        super("Your objective is to keep the city together during this crisis and maintain peace, for you to win there " +
                "must not be any chaotic zones at the end of the turn or the game must reach his " + turns + "th turn " +
                "without the victory of any other team");
        mTurns = turns;
    }

    @Override
    public boolean checkVictory(TurnGame game) {
        if (game.getTurn() == mTurns) {
            return true;
        }

        int count = 0;
        for (BaseZone z : game.getBoard().getZones()) {
            if (z.isChaotic()) {
                count++;
            }
        }
        return count == 0;
    }
}
