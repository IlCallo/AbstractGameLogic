package blazing.tears.group;

import blazing.tears.FirebaseSync;
import blazing.tears.GameLogger;
import blazing.tears.objective.BaseObjective;
import blazing.tears.role.Role;
import blazing.tears.role.RoleProvider;
import com.google.firebase.database.*;

import java.util.Map;
import java.util.logging.Logger;

public class Team extends BaseGroup implements FirebaseSync {
    private BaseObjective mObjective;
    private String mColor;
    private int mMoney;
    private Map<Role, Integer> mRolePool;

    public Team(String id, String name) {
        super(id, name);
        mColor = "transparent";
        mMoney = 0;

        // Initialize the standard main.role pool
        mRolePool = RoleProvider.getInitialRolePool();
    }

    public BaseObjective getObjective() {
        return mObjective;
    }

    public void setObjective(BaseObjective objective) {
        mObjective = objective;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public int getMoney() {
        return mMoney;
    }

    public void earnMoney(int earning) {
        mMoney += earning;
    }

    public boolean spendMoney(int loss) {
        if (mMoney - loss < 0) {
            return false;
        }
        mMoney -= loss;
        return true;
    }

    public Map<Role, Integer> getRolePool() {
        return mRolePool;
    }

    public void setRolePool(Map<Role, Integer> rolePool) {
        mRolePool = rolePool;
    }

    @Override
    public void sync() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("team/" + getId());
        Logger logger = GameLogger.instance();

        logger.info("Team " + getId() + " with name " + getName() + " and color " + mColor + " is now synchronizing");

        // Sync money reserve
        ref.child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMoney = dataSnapshot.getValue(Long.class).intValue();
                logger.info("Team " + getId() + " has now " + mMoney + " in his reserve");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.warning(databaseError.toException().toString());
            }
        });
    }
}
