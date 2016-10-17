package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.zone.BaseZone;

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