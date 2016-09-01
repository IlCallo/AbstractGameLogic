package agl.impl;

import agl.core.aglObject;

public class Building extends aglObject {
    private Team mOwner;

    public Building(int id, Team owner) {
        super(id);
        mOwner = owner;
    }

    public Team getOwner() {
        return mOwner;
    }
}
