package agl.impl;

import agl.impl.zone.BaseZone;
import com.firebase.geofire.GeoLocation;

import java.util.List;

public class Board {
    private GeoLocation mCenter;
    private double mRadius;
    private List<BaseZone> mZones;

    public Board(GeoLocation center, double radius, List<BaseZone> zones) {
        mCenter = center;
        mRadius = radius;
        mZones = zones;
    }

    public GeoLocation getCenter() {
        return mCenter;
    }

    public double getRadius() {
        return mRadius;
    }

    public List<BaseZone> getZones() {
        return mZones;
    }
}