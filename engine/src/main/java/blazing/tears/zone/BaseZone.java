package blazing.tears.zone;

import blazing.tears.Building;
import blazing.tears.FirebaseSync;
import blazing.tears.GameLogger;
import blazing.tears.actor.Unit;
import blazing.tears.group.Team;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class BaseZone extends PowerUp implements FirebaseSync {
    private int mId;
    private int mCost;
    private String mDescription;
    private boolean mChaotic;
    private int mDemons;
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
        mDemons = 0;
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

    public void setChaotic(boolean chaotic) {
        mChaotic = chaotic;
    }

    public int getDemons() {
        return mDemons;
    }

    public void addDemon() {
        mDemons++;
    }

    public void removeDemon() {
        if (mDemons > 0) {
            mDemons--;
        }
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

    @Override
    public void sync() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("zone/" + getId());
        Logger logger = GameLogger.instance();

        logger.info("Zone " + getId() + " is now synchronizing");

        // Sync chaotic status
        ref.child("chaotic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChaotic = dataSnapshot.getValue(Boolean.class);
                logger.info("Zone " + getId() + " is now " + (mChaotic ? "" : "not") + " chaotic");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });
    }
}