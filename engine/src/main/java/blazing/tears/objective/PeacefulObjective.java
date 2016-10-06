package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.zone.BaseZone;

public class PeacefulObjective extends BaseObjective {
    private int mTurns;

    public PeacefulObjective(String description, int turns) {
        super(description);
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
