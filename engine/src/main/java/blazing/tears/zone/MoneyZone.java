package blazing.tears.zone;

public class MoneyZone extends BaseZone {
    private int mMoney;

    public MoneyZone(int id, int cost, int money) {
        super(id, cost, "Earn " + money + " money");
        mMoney = money;
    }

    public int getMoney() {
        return mMoney;
    }

    @Override
    protected void powerUp() {
        getController().earnMoney(mMoney);
    }
}
