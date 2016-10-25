package blazing.tears.objective;

import blazing.tears.Building;
import blazing.tears.TurnGame;
import blazing.tears.group.Team;
import blazing.tears.zone.BaseZone;

public class RichObjective extends BaseObjective {
    private int mVictoryMoney;
    private Team mTeam;

    public RichObjective(int victoryMoney, Team team) {
        super("Your objective is to become the kingpin of the city in the easiest way: buying the enemies. " +
                "For you to win, you must have in your team reserve " + victoryMoney + " coins at the end of the turn");
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
