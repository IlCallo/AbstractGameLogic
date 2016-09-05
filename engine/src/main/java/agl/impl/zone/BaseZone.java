package agl.impl.zone;

import agl.impl.Building;
import agl.impl.Team;
import agl.impl.Unit;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseZone implements PowerUp {
    private int mId;
    private int mCost;
    private String mDescription;
    private boolean mChaotic;
    private ArrayList<Building> mBuildings;
    private ArrayList<Unit> mUnits;

    private ArrayList<BaseZone> mNearZones;
    private ArrayList<GeoLocation> mPerimeter;
    private GeoLocation mCenter;

    private HashMap<Team, Integer> mControlMap;
    private Team mController = null;

    public BaseZone(int id, int cost, String description) {
        mId = id;
        mCost = cost;
        mDescription = description;
        mChaotic = false;
        mControlMap = new HashMap<>();
        mNearZones = new ArrayList<>();

        mBuildings = new ArrayList<>();
        mUnits = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public boolean isChaotic() {
        return mChaotic;
    }

    void setChaotic(boolean chaotic) {
        mChaotic = chaotic;
    }

    public int getCost() {
        return mCost;
    }

    public String getDescription() {
        return mDescription;
    }

    public ArrayList<Building> getBuildings() {
        return mBuildings;
    }

    public void addBuilding(Building building) {
        mBuildings.add(building);
        updateController(building.getOwner(), 1);
    }

    public void removeBuilding(Building building) {
        mBuildings.remove(building);
        updateController(building.getOwner(), -1);
    }

    public ArrayList<Unit> getUnits() {
        return mUnits;
    }

    public void addUnit(Unit unit) {
        mUnits.add(unit);
        updateController(unit.getTeam(), 1);
    }

    public void removeUnit(Unit unit) {
        mUnits.remove(unit);
        updateController(unit.getTeam(), -1);
    }

    public ArrayList<BaseZone> getNearZones() {
        return mNearZones;
    }

    public void setNearZones(ArrayList<BaseZone> nearZones) {
        mNearZones = nearZones;
    }

    public boolean isNear(BaseZone z) {
        return mNearZones.contains(z);
    }

    public ArrayList<GeoLocation> getPerimeter() {
        return mPerimeter;
    }

    public void setPerimeter(ArrayList<GeoLocation> perimeter) {
        mPerimeter = perimeter;
        mCenter = calculateCentroid(perimeter);
    }

    public GeoLocation getCenter() {
        return mCenter;
    }

    public Team getController() {
        return mController;
    }

    public void clear() {
        mBuildings.clear();
        mUnits.clear();
        mControlMap.clear();

        mController = null;
    }

    private void updateController(Team team, int delta) {
        mControlMap.put(team, mControlMap.getOrDefault(team, 0) + delta);
        int max = 0;
        mController = null;

        for (Map.Entry<Team, Integer> e : mControlMap.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                mController = e.getKey();
            } else if (e.getValue() == max) {
                mController = null;
            }
        }
    }

    private GeoLocation calculateCentroid(ArrayList<GeoLocation> perimeter) {
        double x = 0.0;
        double y = 0.0;
        int pointCount = perimeter.size();
        for (GeoLocation vertex : perimeter) {
            x += vertex.latitude;
            y += vertex.longitude;
        }

        x = x / pointCount;
        y = y / pointCount;

        return new GeoLocation(x, y);
    }
}