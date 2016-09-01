package agl.impl.zone;

import agl.impl.VariableParam;

public class MoneyZone extends BaseZone {
    private int mMoney;

    public MoneyZone(int id, int cost, int money) {
        super(id, cost, "Earn " + money + "money");
        mMoney = money;
    }

    public int getMoney() {
        return mMoney;
    }

    @Override
    public void usePower(VariableParam param) {
        getController().earnMoney(mMoney);
    }
}
