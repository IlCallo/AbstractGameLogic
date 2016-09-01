package agl.impl;

import agl.impl.zone.BaseZone;

import java.util.List;

public class Board {
    private List<BaseZone> mZones;

    public Board(List<BaseZone> zones) {
        mZones = zones;
    }

    public List<BaseZone> getZones() {
        return mZones;
    }

    public void addZone(BaseZone zone) {
        mZones.add(zone);
    }
}
