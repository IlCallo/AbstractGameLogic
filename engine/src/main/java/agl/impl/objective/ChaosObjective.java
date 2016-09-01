package agl.impl.objective;

import agl.impl.TurnGame;
import agl.impl.zone.BaseZone;

public class ChaosObjective extends BaseObjective {
    private int mNumZones;

    public ChaosObjective(String description, int numZones) {
        super(description);
        mNumZones = numZones;
    }

    public int getNumZones() {
        return mNumZones;
    }

    @Override
    public boolean checkVictory(TurnGame game) {
        int count = 0;

        for (BaseZone z : game.getBoard().getZones()) {
            if (z.isChaotic()) {
                count++;
            }
        }

        return count >= mNumZones;
    }
}
