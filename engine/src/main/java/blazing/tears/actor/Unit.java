package blazing.tears.actor;

import blazing.tears.FirebaseSync;
import blazing.tears.GameLogger;
import blazing.tears.action.Action;
import blazing.tears.action.ActionProvider;
import blazing.tears.group.Team;
import blazing.tears.role.Role;
import blazing.tears.role.RoleProvider;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.*;

import java.util.logging.Logger;

public class Unit extends BaseActor implements FirebaseSync {
    private Team mTeam;
    private int mZone;
    private GeoLocation mLocation;
    private int mMoney;

    public Unit(String id, Team team) {
        super(id);
        mTeam = team;

        mZone = 0;
        mLocation = new GeoLocation(0, 0);
        mMoney = 0;

        sync();
    }

    public Team getTeam() {
        return mTeam;
    }

    public void setTeam(Team team) {
        mTeam = team;
    }

    public int getZone() {
        return mZone;
    }

    public void setZone(int zone) {
        mZone = zone;
    }

    public GeoLocation getLocation() {
        return mLocation;
    }

    public void setLocation(GeoLocation location) {
        mLocation = location;
    }

    public int getMoney() {
        return mMoney;
    }

    public void setMoney(int money) {
        mMoney = money;
    }

    @Override
    public void sync() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("unit/" + getId());
        Logger logger = GameLogger.instance();

        logger.info("Unit " + getId() + " from team " + getTeam().getId() + " is now synchronizing");

        // Sync zone
        ref.child("zone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mZone = dataSnapshot.getValue(Long.class).intValue();
                logger.info("Unit " + getId() + " moved to zone " + mZone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });

        // Sync location
        ref.child("lastPosition").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLocation = new GeoLocation(dataSnapshot.child("latitude").getValue(Double.class),
                        dataSnapshot.child("longitude").getValue(Double.class));
                logger.info("Unit " + getId() + " moved to position " + mLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });

        // Sync location
        ref.child("lastPosition").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLocation = new GeoLocation(dataSnapshot.child("latitude").getValue(Double.class),
                        dataSnapshot.child("longitude").getValue(Double.class));
                logger.info("Unit " + getId() + " moved to position " + mLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });

        // Sync role
        ref.child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setRole(RoleProvider.getRole(Role.valueOf(dataSnapshot.getValue(String.class))));
                logger.info("Unit " + getId() + " role is now " + getRole());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });

        // Sync action
        ref.child("action").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setAction(ActionProvider.getAction(Action.valueOf(dataSnapshot.getValue(String.class))));
                logger.info("Unit " + getId() + " action is now " + getAction());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });

        // Sync money
        ref.child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMoney = dataSnapshot.getValue(Long.class).intValue();
                logger.info("Unit " + getId() + " has now " + mMoney + " coins in his pocket");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });
    }
}
