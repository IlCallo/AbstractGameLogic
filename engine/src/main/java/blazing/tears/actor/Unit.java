package blazing.tears.actor;

import blazing.tears.group.Team;
import blazing.tears.role.BaseRole;

public class Unit extends BaseActor {
    private Team mTeam;

    public Unit(String id) {
        super(id);
    }

    public Unit(String id, Team team) {
        super(id);
        mTeam = team;
    }

    public Unit(String id, BaseRole role) {
        super(id, role);
    }

    public Unit(String id, BaseRole role, Team team) {
        super(id, role);
        mTeam = team;
    }

    public Team getTeam() {
        return mTeam;
    }

    public void setTeam(Team team) {
        mTeam = team;
    }
}
