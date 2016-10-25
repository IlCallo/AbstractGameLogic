package blazing.tears.objective;

import blazing.tears.Building;
import blazing.tears.TurnGame;
import blazing.tears.actor.Unit;
import blazing.tears.group.Team;
import blazing.tears.zone.BaseZone;

public class OmnipresentObjective extends BaseObjective {
    private int mNumZones;
    private Team mTeam;

    public OmnipresentObjective(int numZones, Team team) {
        super("Your objective is to spread your spies in the city to become the puppeteer who holds the threads in " +
                "the shadows of this cities, for you to win there must be at least one unit in  at least " + numZones
                + " zones at the end of the turn");
        mNumZones = numZones;
        mTeam = team;
    }

    @Override
    public boolean checkVictory(TurnGame game) {
        int count = 0;
        for (BaseZone z : game.getBoard().getZones()) {
            boolean unitFound = false;
            for (Unit a : z.getUnits()) {
                if (a.getTeam().equals(mTeam)) {
                    count++;
                    unitFound = true;
                    break;
                }
            }

            if (!unitFound) {
                for (Building b : z.getBuildings()) {
                    if (b.getOwner().equals(mTeam)) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count >= mNumZones;
    }
}
