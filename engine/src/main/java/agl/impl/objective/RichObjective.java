package agl.impl.objective;

import agl.impl.Building;
import agl.impl.Team;
import agl.impl.TurnGame;
import agl.impl.zone.BaseZone;

public class RichObjective extends BaseObjective {
    private int mVictoryMoney;
    private Team mTeam;

    public RichObjective(String description, int victoryMoney, Team team) {
        super(description);
        mVictoryMoney = victoryMoney;
        mTeam = team;
    }

    @Override
    public boolean checkVictory(TurnGame game) {
        int sum = mTeam.getMoney();
        for (BaseZone z : game.getBoard().getZones()) {
            for (Building b : z.getBuildings()) {
                if (b.getOwner().equals(mTeam)) {
                    sum += z.getCost();
                    break;
                }
            }
        }
        return sum >= mVictoryMoney;
    }
}
