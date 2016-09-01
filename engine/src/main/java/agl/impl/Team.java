package agl.impl;

import agl.core.aglGroup;
import agl.impl.objective.Objective;

public class Team extends aglGroup {
    private Objective mObjective;
    private int mMoney;

    public Team(String name) {
        super(name);
        mMoney = 0;
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
}
