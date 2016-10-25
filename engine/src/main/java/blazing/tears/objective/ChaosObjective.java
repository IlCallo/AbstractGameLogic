package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.zone.BaseZone;

public class ChaosObjective extends BaseObjective {
    private int mNumZones;

    public ChaosObjective(int numZones) {
        super("Your objective is to bring chaos in the city, for you to win "
                + numZones + " zone must be chaotic at the end of the turn");
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
