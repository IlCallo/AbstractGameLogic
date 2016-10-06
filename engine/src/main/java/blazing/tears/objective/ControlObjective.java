package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.group.Team;
import blazing.tears.zone.BaseZone;

public class ControlObjective extends BaseObjective {
    private int mNumZones;
    private Team mTeam;

    public ControlObjective(String description, int numZones, Team team) {
        super(description);
        mNumZones = numZones;
        mTeam = team;
    }

    @Override
    public boolean checkVictory(TurnGame game) {
        int count = 0;
        for (BaseZone z : game.getBoard().getZones()) {
            if (z.getController().equals(mTeam)) {
                count++;
            }
        }
        return count >= mNumZones;
    }
}
