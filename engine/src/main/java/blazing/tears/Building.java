package blazing.tears;

import blazing.tears.group.Team;

public class Building {
    private String mId;
    private Team mOwner;

    public Building(String id, Team owner) {
        mId = id;
        mOwner = owner;
    }

    public String getId() {
        return mId;
    }

    public Team getOwner() {
        return mOwner;
    }
}
