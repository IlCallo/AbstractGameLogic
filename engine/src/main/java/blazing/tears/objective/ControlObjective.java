package blazing.tears.objective;

import blazing.tears.TurnGame;
import blazing.tears.group.Team;
import blazing.tears.zone.BaseZone;

public class ControlObjective extends BaseObjective {
    private int mNumZones;
    private Team mTeam;

    public ControlObjective(int numZones, Team team) {
        super("Your objective is to consolidate your influence on the city, for you to win "
                + numZones + " zones must under your control (you have more units than all other single teams on that zone) " +
                "at the end of the turn");
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
