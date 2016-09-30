package agl.impl;

import agl.core.aglGroup;
import agl.impl.objective.Objective;
import agl.impl.role.RoleProvider;

import java.util.Map;

public class Team extends aglGroup {
    private Objective mObjective;
    private int mMoney;
    private Map<String, Integer> mRolePool;

    public Team(String id, String name) {
        super(id, name);
        mMoney = 0;

        // Initialize the standard role pool
        mRolePool = RoleProvider.getInitialRolePool();
    }

    public Objective getObjective() {
        return mObjective;
    }

    public void setObjective(Objective objective) {
        mObjective = objective;
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

    public Map<String, Integer> getRolePool() {
        return mRolePool;
    }

    public void setRolePool(Map<String, Integer> rolePool) {
        mRolePool = rolePool;
    }
}
