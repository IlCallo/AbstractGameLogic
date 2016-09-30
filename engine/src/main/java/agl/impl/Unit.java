package agl.impl;

import agl.core.aglActor;
import agl.core.aglRole;

public class Unit extends aglActor {
    private Team mTeam;

    public Unit(String id) {
        super(id);
    }

    public Unit(String id, Team team) {
        super(id);
        mTeam = team;
    }

    public Unit(String id, aglRole role) {
        super(id, role);
    }

    public Unit(String id, aglRole role, Team team) {
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
