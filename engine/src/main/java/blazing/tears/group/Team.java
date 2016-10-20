package blazing.tears.group;

import blazing.tears.objective.Objective;
import blazing.tears.role.Role;
import blazing.tears.role.RoleProvider;

import java.util.Map;

public class Team extends BaseGroup {
    private Objective mObjective;
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

    public Objective getObjective() {
        return mObjective;
    }

    public void setObjective(Objective objective) {
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
}
